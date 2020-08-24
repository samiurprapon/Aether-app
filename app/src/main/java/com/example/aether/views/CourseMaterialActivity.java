package com.example.aether.views;

import android.content.Context;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import com.example.aether.R;
import com.example.aether.utils.adapter.SlideAdapter;
import com.example.aether.model.Slide;
import com.example.aether.utils.callbacks.SwipeController;
import com.example.aether.utils.onClickListeners.RecyclerItemClickListener;
import com.example.aether.utils.GridSpacingItemDecoration;

public class CourseMaterialActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{

    private SwipeRefreshLayout swipeRefreshLayout;

    private ArrayList<Slide> slideList;
    private SlideAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_material);

        swipeRefreshLayout = findViewById(R.id.simpleSwipeRefreshLayout);
        slideList = new ArrayList<>();

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        mAdapter = new SlideAdapter(this, slideList);
        SearchView mSearch = findViewById(R.id.search_box);


        SwipeController swipeController = new SwipeController();
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeController);

        mSearch.setOnQueryTextListener(this);

        // recycler view configurations

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        recyclerView.setNestedScrollingEnabled(false);


        itemTouchHelper.attachToRecyclerView(recyclerView);

        // TODO
        // call getCourse Api

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, recyclerView,new RecyclerItemClickListener.OnItemClickListener() {

                    @Override
                    public void onItemClick(View view, int position) {

                        Slide slide = slideList.get(position);

                        Toast.makeText(getApplicationContext(),"Slide clicked : "+slide.getTitle(),Toast.LENGTH_SHORT).show();


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
                            // TODO
                            // here will be the background thread to call the slide list

                        }

                    }
                }, 3000);
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
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, r.getDisplayMetrics()));
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

        List<Slide> newList = new ArrayList<>();

        for (Slide slide: slideList) {
            if (slide.getTitle().toLowerCase().contains(userInput)) {

                newList.add(slide);

            }

        }

        mAdapter.updateCourse(newList);

        return true;
    }


}
