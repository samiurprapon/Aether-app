package com.example.aether.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Slide implements Serializable {
    @SerializedName("slide_id")
    private int slide_id;

    @SerializedName("course_id")
    private int course_id;

    @SerializedName("title")
    private String title;

    @SerializedName("url")
    private String url;

    public int getSlide_id() {
        return slide_id;
    }

    public void setSlide_id(int slide_id) {
        this.slide_id = slide_id;
    }

    public int getCourse_id() {
        return course_id;
    }

    public void setCourse_id(int course_id) {
        this.course_id = course_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
