package com.example.aether.auth.emailAuth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.aether.DashboardActivity;
import com.example.aether.ProfileActivity;
import com.example.aether.R;
import com.example.aether.api.RetrofitClient;
import com.example.aether.api.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmailLoginActivity extends AppCompatActivity {

    private EditText inputEmail;
    private EditText inputPassword;
    private FirebaseAuth auth;
    private ProgressBar progressBar;
    private CheckBox checkBox;

    private String email;
    private boolean isTeacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Get Fire base auth instance
        auth = FirebaseAuth.getInstance();

        // set the view now
        setContentView(R.layout.activity_email_login);

        inputEmail = findViewById(R.id.email);
        inputPassword = findViewById(R.id.password);
        checkBox = findViewById(R.id.cb_teacher);
        progressBar = findViewById(R.id.progressBar);


        TextView tvSignup = findViewById(R.id.btn_signup);
        Button btnLogin = findViewById(R.id.btn_login);
        Button btnReset = findViewById(R.id.btn_reset_password);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        tvSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EmailLoginActivity.this, EmailRegistrationActivity.class));
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EmailLoginActivity.this, ResetPasswordActivity.class));
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = inputEmail.getText().toString();
                final String password = inputPassword.getText().toString();
                isTeacher = checkBox.isChecked();


                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                //authenticate user
                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(EmailLoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                progressBar.setVisibility(View.GONE);
                                if (!task.isSuccessful()) {
                                    // there was an error
                                    if (password.length() < 6) {
                                        inputPassword.setError(getString(R.string.minimum_password));
                                    } else {
                                        Toast.makeText(EmailLoginActivity.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    // server call
                                    serverAuth();
                                }
                            }
                        });
            }
        });
    }

    private void serverAuth() {
        final String email = auth.getCurrentUser().getEmail();
        String uid = auth.getCurrentUser().getUid();

        User user = new User(email, uid, isTeacher);

        Call<User> call = RetrofitClient.getInstance()
                            .getRetroApi()
                            .authentication(user);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {

                if(response.code() == 202) {
                    Intent intent = new Intent(EmailLoginActivity.this, ProfileActivity.class);
                    startActivity(intent);

                } else if(response.code() == 200) {
                    Intent intent = new Intent(EmailLoginActivity.this, DashboardActivity.class);
                    startActivity(intent);

                } else {
                    auth.signOut();
                }

            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                Toast.makeText(EmailLoginActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                auth.signOut();

            }
        });


    }
}

