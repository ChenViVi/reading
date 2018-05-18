package com.vivi.reading.bean;

/**
 * Created by ViVi on 2016/1/12.
 */
public class NavDrawerItem {
    private String title;
    private int imgId;


    public NavDrawerItem(String title,int imgId) {
        this.title = title;
        this.imgId = imgId;
    }

    public String getTitle() {
        return title;
    }

    public int getImgId() {
        return imgId;
    }
}
