package com.example.aether.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.aether.ProfileActivity;
import com.google.firebase.auth.FirebaseAuth;

import com.example.aether.DashboardActivity;
import com.example.aether.R;
import com.example.aether.auth.AuthenticationHome;

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
            Intent intent = new Intent(SplashActivity.this, ProfileActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(SplashActivity.this, AuthenticationHome.class);
            startActivity(intent);
        }
        finish();
    }
}
