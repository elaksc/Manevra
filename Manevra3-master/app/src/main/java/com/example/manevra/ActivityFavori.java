package com.example.manevra;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.manevra.Adapter.GonderilenKitapAdapter;
import com.example.manevra.Model.GonderilenKitap;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ActivityFavori extends AppCompatActivity {

    private DatabaseReference kitapYukleRef;
    FirebaseDatabase database;
    private ValueEventListener kitapYukleRefListener;
    List<GonderilenKitap> liste = new ArrayList<>();
    private RecyclerView recyclerView;
    GonderilenKitapAdapter recyclerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favori);
    }

    private void recyclerViewRun(List<GonderilenKitap> liste) {
        recyclerAdapter = new GonderilenKitapAdapter(liste, this);
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

        private void getUserData() {

            kitapYukleRef = database.getReference("likes/");
            kitapYukleRefListener = kitapYukleRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        if (snapshot.hasChildren()) {
                            liste.clear();

                            Collections.reverse(liste);
                            recyclerViewRun(liste);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
