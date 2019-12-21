package com.example.aether.api.models;

public class NewUser {
    private String uid;
    private String username;
    private int nsuId;
    private int sex;

    public NewUser(String uid, String username, int nsuId, int sex) {
        this.uid = uid;
        this.username = username;
        this.nsuId = nsuId;
        this.sex = sex;
    }
}
