package com.badr.nwes.beyondbadr.adapters;

import android.content.Context;

/**
 * Created by Citec on 26/11/2016.
 */
public class ImageSimple {
    private String name;
    private String description;
    private int size_width;
    private double scaled;
    private Context context;

    public ImageSimple(String name, String description, int size_width, double scaled, Context context) {
        this.name = name;
        this.description = description;
        this.size_width = size_width;
        this.scaled = scaled;
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public int getSize_width() {
        return size_width;
    }

    public void setSize_width(int size_width) {
        this.size_width = size_width;
    }

    public double getScaled() {
        return scaled;
    }

    public void setScaled(double scaled) {
        this.scaled = scaled;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return String.format("%s (%s)", name, description);
    }
}
