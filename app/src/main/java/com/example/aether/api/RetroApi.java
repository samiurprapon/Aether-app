package com.example.aether.api;

import com.example.aether.model.Course;
import com.example.aether.model.Courses;
import com.example.aether.model.Enroll;
import com.example.aether.model.NewUser;
import com.example.aether.model.Slides;
import com.example.aether.model.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RetroApi {
    @POST("auth")
    Call<User> authentication(@Body User user);

    @POST("update")
    Call<NewUser> createProfile(@Body NewUser user);

    @POST("course/join")
    Call<Enroll> enroll(@Body Enroll enroll);

    @POST("course/list")
    Call<Courses> list(@Body Courses courses);

    @POST("course/slide/")
    Call<Slides> slideList(@Body Course course);


}
