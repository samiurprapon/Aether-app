package com.example.aether.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.aether.R;
import com.example.aether.utils.networking.RetrofitClient;
import com.example.aether.model.NewUser;
import com.google.firebase.auth.FirebaseAuth;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    private EditText mName;
    private EditText mId;
    private Button mSubmit;
    private ImageView boy;
    private ImageView girl;
    private ImageView starB;
    private ImageView starG;
    private int sex = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mName = findViewById(R.id.et_name);
        mId = findViewById(R.id.et_nsu_id);
        mSubmit = findViewById(R.id.btn_done);

        boy = findViewById(R.id.img_boy);
        girl = findViewById(R.id.img_girl);

        starB = findViewById(R.id.ic_b_star);
        starG = findViewById(R.id.ic_g_star);

        boy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                starB.setVisibility(View.VISIBLE);
                starG.setVisibility(View.GONE);
                sex = 1;
                return;
            }
        });

        girl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                starG.setVisibility(View.VISIBLE);
                starB.setVisibility(View.GONE);
                sex = 0;
                return;
            }
        });


        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createProfile(mName.getText().toString(), Integer.parseInt(mId.getText().toString()), sex);
            }

        });

    }

    private void createProfile(String name, int id, int sex) {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        NewUser user = new NewUser(uid, name, id, sex);

        Call<NewUser> call = RetrofitClient.getInstance()
                .getRetroApi()
                .createProfile(user);

        call.enqueue(new Callback<NewUser>() {
            @Override
            public void onResponse(@NonNull Call<NewUser> call, @NonNull Response<NewUser> response) {
                if(response.code() == 205) {
                    startActivity(new Intent(ProfileActivity.this, DashboardActivity.class));
                } else {
                    Toast.makeText(getApplicationContext(), "Some error occurred", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<NewUser> call, @NonNull Throwable t) {
                Toast.makeText(getApplicationContext(), "Some error occurred", Toast.LENGTH_LONG).show();
            }
        });

    }

}
