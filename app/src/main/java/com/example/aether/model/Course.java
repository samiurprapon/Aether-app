package com.example.aether.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Course implements Serializable {

    @SerializedName("course_id")
    private int course_id;

    @SerializedName("course_code")
    private String course_code;

    @SerializedName("enroll_code")
    private String enroll_code;

    @SerializedName("course_name")
    private String course_name;

    @SerializedName("semester")
    private int semester;

    @SerializedName("teacher_token")
    private String teacher_token;


    public int getCourse_id() {
        return course_id;
    }

    public void setCourse_id(int course_id) {
        this.course_id = course_id;
    }

    public String getCourse_code() {
        return course_code;
    }

    public void setCourse_code(String course_code) {
        this.course_code = course_code;
    }

    public String getEnroll_code() {
        return enroll_code;
    }

    public void setEnroll_code(String enroll_code) {
        this.enroll_code = enroll_code;
    }

    public String getCourse_name() {
        return course_name;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public String getTeacher_token() {
        return teacher_token;
    }

    public void setTeacher_token(String teacher_token) {
        this.teacher_token = teacher_token;
    }
}
