package com.vivi.reading.bean;

/**
 * Created by vivi on 2016/6/3.
 */
public class Article {
    private int id;
    private String title;
    private String info1;
    private String info2;
    private String author;
    private String date;
    private String imgUrl;
    private String content;
    private int result;

    public int getId() {
        return id;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public String getDate() {
        return date;
    }

    public String getInfo1() {
        return info1;
    }

    public String getInfo2() {
        return info2;
    }

    public String getTitle() {
        return title;
    }

    public int getResult() {
        return result;
    }
}
