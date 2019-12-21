package com.example.aether.model;

import java.io.Serializable;

public class Slide implements Serializable {
    private int id;
    private int courseId;
    private String title;
    private String url;

    public Slide(int courseId, String title, String url) {
        this.courseId = courseId;
        this.title = title;
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public String getTitle() {
        return title;
    }
}
