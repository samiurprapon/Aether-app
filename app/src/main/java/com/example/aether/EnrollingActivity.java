package com.example.aether;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.aether.api.RetrofitClient;
import com.example.aether.api.models.Enroll;
import com.google.firebase.auth.FirebaseAuth;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EnrollingActivity extends AppCompatActivity {

    private Button mEnroll;
    private EditText mCourseCode;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enrolling);

        mEnroll = findViewById(R.id.btn_enroll);
        mCourseCode = findViewById(R.id.et_course_code);
        progressBar = findViewById(R.id.progressbar);


        mEnroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);


                verifyCourseCode(mCourseCode.getText().toString());
            }
        });
    }

    private void verifyCourseCode(String enrollCode) {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Call<Enroll> call = RetrofitClient.getInstance()
                .getRetroApi()
                .enroll(new Enroll(uid, enrollCode));

        call.enqueue(new Callback<Enroll>() {
            @Override
            public void onResponse(@NonNull Call<Enroll> call, @NonNull Response<Enroll> response) {
                if(response.code() == 201) {
                    Toast.makeText(getApplicationContext(), "Enrolled!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(EnrollingActivity.this, DashboardActivity.class));

                } else if(response.code() == 204) {
                    Toast.makeText(getApplicationContext(), "You already Enrolled!", Toast.LENGTH_SHORT).show();
                }

                progressBar.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onFailure(@NonNull Call<Enroll> call, @NonNull Throwable t) {
                Toast.makeText(getApplicationContext(), "No Course Found", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);

            }
        });


        //TODO
        // using api code need to be verified

    }


}
