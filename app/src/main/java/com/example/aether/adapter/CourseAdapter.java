package com.example.aether.adapter;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aether.api.models.Course;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

import com.example.aether.R;

/**
 * RecyclerView adapter class to render items
 * This class can go into another separate class, but for simplicity
 */
public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.MyViewHolder>  {

    private ArrayList<Course> courseList;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView mCourseTitle;
        ImageView thumbnail;

        MaterialCardView parentLayout;

        MyViewHolder(View view) {
            super(view);
            mCourseTitle = view.findViewById(R.id.tv_course_title);
            thumbnail = view.findViewById(R.id.thumbnail);
            parentLayout = view.findViewById(R.id.row_container);
        }
    }


    public CourseAdapter(Context context, ArrayList<Course> courseList) {
        this.courseList = courseList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.course_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        final Course course = courseList.get(position);
        holder.mCourseTitle.setText(String.valueOf(course.getEnrollCode()));
    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }


    // need to implement here to show search results

    public void updateCourse(List<Course> courses) {

        courseList = new ArrayList<>();
        courseList.addAll(courses);

        notifyDataSetChanged();
    }


}