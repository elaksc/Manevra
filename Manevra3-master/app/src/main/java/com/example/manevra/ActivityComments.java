package com.example.manevra;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.manevra.Adapter.CommentAdapter;
import com.example.manevra.Adapter.GonderilenKitapAdapter;
import com.example.manevra.Model.CommentModel;
import com.example.manevra.Model.GonderilenKitap;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class ActivityComments extends AppCompatActivity {
    Bundle bundle;
    String KitapAdi, EkleyenKullanici, KitapGorseli, KitapHakkinda, KitapID, KitapYazari;
    ImageView image, favoriteBtn;
    TextView userName, bookName, favorTxt;
    Button sendBtn;
    ImageButton bckbtn;
    EditText editMessage;

    private DatabaseReference commentRef, sentMessage;
    FirebaseDatabase database;
    private ValueEventListener commentRefListener;

    List<CommentModel> liste = new ArrayList<>();
    List<CommentModel> message = new ArrayList<>();
    private RecyclerView recyclerView;
    CommentAdapter recyclerAdapter;
    int commentCount = 0;
    String userid;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        bckbtn =(ImageButton) findViewById(R.id.backBtn);
        bckbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(ActivityComments.this,ActivityAnasayfa.class);
                startActivity(intent);


            }
        });
        bundle = getIntent().getExtras();
        database = FirebaseDatabase.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        assert firebaseUser != null;
        userid = firebaseUser.getUid().toString();
        if (bundle != null) {
            KitapAdi = bundle.getString("KitapAdi");
            EkleyenKullanici = bundle.getString("EkleyenKullanici");
            KitapGorseli = bundle.getString("KitapGorseli");
            KitapHakkinda = bundle.getString("KitapHakkinda");
            KitapID = bundle.getString("KitapID");
            KitapYazari = bundle.getString("KitapYazari");
            init();
            recyclerView = findViewById(R.id.commentRecyclerView);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            layoutManager.scrollToPosition(0);
            recyclerView.setLayoutManager(layoutManager);
            getUserData();
            sendBtn.setEnabled(true);
            sendBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!editMessage.getText().equals("")) {
                        message.clear();
                        sentMessage = database.getReference("Comments/" + KitapID + "/" + String.valueOf(commentCount + 1));
                        message.add(new CommentModel(EkleyenKullanici, editMessage.getText().toString(),
                                time()));
                        sentMessage.setValue(message.get(0));
                        editMessage.setText("");
                    }
                }
            });
        }
    }

    private void getUserData() {
        commentRef = database.getReference("Comments/" + KitapID);
        commentRefListener = commentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    if (snapshot.hasChildren()) {
                        liste.clear();
                        commentCount = (int) snapshot.getChildrenCount();
                        for (int listeSize = 0; listeSize < snapshot.getChildrenCount(); listeSize++) {
                            liste.add(new CommentModel(
                                    snapshot.child(String.valueOf(listeSize + 1)).child("ekleyenKullanici").getValue(String.class),
                                    snapshot.child(String.valueOf(listeSize + 1)).child("text").getValue(String.class),
                                    snapshot.child(String.valueOf(listeSize + 1)).child("date").getValue(Long.class)
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

    private void recyclerViewRun(List<CommentModel> liste) {
        recyclerAdapter = new CommentAdapter(liste, this);
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    private void init() {
        image = findViewById(R.id.KitapResmi_GonderiOgesi);
        favoriteBtn = findViewById(R.id.begeni_GonderiOgesi);
        userName = findViewById(R.id.txtKullaniciAdi_GonderiOgesi);
        bookName = findViewById(R.id.txt_gonderen_GonderiOgesi);
        favorTxt = findViewById(R.id.txt_begeniler_GonderiOgesi);
        editMessage = findViewById(R.id.editMessage);
        sendBtn = findViewById(R.id.sendButton);
        Picasso.get().load(KitapGorseli).into(image);
        userName.setText(EkleyenKullanici);
        bookName.setText(KitapAdi);
    }

    private Long time() {
        Calendar c = Calendar.getInstance();
        Long utcMilliseconds = c.getTimeInMillis();
        return utcMilliseconds;
    }
}