package com.example.aether.model;

public class Slides {
    private int id;
    private int courseId;
    private String title;
    private String url;
    private Slide[] slides;

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public Slide[] getSlides() {
        return slides;
    }
}
