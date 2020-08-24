package com.example.aether.views.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.aether.views.DashboardActivity;
import com.example.aether.R;
import com.example.aether.views.auth.AuthenticationHome;
import com.google.firebase.auth.FirebaseAuth;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Thread timer = new Thread() {

            public void run() {
                try {
                    sleep(1000);// 4 second wait
                } catch (InterruptedException e) {
                    Log.d("ERROR", e.toString());
                    Thread.currentThread().interrupt();
                } finally {
                    applicationInitialize();
                }
            }
        };

        timer.start();
    }

//  Checking either logged in or a new user
    private void applicationInitialize() {
        if (FirebaseAuth.getInstance().getCurrentUser() != null){
            Intent intent = new Intent(SplashActivity.this, DashboardActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(SplashActivity.this, AuthenticationHome.class);
            startActivity(intent);
        }
        finish();
    }
}
