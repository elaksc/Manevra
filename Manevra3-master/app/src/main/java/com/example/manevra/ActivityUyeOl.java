package com.example.manevra;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class ActivityUyeOl extends AppCompatActivity {

    EditText email, kullanici_adi, sifre;
    Button btn_uyeol;
    FirebaseDatabase database;
    FirebaseAuth yetki;
    DatabaseReference yol;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uye_ol3);
        database = FirebaseDatabase.getInstance();
        email = (EditText) findViewById(R.id.EditMail);
        kullanici_adi = (EditText) findViewById(R.id.EditKullaniciAdi);
        sifre = (EditText) findViewById(R.id.EditPassword);
        btn_uyeol = (Button) findViewById(R.id.btnuyeol);

        yetki = FirebaseAuth.getInstance();

        btn_uyeol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd = new ProgressDialog(ActivityUyeOl.this);
                pd.setMessage("Lütfen Bekleyin...");
                pd.show();
                String str_mail = email.getText().toString();
                String str_kullaniciAdi = kullanici_adi.getText().toString();
                String str_sifre = sifre.getText().toString();

                if (TextUtils.isEmpty(str_kullaniciAdi)|| TextUtils.isEmpty(str_mail)||
                        TextUtils.isEmpty(str_sifre)){
                    Toast.makeText(ActivityUyeOl.this, "lÜTFEN BÜTÜN ALANLARI DOLDURUN!", Toast.LENGTH_SHORT).show();
                }
                else if (str_sifre.length()<6){
                    Toast.makeText(ActivityUyeOl.this, "ŞİFRENİZ MİNİMUM 6 KARAKTERDEN OLUŞMALI", Toast.LENGTH_SHORT).show();
                }
                else{
                    uyeEkle(str_mail,str_kullaniciAdi,str_sifre);
                }


            }
        });
    }
    private void uyeEkle(String mail, String kullaniciAdi, String sifre){
        yetki.createUserWithEmailAndPassword(mail,sifre)
                .addOnCompleteListener(ActivityUyeOl.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            FirebaseUser firebaseUser = yetki.getCurrentUser();
                            String kullaniciId = firebaseUser.getUid();
                            yol = database.getReference("Kullanicilar/"+kullaniciId);
                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("id",kullaniciId);
                            hashMap.put("kullaniciadi",kullaniciAdi);
                            hashMap.put("mail",mail);
                            hashMap.put("sifre",sifre);
                            hashMap.put("resim","https://firebasestorage.googleapis.com/v0/b/manevra-5f2df.appspot.com/o/" +
                                    "default-user-image.png?alt=media&token=cbeefd51-2704-48c3-ac1a-4f245ef3a642");

                            yol.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        pd.dismiss();
                                        Intent intent = new Intent(ActivityUyeOl.this,ActivityAnasayfa.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    }
                                }
                            });
                        }
                        else{
                            pd.dismiss();
                            Toast.makeText(ActivityUyeOl.this, "Bu Mail veya Şifre ile Kayıt Başarısız", Toast.LENGTH_LONG).show();

                        }
                    }
                });
    }
}