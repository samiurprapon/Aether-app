package com.example.aether;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import com.example.aether.adapter.CourseAdapter;
import com.example.aether.callbacks.SwipeController;
import com.example.aether.model.Course;
import com.example.aether.onClickListeners.RecyclerItemClickListener;
import com.example.aether.ui.GridSpacingItemDecoration;

public class DashboardActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private SwipeRefreshLayout swipeRefreshLayout;
    private ArrayList<Course> courseArrayList;
    private Course[] courses;
    private CourseAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        swipeRefreshLayout = findViewById(R.id.simpleSwipeRefreshLayout);
        courseArrayList = new ArrayList<>();

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        mAdapter = new CourseAdapter(this, courseArrayList);
        SearchView mSearch = findViewById(R.id.search_box);


        SwipeController swipeController = new SwipeController();
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeController);

        mSearch.setOnQueryTextListener(this);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashboardActivity.this, EnrollingActivity.class);
                startActivity(intent);
            }
        });

        // recycler view configurations

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(8), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        recyclerView.setNestedScrollingEnabled(false);


        itemTouchHelper.attachToRecyclerView(recyclerView);

        // TODO
        // call getCourse Api
        if (checkNetworkConnection()) {
            // here will be the background thread to call the course list
            fetchCourses();
        }
//        else {
//
//            // collect course from shared preference
//        }

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, recyclerView,new RecyclerItemClickListener.OnItemClickListener() {

                    @Override
                    public void onItemClick(View view, int position) {

                        Course course = courseArrayList.get(position);
                        Intent intent = new Intent(DashboardActivity.this, SlidesActivity.class);
                        intent.putExtra("course", course);

                        // Toast.makeText(getApplicationContext(),"course clicked : "+course.getTitle(),Toast.LENGTH_SHORT).show();

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

                            fetchCourses();

                        }

                    }
                }, 3000);
            }
        });

    }

    private void fetchCourses() {

    }

    public boolean checkNetworkConnection() {


        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());

                if (capabilities != null) {
                    if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                        return true;
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                        return true;
                    } else {
                        return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET);
                    }
                }
            }
            else {
                try {
                    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

                    return (activeNetworkInfo != null && activeNetworkInfo.isConnected());

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return false;
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }


    /*
        onQueryTextSubmit(String s) and onQueryTextChange(String s) is to show search result from search bar
        this two methods are from SearchView OnQueryTextListener Interface
     */

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String string) {

        String userInput = string.toLowerCase() ;

        List<Course> newList = new ArrayList<>();

        for (Course course: courseArrayList) {
            if (course.getCourse_name().toLowerCase().contains(userInput) || course.getCourse_code().toLowerCase().contains(userInput)) {
                newList.add(course);
            }

        }

        mAdapter.updateCourse(newList);

        return true;
    }

}
