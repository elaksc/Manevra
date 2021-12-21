package com.example.manevra;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

public class ActivityMain extends AppCompatActivity {


    Button giris, uyeOl;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        giris = findViewById(R.id.uyegirisi);
        uyeOl = findViewById(R.id.uyeolma);

        if (!(currentUser == null)) {
            currentUser.reload().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Intent sayfaGecis2 = new Intent(ActivityMain.this
                            , ActivityAnasayfa.class);
                    startActivity(sayfaGecis2);
                    //arrangeUserExist();
                    Toast.makeText(ActivityMain.this, "Succesfully Logged In", Toast.LENGTH_LONG).show();
                    finish();
                }
            });
            currentUser.reload().addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    if (e instanceof FirebaseAuthInvalidUserException) {
                        giris.setEnabled(true);
                        uyeOl.setEnabled(true);
                        Log.d("MainActivity", "user doesn't exist anymore");
                        //createAnonymousAccount();
                    }
                }
            });
        }else{
            giris.setEnabled(true);
            uyeOl.setEnabled(true);
        }

        giris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sayfaGecis2 = new Intent(ActivityMain.this
                        , ActivityGiris.class);
                startActivity(sayfaGecis2);
            }
        });
        uyeOl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sayfaGecis3 = new Intent(ActivityMain.this
                        , ActivityUyeOl.class);
                startActivity(sayfaGecis3);
            }
        });

    }

    ;


}