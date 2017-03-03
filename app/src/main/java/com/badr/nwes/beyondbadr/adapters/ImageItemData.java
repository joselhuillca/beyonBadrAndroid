package com.badr.nwes.beyondbadr.adapters;

import android.content.Context;
import android.widget.ImageView;

import com.badr.nwes.beyondbadr.data.BookDataSource;
import com.badr.nwes.beyondbadr.data.SectionDataSource;

/**
 * Created by Citec on 26/11/2016.
 */
public class ImageItemData {

    private String name;
    private String description;

    public Context context;

    private SectionDataSource sec;
    private int ind_chapter;
    private int ind_section;
    private int ind_image;
    private int size_with;
    private int size_heigh;
    private int numeroThumbalis_inScreen;

    private int[]end_pos_chapter_;
    private  int number_r;
    private int cont;

    public ImageItemData(){

    }

    public ImageItemData(String name, String description, Context context, SectionDataSource sec, int ind_chapter, int ind_section, int ind_image, int size_with, int size_heigh, int numeroThumbalis_inScreen, int[] end_pos_chapter_, int number_r, int cont) {
        this.name = name;
        this.description = description;
        this.context = context;
        this.sec = sec;
        this.ind_chapter = ind_chapter;
        this.ind_section = ind_section;
        this.ind_image = ind_image;
        this.size_with = size_with;
        this.size_heigh = size_heigh;
        this.numeroThumbalis_inScreen = numeroThumbalis_inScreen;
        this.end_pos_chapter_ = end_pos_chapter_;
        this.number_r = number_r;
        this.cont = cont;
    }

    public int[] getEnd_pos_chapter_() {
        return end_pos_chapter_;
    }

    public void setEnd_pos_chapter_(int[] end_pos_chapter_) {
        this.end_pos_chapter_ = end_pos_chapter_;
    }

    public int getNumber_r() {
        return number_r;
    }

    public void setNumber_r(int number_r) {
        this.number_r = number_r;
    }

    public int getCont() {
        return cont;
    }

    public void setCont(int cont) {
        this.cont = cont;
    }

    public int getInd_section() {
        return ind_section;
    }

    public void setInd_section(int ind_section) {
        this.ind_section = ind_section;
    }

    public int getInd_chapter() {
        return ind_chapter;
    }

    public void setInd_chapter(int ind_chapter) {
        this.ind_chapter = ind_chapter;
    }


    public int getSize_with() {
        return size_with;
    }

    public void setSize_with(int size_with) {
        this.size_with = size_with;
    }

    public int getNumeroThumbalis_inScreen() {
        return numeroThumbalis_inScreen;
    }

    public void setNumeroThumbalis_inScreen(int numeroThumbalis_inScreen) {
        this.numeroThumbalis_inScreen = numeroThumbalis_inScreen;
    }

    public int getSize_heigh() {
        return size_heigh;
    }

    public void setSize_heigh(int size_heigh) {
        this.size_heigh = size_heigh;
    }

    public int getInd_image() {
        return ind_image;
    }

    public void setInd_image(int ind_image) {
        this.ind_image = ind_image;
    }

    public SectionDataSource getSec() {
        return sec;
    }

    public void setSec(SectionDataSource sec) {
        this.sec = sec;
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

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }


}
