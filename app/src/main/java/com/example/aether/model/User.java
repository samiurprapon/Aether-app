package com.example.aether.model;

public class User {
    private String message;
    private String email;
    private String uid;
    private boolean isTeacher;

    public User(String email, String uid, boolean isTeacher) {
        this.email = email;
        this.uid = uid;
        this.isTeacher = isTeacher;
    }

    public String getMessage() {
        return message;
    }
}
