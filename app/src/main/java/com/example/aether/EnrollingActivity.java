package com.example.aether;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

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
                String code = mCourseCode.getText().toString();

                verifyCourseCode(code);
                startActivity(new Intent(EnrollingActivity.this, DashboardActivity.class));
            }
        });
    }

    private void verifyCourseCode(String code) {

        //TODO
        // using api code need to be verified
        progressBar.setVisibility(View.INVISIBLE);
        Toast.makeText(EnrollingActivity.this, "Enrolled!", Toast.LENGTH_SHORT).show();

    }
}
