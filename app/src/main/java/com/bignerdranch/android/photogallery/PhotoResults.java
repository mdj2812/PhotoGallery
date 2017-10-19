package com.bignerdranch.android.photogallery;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by MA on 19/10/2017.
 */

public class PhotoResults {
    @SerializedName("page")
    private int mPage;
    @SerializedName("pages")
    private int mPages;
    @SerializedName("perpage")
    private int mPerpage;
    @SerializedName("total")
    private int mTotal;
    @SerializedName("photo")
    private List<GalleryItem> mPhotos;

    public int getPage() {
        return mPage;
    }

    public void setPage(int page) {
        mPage = page;
    }

    public int getPages() {
        return mPages;
    }

    public void setPages(int pages) {
        mPages = pages;
    }

    public int getPerpage() {
        return mPerpage;
    }

    public void setPerpage(int perpage) {
        mPerpage = perpage;
    }

    public int getTotal() {
        return mTotal;
    }

    public void setTotal(int total) {
        mTotal = total;
    }

    public List<GalleryItem> getPhotos() {
        return mPhotos;
    }

    public void setPhotos(List<GalleryItem> photos) {
        mPhotos = photos;
    }
}
