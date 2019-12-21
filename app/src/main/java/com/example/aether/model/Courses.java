package com.example.aether.model;

public class Courses {
    private String uid;
    private Course[] courses;

    public Courses(String uid) {
        this.uid = uid;
    }

    public Course[] getCourses() {
        return courses;
    }
}
