package com.bignerdranch.android.photogallery;

import com.google.gson.annotations.SerializedName;

/**
 * Created by MA on 19/10/2017.
 */

public class PhotoRequestResult {
    @SerializedName("photos")
    private PhotoResults mPhotos;
    @SerializedName("stat")
    private String mStat;

    public PhotoResults getPhotos() {
        return mPhotos;
    }

    public void setPhotos(PhotoResults photos) {
        mPhotos = photos;
    }

    public String getStat() {
        return mStat;
    }

    public void setStat(String stat) {
        mStat = stat;
    }
}
