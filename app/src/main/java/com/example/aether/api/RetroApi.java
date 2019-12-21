package com.example.aether.api;

import com.example.aether.api.models.Course;
import com.example.aether.api.models.Courses;
import com.example.aether.api.models.Enroll;
import com.example.aether.api.models.NewUser;
import com.example.aether.api.models.User;

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


}
