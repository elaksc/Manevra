package com.example.manevra;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ActivityMain extends AppCompatActivity {


    Button giris, uyeOl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);



        giris = findViewById(R.id.uyegirisi);
        giris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sayfaGecis2 = new Intent(ActivityMain.this
                        , ActivityGiris.class);
                startActivity(sayfaGecis2);
            }
        });
        uyeOl = findViewById(R.id.uyeolma);
        uyeOl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sayfaGecis3 = new Intent(ActivityMain.this
                        , ActivityUyeOl.class);
                startActivity(sayfaGecis3);
            }
        });

    };


}