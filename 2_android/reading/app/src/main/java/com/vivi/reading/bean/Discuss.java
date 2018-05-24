package com.vivi.reading.bean;

import java.io.Serializable;

/**
 * Created by vivi on 2018/4/14.
 */

public class Discuss implements Serializable{
    private int id;
    private String user;
    private String type;
    private String title;
    private String content;
    private String date;

    public int getId() {
        return id;
    }

    public String getUser() {
        return user;
    }

    public String getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getDate() {
        return date;
    }
}
