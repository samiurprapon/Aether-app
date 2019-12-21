package com.example.aether.api.models;

public class Course {
    private int id;
    private int courseId;
    private String enrollCode;
    private int studentId;

    public Course(int id, int courseId, String enrollCode, int studentId) {
        this.id = id;
        this.courseId = courseId;
        this.enrollCode = enrollCode;
        this.studentId = studentId;
    }
}
