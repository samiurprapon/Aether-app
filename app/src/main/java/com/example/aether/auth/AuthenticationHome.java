package com.example.aether.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.aether.R;
import com.example.aether.auth.emailAuth.EmailLoginActivity;
import com.example.aether.auth.emailAuth.EmailRegistrationActivity;


public class AuthenticationHome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication_home);

        Button mSignIn = findViewById(R.id.btn_sign_in);
        Button mSignUp = findViewById(R.id.btn_sign_up);


        mSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AuthenticationHome.this, EmailRegistrationActivity.class));
            }
        });

        mSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AuthenticationHome.this, EmailLoginActivity.class));
            }
        });

    }
}
