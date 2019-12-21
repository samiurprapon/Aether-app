package com.example.aether.model;

import java.io.Serializable;

public class Course implements Serializable {
    private int id;
    private int courseId;
    private String enrollCode;
    private int studentId;

    public Course(int courseId) {
        this.courseId = courseId;
    }

    public Course(int id, int courseId, String enrollCode, int studentId){
        this.id = id;
        this.courseId = courseId;
        this.enrollCode = enrollCode;
        this.studentId = studentId;
    }

    public String getEnrollCode() {
        return enrollCode;
    }

    public int getCourseId() {
        return courseId;
    }
}
