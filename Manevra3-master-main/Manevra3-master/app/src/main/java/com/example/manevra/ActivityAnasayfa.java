package com.example.manevra;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.manevra.Adapter.GonderilenKitapAdapter;
import com.example.manevra.Model.GonderilenKitap;
import com.example.manevra.Model.ToDoModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentChange;

import java.util.ArrayList;

public class ActivityAnasayfa extends AppCompatActivity {

    FloatingActionButton btn;
    DrawerLayout cekmece;
    private DatabaseReference dbref;
    private RecyclerView recyclerView;
    private ArrayList<GonderilenKitap> list =  new ArrayList<>();;
    private GonderilenKitapAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anasayfa2);
        cekmece=findViewById(R.id.cekmece_arkaplan);
        recyclerView = findViewById(R.id.kitapList);
        Button btnSearch =(Button)findViewById(R.id.btnsearch);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityAnasayfa.this,ActivityKronometre.class);
                startActivity(intent);
            }
        });
        btn = (FloatingActionButton) findViewById(R.id.ekleme);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityAnasayfa.this,ActivityKitaplar.class);
                startActivity(intent);

            }
        });

        dbref = FirebaseDatabase.getInstance().getReference("Eklenen Kitaplar");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        adapter = new GonderilenKitapAdapter(this,list);
        recyclerView.setAdapter(adapter);
        getUserData();

    }

    private void getUserData() {

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot child : snapshot.getChildren()) {
                    GonderilenKitap gonderilenKitap = snapshot.getValue(GonderilenKitap.class);
                    list.add(gonderilenKitap);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };
      dbref.addValueEventListener(postListener);

    }

    public void MenuyeTiklama(View view)
    {
        cekmeceyiac(cekmece);
    }

    public void cekmeceyiac(DrawerLayout cekmece) {
        cekmece.openDrawer(GravityCompat.START);
    }
    public void LogoyaTiklama(View view)
    {
        cekmeceyiKapat(cekmece);
    }

    public void cekmeceyiKapat(DrawerLayout cekmece) {
        if(cekmece.isDrawerOpen(GravityCompat.START))
        {
            cekmece.closeDrawer(GravityCompat.START);
        }
    }

    public void AnasayfayaTiklama(View view){
        recreate();
    }

    public void FavorilerimTiklama(View view){
        //Favori sayfası
    }

    public void KronometreTiklama(View view){
        Intent intent = new Intent(ActivityAnasayfa.this,ActivityKronometre.class);
        startActivity(intent);
    }

    public void ToDoListTiklama(View view){
        Intent intent = new Intent(ActivityAnasayfa.this,ToDoListActivity.class);
        startActivity(intent);    }

    public void ProfilimTiklama(View view){
        //Favori sayfası
    }

    public void CikisTiklama(View view) {
        AlertDialog.Builder uyariPenceresi = new AlertDialog.Builder(ActivityAnasayfa.this);
        uyariPenceresi.setTitle("Çıkış");
        uyariPenceresi.setMessage("Çıkış Yapılsın Mı?");
        uyariPenceresi.setPositiveButton("EVET", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finishAffinity();
                Intent intent =new Intent(ActivityAnasayfa.this,ActivityMain.class);
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
    protected void onPause(){
        cekmeceyiKapat(cekmece);
        super.onPause();
    }
}
