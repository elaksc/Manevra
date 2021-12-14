package com.example.manevra;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class ActivityGiris  extends AppCompatActivity {

    EditText girisMail, girisSifre;
    Button girisButton;


    FirebaseAuth GirisYetkisi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giris2);
        girisMail = (EditText) findViewById(R.id.girisMail);
        girisSifre = (EditText) findViewById(R.id.girisSifre);
        girisButton = (Button) findViewById(R.id.btn_uyeGirisi);


        GirisYetkisi = FirebaseAuth.getInstance();
        girisButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressDialog pdGiris = new ProgressDialog(ActivityGiris.this);
                    pdGiris.setMessage("Giris Yapılıyor...");
                    pdGiris.show();

                    String str_mailGiris = girisMail.getText().toString();
                    String str_sifreGiris = girisSifre.getText().toString();

                    if (TextUtils.isEmpty(str_mailGiris)|| TextUtils.isEmpty(str_sifreGiris)){
                        Toast.makeText(ActivityGiris.this, "lÜTFEN BÜTÜN ALANLARI DOLDURUN!", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        GirisYetkisi.signInWithEmailAndPassword(str_mailGiris,str_sifreGiris)
                                .addOnCompleteListener(ActivityGiris.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        DatabaseReference yolGiris = FirebaseDatabase.getInstance().getReference()
                                                .child("Kullanıcılar").child(GirisYetkisi.getCurrentUser().getUid());
                                        yolGiris.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                pdGiris.dismiss();
                                                Intent intent = new Intent(ActivityGiris.this, ActivityAnasayfa.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(intent);
                                                finish();
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {


                                            }
                                        });
                                    }
                                    else
                                    {
                                        pdGiris.dismiss();
                                        Toast.makeText(ActivityGiris.this, "Giriş Başarısız", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });

                }




            }
        });





    }

}