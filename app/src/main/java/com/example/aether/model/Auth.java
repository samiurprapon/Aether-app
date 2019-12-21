package com.example.aether.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Auth implements Serializable {

    @SerializedName("email")
    private String email;

    @SerializedName("isTeacher")
    private boolean isTeacher;

    public Auth(String email, boolean isTeacher) {
        this.email = email;
        this.isTeacher = isTeacher;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isTeacher() {
        return isTeacher;
    }

    public void setTeacher(boolean teacher) {
        isTeacher = teacher;
    }
}
