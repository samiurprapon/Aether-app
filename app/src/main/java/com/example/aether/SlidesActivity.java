package com.example.aether;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.aether.adapter.SlideAdapter;
import com.example.aether.api.RetrofitClient;
import com.example.aether.callbacks.SwipeController;
import com.example.aether.model.Course;
import com.example.aether.model.Slide;
import com.example.aether.model.Slides;
import com.example.aether.onClickListeners.RecyclerItemClickListener;
import com.example.aether.ui.GridSpacingItemDecoration;

import java.util.ArrayList;
import java.util.Collections;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SlidesActivity extends Activity {

    private SwipeRefreshLayout swipeRefreshLayout;
    private ArrayList<Slide> slideArrayList;
    private Slide[] slides;
    private SlideAdapter mAdapter;
    private Course course;
    private int courseId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slides);

        course = (Course) getIntent().getSerializableExtra("course");
        courseId = course.getCourseId();

        TextView mCourseTitle = findViewById(R.id.tv_course_name);
        swipeRefreshLayout = findViewById(R.id.simpleSwipeRefreshLayout);
        slideArrayList = new ArrayList<>();

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        mAdapter = new SlideAdapter(this, slideArrayList);
        mCourseTitle.setText(course.getEnrollCode());


        SwipeController swipeController = new SwipeController();
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeController);


        // recycler view configurations

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        recyclerView.setNestedScrollingEnabled(false);


        itemTouchHelper.attachToRecyclerView(recyclerView);

        // TODO
        // call getSlide Api
        if (checkNetworkConnection()) {
            // here will be the background thread to call the course list
            fetchSlides();
        }
//        else {
//            // collect course from shared preference
//        }

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, recyclerView,new RecyclerItemClickListener.OnItemClickListener() {

                    @Override
                    public void onItemClick(View view, int position) {

                        Slide slide = slideArrayList.get(position);
                        Intent intent = new Intent(SlidesActivity.this, ReaderActivity.class);
                        intent.putExtra("course", course);
                        intent.putExtra("slide", slide);

                        //Toast.makeText(getApplicationContext(),"Slide clicked : "+slide.getTitle(),Toast.LENGTH_SHORT).show();

                        startActivity(intent);

                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                        // TODO
                        // On the upcoming version this long press feature will be added
                    }
                })
        );


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                // implement Handler to wait for 3 seconds and then update UI means update value of TextView
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);

                        if (checkNetworkConnection()) {
                            // here will be the background thread to call the course list

                            fetchSlides();

                        }

                    }
                }, 3000);
            }
        });

    }

    private void fetchSlides() {

        Call<Slides> call = RetrofitClient.getInstance()
                .getRetroApi()
                .slideList(new Course(courseId));

        call.enqueue(new Callback<Slides>() {
            @Override
            public void onResponse(@NonNull Call<Slides> call, @NonNull Response<Slides> response) {

                if(response.body() != null) {
                    slides = response.body().getSlides();
                    slideArrayList.clear();
                    Collections.addAll(slideArrayList, slides);
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Slides> call, @NonNull Throwable t) {
                Toast.makeText(getApplicationContext(), "Error : ", Toast.LENGTH_LONG).show();

            }
        });

    }

    public boolean checkNetworkConnection() {

        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return (networkInfo != null && networkInfo.isConnected());
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx() {
        int dp = 8;
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

}
