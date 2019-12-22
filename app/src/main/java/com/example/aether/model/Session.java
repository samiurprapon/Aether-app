package com.example.aether.model;

public class Session {
    private int slideId;
    private String uid;
    private int slidePage;
    private String startTime;
    private String endTime;

    public Session(int slideId, String uid, int slidePage, String startTime, String endTime) {
        this.slideId = slideId;
        this.uid = uid;
        this.slidePage = slidePage;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
