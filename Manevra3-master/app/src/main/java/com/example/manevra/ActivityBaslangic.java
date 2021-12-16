package com.example.manevra;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class ActivityBaslangic extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baslangic);
        Handler handler = new Handler();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(ActivityBaslangic.this, ActivityMain.class);
                startActivity(intent);
                finish();
            }
        };
        handler.postDelayed(runnable, 6000);
    }
}