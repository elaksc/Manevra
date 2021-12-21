package com.example.manevra;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.example.manevra.Adapter.GonderilenKitapAdapter;
import com.example.manevra.Model.GonderilenKitap;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ActivityFavori extends AppCompatActivity {

    private DatabaseReference kitapYukleRef, kitapFavCheckRef;
    FirebaseDatabase database;
    private ValueEventListener kitapYukleRefListener, kitapFavCheckRefListener;

    List<GonderilenKitap> liste = new ArrayList<>();
    private RecyclerView recyclerView;
    GonderilenKitapAdapter recyclerAdapter;
    ImageButton bckbtn;
    int intentCount = 0;
    String userid;
    private FirebaseUser firebaseUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favori);

        bckbtn =(ImageButton) findViewById(R.id.backBtn);
        bckbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(ActivityFavori.this,ActivityAnasayfa.class);
                startActivity(intent);


            }
        });
        database = FirebaseDatabase.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        assert firebaseUser != null;
        userid = firebaseUser.getUid().toString();

        recyclerView = findViewById(R.id.kitapList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.scrollToPosition(0);
        recyclerView.setLayoutManager(layoutManager);

        getUserData();
    }

    private void recyclerViewRun(List<GonderilenKitap> liste) {
        recyclerAdapter = new GonderilenKitapAdapter(liste, this);
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerAdapter.setOnItemClickListener(new GonderilenKitapAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (intentCount == 0) {
                    Intent intent = new Intent(ActivityFavori.this, ActivityComments.class);
                    intent.putExtra("KitapAdi", liste.get(position).getKitapAdi());
                    intent.putExtra("EkleyenKullanici", liste.get(position).getEkleyenKullanici());
                    intent.putExtra("KitapGorseli", liste.get(position).getKitapGorseli());
                    intent.putExtra("KitapHakkinda", liste.get(position).getKitapHakkinda());
                    intent.putExtra("KitapID", liste.get(position).getKitapID());
                    intent.putExtra("KitapYazari", liste.get(position).getKitapYazari());
                    startActivity(intent);
                    intentCount++;


                }
            }
        });
    }

    private void getUserData() {

        kitapYukleRef = database.getReference("/");
        kitapYukleRefListener = kitapYukleRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    if (snapshot.hasChildren()) {
                        if (snapshot.child("Eklenen_Kitaplar").exists() && snapshot.child("Eklenen_Kitaplar").hasChildren()) {
                            liste.clear();
                            for (int listeSize = 0; listeSize < snapshot.getChildrenCount(); listeSize++) {
                                if (snapshot.child("likes").exists() && snapshot.child("likes").hasChildren()) {
                                    if (snapshot.child("likes").child(String.valueOf(listeSize + 1)).exists() && snapshot.child("likes").child(String.valueOf(listeSize + 1)).hasChildren())
                                        if (snapshot.child("likes").child(String.valueOf(listeSize + 1)).child(userid).exists()) {
                                            if (snapshot.child("likes").child(String.valueOf(listeSize + 1)).child(userid).getValue().equals(true)) {
                                                Log.e("has5", liste.size() + "");
                                                liste.add(new GonderilenKitap(
                                                        snapshot.child("Eklenen_Kitaplar").child(String.valueOf(listeSize + 1)).child("kitapID").getValue(String.class),
                                                        snapshot.child("Eklenen_Kitaplar").child(String.valueOf(listeSize + 1)).child("kitapAdi").getValue(String.class),
                                                        snapshot.child("Eklenen_Kitaplar").child(String.valueOf(listeSize + 1)).child("kitapYazari").getValue(String.class),
                                                        snapshot.child("Eklenen_Kitaplar").child(String.valueOf(listeSize + 1)).child("kitapHakkinda").getValue(String.class),
                                                        snapshot.child("Eklenen_Kitaplar").child(String.valueOf(listeSize + 1)).child("kitapGorseli").getValue(String.class),
                                                        snapshot.child("Eklenen_Kitaplar").child(String.valueOf(listeSize + 1)).child("ekleyenKullanici").getValue(String.class)
                                                ));

                                            }
                                        }
                                }
                            }

                            Collections.reverse(liste);
                            recyclerViewRun(liste);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
