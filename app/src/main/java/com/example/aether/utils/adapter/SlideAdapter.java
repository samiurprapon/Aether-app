package com.example.aether.utils.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aether.R;
import com.example.aether.model.Slide;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

public class SlideAdapter extends RecyclerView.Adapter<SlideAdapter.MyViewHolder>{


    private ArrayList<Slide> slideList;


    /**
     * RecyclerView adapter class to render items
     * This class can go into another separate class, but for simplicity
     */

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView mSlideTitle;

        MaterialCardView parentLayout;

        MyViewHolder(View view) {
            super(view);
            mSlideTitle = view.findViewById(R.id.tv_course_title);
            parentLayout = view.findViewById(R.id.row_container);
        }
    }


    public SlideAdapter(Context context, ArrayList<Slide> slideList) {
        this.slideList = slideList;
    }

    @NonNull
    @Override
    public SlideAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.slide_item, parent, false);     // TODO - layout need to change immediately

        return new SlideAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        final Slide slide = slideList.get(position);
        holder.mSlideTitle.setText(slide.getTitle());

    }

    @Override
    public int getItemCount() {
        return slideList.size();
    }


    // need to implement here to show search results

    public void updateCourse(List<Slide> slides) {

        slideList = new ArrayList<>();
        slideList.addAll(slides);

        notifyDataSetChanged();
    }
}

