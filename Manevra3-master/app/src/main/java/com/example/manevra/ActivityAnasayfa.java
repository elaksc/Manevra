package com.example.manevra;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.manevra.Adapter.GonderilenKitapAdapter;
import com.example.manevra.Model.GonderilenKitap;
import com.example.manevra.Model.ToDoModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentChange;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ActivityAnasayfa extends AppCompatActivity {

    FloatingActionButton btn;
    DrawerLayout cekmece;
    FirebaseDatabase database;
    private DatabaseReference kitapYukleRef,likereference;
    private ValueEventListener kitapYukleRefListener;
    private DatabaseReference dbref;
    ImageView begeni,dolubegeni;
    private RecyclerView recyclerView;
    GonderilenKitapAdapter recyclerAdapter;
    List<GonderilenKitap> liste = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anasayfa2);
        database = FirebaseDatabase.getInstance();
        cekmece = findViewById(R.id.cekmece_arkaplan);
        Button btnSearch = (Button) findViewById(R.id.btnsearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityAnasayfa.this, ActivityKronometre.class);
                startActivity(intent);
            }
        });
        likereference=FirebaseDatabase.getInstance().getReference("likes");

        btn = (FloatingActionButton) findViewById(R.id.ekleme);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityAnasayfa.this, ActivityKitaplar.class);
                startActivity(intent);

            }
        });

        recyclerView = findViewById(R.id.kitapList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.scrollToPosition(0);
        recyclerView.setLayoutManager(layoutManager);
        getUserData();

    }


    private void getUserData() {

        kitapYukleRef = database.getReference("Eklenen_Kitaplar/");
        kitapYukleRefListener = kitapYukleRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    if (snapshot.hasChildren()) {
                        liste.clear();
                        for (int listeSize = 0; listeSize < snapshot.getChildrenCount(); listeSize++) {
                            liste.add(new GonderilenKitap(
                                    snapshot.child(String.valueOf(listeSize + 1)).child("kitapID").getValue(String.class),
                                    snapshot.child(String.valueOf(listeSize + 1)).child("kitapAdi").getValue(String.class),
                                    snapshot.child(String.valueOf(listeSize + 1)).child("kitapYazari").getValue(String.class),
                                    snapshot.child(String.valueOf(listeSize + 1)).child("kitapHakkinda").getValue(String.class),
                                    snapshot.child(String.valueOf(listeSize + 1)).child("kitapGorseli").getValue(String.class),
                                    snapshot.child(String.valueOf(listeSize + 1)).child("ekleyenKullanici").getValue(String.class)
                                    ));
                        }
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
    private void recyclerViewRun(List<GonderilenKitap> liste) {
        recyclerAdapter = new GonderilenKitapAdapter(liste, this);
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
     /*     recyclerAdapter.setOnItemClickListener(new GonderilenKitapAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Toast.makeText(getApplicationContext(), "Position ~> " + position, Toast.LENGTH_SHORT).show();
              Intent gecis = new Intent(AdminPanelActivity.this, LoginActivity.class);
                startActivity(gecis);
             */
            }



    public void MenuyeTiklama(View view) {
        cekmeceyiac(cekmece);
    }

    public void cekmeceyiac(DrawerLayout cekmece) {
        cekmece.openDrawer(GravityCompat.START);
    }

    public void LogoyaTiklama(View view) {
        Intent intent = new Intent(ActivityAnasayfa.this, ActivityProfile.class);
        startActivity(intent);
        cekmeceyiKapat(cekmece);
    }

    public void cekmeceyiKapat(DrawerLayout cekmece) {
        if (cekmece.isDrawerOpen(GravityCompat.START)) {
            cekmece.closeDrawer(GravityCompat.START);
        }
    }

    public void AnasayfayaTiklama(View view) {
        recreate();
    }

    public void FavorilerimTiklama(View view) {
        Intent intent = new Intent(ActivityAnasayfa.this, ActivityFavori.class);
        startActivity(intent);
    }

    public void KronometreTiklama(View view) {
        Intent intent = new Intent(ActivityAnasayfa.this, ActivityKronometre.class);
        startActivity(intent);
    }

    public void ToDoListTiklama(View view) {
        Intent intent = new Intent(ActivityAnasayfa.this, ToDoListActivity.class);
        startActivity(intent);
    }

    public void ProfilimTiklama(View view) {
        Intent intent = new Intent(ActivityAnasayfa.this, ActivityProfile.class);
        startActivity(intent);
    }

    public void CikisTiklama(View view) {
        AlertDialog.Builder uyariPenceresi = new AlertDialog.Builder(ActivityAnasayfa.this);
        uyariPenceresi.setTitle("Çıkış");
        uyariPenceresi.setMessage("Çıkış Yapılsın Mı?");
        uyariPenceresi.setPositiveButton("EVET", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finishAffinity();
                Intent intent = new Intent(ActivityAnasayfa.this, ActivityMain.class);
                startActivity(intent);
            }
        });
        uyariPenceresi.setNegativeButton("HAYIR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                cekmeceyiKapat(cekmece);

            }
        });

        uyariPenceresi.show();
    }

    @Override
    protected void onPause() {
        cekmeceyiKapat(cekmece);
        super.onPause();
    }
}
