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
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.manevra.Model.GonderilenKitap;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class ActivityKitaplar extends AppCompatActivity {

    Uri resimUri;
    String benimUrim = "";
    StorageTask eklemeGorevi;
    StorageReference resimYukleme;
    ImageView image_kapat, image_eklendi;
    TextView kitap_ekleme;
    EditText edt_kitapAd, edt_kitapYazar, edt_kitapHakkinda;

    // Firebase Reference
    FirebaseDatabase database;
    private DatabaseReference kitapRef, kitapCountRef;
    private ValueEventListener kitapRefListener, kitapCountRefListener;
    int kitapCount = 0;
    int intentCount = 0;

    private List<GonderilenKitap> gonderilenKitaps = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kitaplar2);

        database = FirebaseDatabase.getInstance();
        pullCategoryCount();
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
                startActivityForResult(galleryIntent, 2);
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
                        if (intentCount == 0){
                        Uri indirmeUri = task.getResult();
                        gonderilenKitaps.clear();
                        gonderilenKitaps.add(new GonderilenKitap(String.valueOf(kitapCount),
                                edt_kitapAd.getText().toString(),
                                edt_kitapYazar.getText().toString(), edt_kitapHakkinda.getText().toString(),
                                Objects.requireNonNull(task.getResult()).toString()
                                , Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail()));
                        if (gonderilenKitaps != null) {
                            kitapRef = database.getReference(("Eklenen_Kitaplar/" + kitapCount).trim());
                            kitapRef.setValue(gonderilenKitaps.get(0));
                            pD.dismiss();
                            startActivity(new Intent(ActivityKitaplar.this, ActivityAnasayfa.class));
                            finish();
                            intentCount++;
                        }

                    } }
                    else {
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
        if (requestCode == 2 && resultCode == RESULT_OK && data != null && data.getData() != null) {

            resimUri = data.getData();
            image_eklendi.setImageURI(resimUri);
        } else {
            Toast.makeText(ActivityKitaplar.this, "Resim Seçilemedi..", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(ActivityKitaplar.this, ActivityAnasayfa.class));
            finish();
        }

    }

    private void pullCategoryCount() {
        kitapCountRef = database.getReference("Eklenen_Kitaplar/");
        kitapCountRefListener = kitapCountRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    if (snapshot.hasChildren()) {
                        //recyclerViewRun(liste);
                        kitapCount = (int) (snapshot.getChildrenCount() + 1);
                    } else {
                        kitapCount = 1;
                    }
                }
                    else{
                        kitapCount = 1;
                    }
                }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}

