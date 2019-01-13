package com.example.user.task;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.google.firebase.database.FirebaseDatabase;

public class SplashscreenActivity extends AppCompatActivity {

    private ImageView image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);

        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashscreenActivity.this, FireBaseWindow.class));
                finish();
            }
        }, 3*600);
    }
}