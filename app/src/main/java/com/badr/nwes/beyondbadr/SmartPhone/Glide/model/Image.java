package com.badr.nwes.beyondbadr.SmartPhone.Glide.model;

import android.content.Context;
import android.support.v4.app.FragmentManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lincoln on 04/04/16.
 */
public class Image implements Serializable {
    private String name;
    private String small, medium, large;
    private String timestamp;

    //------------------------------------Solo para Fragment Badr----------------------------------------------
    private Context mcontext;
    private int position;
    private List<Image> images;
    private String fileJSON;

    public String getFileJSON() {
        return fileJSON;
    }

    public void setFileJSON(String fileJSON) {
        this.fileJSON = fileJSON;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public Context getMcontext() {
        return mcontext;
    }

    public void setMcontext(Context mcontext) {
        this.mcontext = mcontext;
    }

    //-----------------------------------------------------------------------------------------------------
    public Image() {
    }

    public Image(String name, String small, String medium, String large, String timestamp) {
        this.name = name;
        this.small = small;
        this.medium = medium;
        this.large = large;
        this.timestamp = timestamp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSmall() {
        return small;
    }

    public void setSmall(String small) {
        this.small = small;
    }

    public String getMedium() {
        return medium;
    }

    public void setMedium(String medium) {
        this.medium = medium;
    }

    public String getLarge() {
        return large;
    }

    public void setLarge(String large) {
        this.large = large;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}