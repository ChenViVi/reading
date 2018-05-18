package com.vivi.reading.bean;

/**
 * Created by vivi on 2016/6/3.
 */
public class Article {
    private int id;
    private String title;
    private String info;

    private String type;
    private String date;

    private String content;
    private int result;

    public int getId() {
        return id;
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

    public int getResult() {
        return result;
    }
}
