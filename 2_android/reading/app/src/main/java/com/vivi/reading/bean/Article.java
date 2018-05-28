package com.vivi.reading.bean;

import java.io.Serializable;

/**
 * Created by vivi on 2016/6/3.
 */
public class Article implements Serializable{
    private int id;
    private int typrId;
    private String title;
    private String info;
    private String type;
    private String date;

    private String content;

    public int getId() {
        return id;
    }

    public int getTyprId() {
        return typrId;
    }

    public String getType() {
        return type;
    }

    public String getContent() {
        return content;
    }

    public String getDate() {
        return date;
    }

    public String getInfo() {
        return info;
    }

    public String getTitle() {
        return title;
    }
}
