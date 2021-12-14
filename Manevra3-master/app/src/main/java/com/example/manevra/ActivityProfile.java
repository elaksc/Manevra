package com.example.manevra;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class ActivityProfile extends AppCompatActivity {

    ImageButton backBtn;
    ImageButton editbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        backBtn=findViewById(R.id.editbtn);
        backBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent ıntent=new Intent(ActivityProfile.this,ActivityProfile.class);
                startActivity(ıntent);
            }
        });
        editbtn=findViewById(R.id.editbtn);
        editbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ıntent=new Intent(ActivityProfile.this,ActivityEditProfile.class);
                startActivity(ıntent);
            }
        });
    }

}
