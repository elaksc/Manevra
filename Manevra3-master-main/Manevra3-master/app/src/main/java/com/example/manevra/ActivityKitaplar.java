package com.example.manevra;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.util.HashMap;
import java.util.UUID;

public class ActivityKitaplar extends AppCompatActivity {

    Uri resimUri;
    String benimUrim = "";
    StorageTask eklemeGorevi;
    StorageReference resimYukleme;
    ImageView image_kapat, image_eklendi;
    TextView kitap_ekleme;
    EditText edt_kitapAd, edt_kitapYazar, edt_kitapHakkinda;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kitaplar2);


        image_kapat = (ImageView) findViewById(R.id.close_kitap);
        image_eklendi = (ImageView) findViewById(R.id.eklenenKitapResmi);
        kitap_ekleme = (TextView) findViewById(R.id.txt_gonder);
        edt_kitapAd = (EditText) findViewById(R.id.kitapAdi);
        edt_kitapHakkinda = (EditText) findViewById(R.id.kitapHakkinda);
        edt_kitapYazar = (EditText) findViewById(R.id.kitapYazari);

        resimYukleme = FirebaseStorage.getInstance().getReference("kitaplar");
        image_kapat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivityKitaplar.this, ActivityAnasayfa.class));
                finish();
            }
        });

        kitap_ekleme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                resimYukle();
            }
        });
        image_eklendi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,2);
            }
        });


    }

    private String dosyaUzantisiAl(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));

    }

    private void resimYukle() {
        ProgressDialog pD = new ProgressDialog(this);
        pD.setMessage("Ekleniyor...");
        pD.show();


        if (resimUri != null) {
            StorageReference dosyayolu = resimYukleme.child(System.currentTimeMillis()
                    + "." + dosyaUzantisiAl(resimUri));
            eklemeGorevi = dosyayolu.putFile(resimUri);
            eklemeGorevi.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    return dosyayolu.getDownloadUrl();

                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri indirmeUri = task.getResult();
                        benimUrim = indirmeUri.toString();

                        DatabaseReference veriyolu = FirebaseDatabase.getInstance().getReference("Eklenen Kitaplar");
                        String kitapID = veriyolu.push().getKey();
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("KitapId", kitapID);
                        hashMap.put("KitapAdi", edt_kitapAd.getText().toString());
                        hashMap.put("KitapYazari", edt_kitapYazar.getText().toString());
                        hashMap.put("KitapHakkinda", edt_kitapHakkinda.getText().toString());
                        hashMap.put("KitapGorseli", benimUrim);
                        hashMap.put("EkleyenKullanici", FirebaseAuth.getInstance().getCurrentUser().getEmail());

                        veriyolu.child(kitapID).setValue(hashMap);

                        pD.dismiss();

                        startActivity(new Intent(ActivityKitaplar.this, ActivityAnasayfa.class));
                        finish();
                    } else {
                        Toast.makeText(ActivityKitaplar.this, "Ekleme Başarısızé", Toast.LENGTH_SHORT).show();

                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ActivityKitaplar.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(ActivityKitaplar.this, "Seçilen Resim Yok ", Toast.LENGTH_SHORT).show();
        }


        //Resim yükleme kodları
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == RESULT_OK && data !=null && data.getData() != null)
        {

            resimUri = data.getData();
            image_eklendi.setImageURI(resimUri);
        }
        else
        {
            Toast.makeText(ActivityKitaplar.this, "Resim Seçilemedi..", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(ActivityKitaplar.this, ActivityAnasayfa.class));
            finish();
        }

    }


}

