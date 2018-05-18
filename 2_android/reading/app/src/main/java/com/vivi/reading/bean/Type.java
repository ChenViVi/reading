package com.vivi.reading.bean;

/**
 * Created by vivi on 2018/4/14.
 */

public class Type {
    private int id;
    private String name;

    public Type(int id, String name){
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}
