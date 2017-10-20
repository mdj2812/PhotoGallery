package com.bignerdranch.android.photogallery;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by MA on 19/10/2017.
 */

public class PhotoGalleryFragment extends Fragment {

    private static final String TAG = "PhotoGalleryFragment";

    private RecyclerView mPhotoRecyclerView;
    private GridLayoutManager mGridLayoutManager;
    private List<GalleryItem> mItems = new ArrayList<>();
    private PhotoAdapter mPhotoAdapter;
    private FlickrFetchr mFlickrFetchr = new FlickrFetchr();
    private int mTotalPage;
    private int mCurrentPage = 1;
    private int mItemPerPage;

    public static PhotoGalleryFragment newInstance() {
        return new PhotoGalleryFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        new FetchItemTask().execute(mCurrentPage);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_photo_gallery, container, false);

        mGridLayoutManager = new GridLayoutManager((getActivity()), 3);
        mPhotoRecyclerView = (RecyclerView) v.findViewById(R.id.photo_recycler_view);
        mPhotoRecyclerView.setLayoutManager(mGridLayoutManager);
        mPhotoRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0 || dx < 0) {
                    if (dy > 0 && mCurrentPage < mTotalPage && mGridLayoutManager.findLastVisibleItemPosition() >= (mItems.size() - 1)) {
                        mCurrentPage++;
                        new FetchItemTask().execute(mCurrentPage);
                    } else {
                        int firstVisibleItem = mGridLayoutManager.findFirstVisibleItemPosition();
                        int calcPage = 0;
                        if (firstVisibleItem < mItemPerPage) {
                            calcPage = 1;
                        } else {
                            calcPage = (firstVisibleItem / mItemPerPage) + (firstVisibleItem % mItemPerPage == 0 ? 0 : 1);
                        }
                        if (calcPage != mCurrentPage) {
                            mCurrentPage = calcPage;
                        }
                    }
                }
            }
        });

        setupAdapter();

        return v;
    }

    private void setupAdapter() {
        if (isAdded()) {
            if (mPhotoAdapter == null) {
                mPhotoAdapter = new PhotoAdapter(mItems);
            } else {
                mPhotoAdapter.addItems(mItems);
            }
            mPhotoRecyclerView.setAdapter(mPhotoAdapter);
        }
    }

    private class PhotoHolder extends RecyclerView.ViewHolder {
        private TextView mTitleTextView;

        public PhotoHolder(View itemView) {
            super(itemView);

            mTitleTextView = (TextView) itemView;
        }

        public void bindGalleryItem(GalleryItem item) {
            mTitleTextView.setText(item.toString());
        }
    }

    private class PhotoAdapter extends RecyclerView.Adapter<PhotoHolder> {

        private List<GalleryItem> mGalleryItems;

        public PhotoAdapter(List<GalleryItem> galleryItems) {
            mGalleryItems = galleryItems;
        }

        @Override
        public PhotoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            TextView textView = new TextView(getActivity());
            return new PhotoHolder(textView);
        }

        @Override
        public void onBindViewHolder(PhotoHolder holder, int position) {
            GalleryItem galleryItem = mGalleryItems.get(position);
            holder.bindGalleryItem(galleryItem);
        }

        @Override
        public int getItemCount() {
            return mGalleryItems.size();
        }

        public PhotoAdapter addItems(List<GalleryItem> galleryItems) {
            mGalleryItems.addAll(galleryItems);
            return this;
        }
    }

    private class FetchItemTask extends AsyncTask<Integer, Void, List<GalleryItem>> {
        @Override
        protected List<GalleryItem> doInBackground(Integer... params) {
            mFlickrFetchr = new FlickrFetchr();
            return mFlickrFetchr.fetchItems(params[0]);
        }

        @Override
        protected void onPostExecute(List<GalleryItem> items) {
            mItems = items;
            setupAdapter();

            mTotalPage = mFlickrFetchr.getPages();
            mItemPerPage = mFlickrFetchr.getPerpage();
        }
    }
}
