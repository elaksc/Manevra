package com.example.manevra;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.manevra.Model.GonderilenKitap;
import com.example.manevra.Model.Kullanici;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class ActivityEditProfile extends AppCompatActivity {

    ImageView close, image_profile;
    TextView save, tv_change;
    MaterialEditText passwordEdit;
    EditText kullaniciEdit;
    FirebaseDatabase database;
    StorageTask eklemeGorevi;
    FirebaseUser firebaseUser;
    CircleImageView profil;
    private ValueEventListener uploadRefListener;
    private List<Kullanici> kullanici = new ArrayList<>();
    private Uri mImageUrl;
    private StorageTask uploadTask;
    int intentCount = 0;
    String mail;
    StorageReference storageReference;


    private DatabaseReference editRef;
    private ValueEventListener editRefListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        database = FirebaseDatabase.getInstance();
        close = findViewById(R.id.closeEditProfile);
        profil = findViewById(R.id.imageProfileEditProfile);
        image_profile = findViewById(R.id.imageProfileEditProfile);
        save = findViewById(R.id.saveEditProfile);
        tv_change = findViewById(R.id.tv_change);
        kullaniciEdit = findViewById(R.id.mailEdit);
        passwordEdit = findViewById(R.id.passwordEdit);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference("Uploads");
        DatabaseReference reference = database.getReference("Kullanicilar/");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Kullanici user = dataSnapshot.getValue(Kullanici.class);
                //   kullaniciEdit.setText(user.getKullaniciAdi());
                passwordEdit.setText(user.getSifre());
                // Glide.with(getApplicationContext()).load(user.getResimUri()).into(image_profile);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        profil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tv_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity()
                        .setAspectRatio(1, 1)
                        .setCropShape(CropImageView.CropShape.OVAL)
                        .start(ActivityEditProfile.this);
            }
        });

        image_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity()
                        .setAspectRatio(1, 1)
                        .setCropShape(CropImageView.CropShape.OVAL)
                        .start(ActivityEditProfile.this);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (kullaniciEdit.length() > 5 && passwordEdit.length() > 5) {
                    updateProfile(kullaniciEdit.getText().toString(), passwordEdit.getText().toString());
                    changePassWord();
                    resetpasswoord();

                } else if (kullaniciEdit.length() > 5 && passwordEdit.getText().toString().equals("")) {
                    updateProfile(kullaniciEdit.getText().toString(), passwordEdit.getText().toString());
                } else {
                    if (kullaniciEdit.length() < 5)
                        kullaniciEdit.setError("En az 6 karakter");

                    if (passwordEdit.length() < 5)
                        passwordEdit.setError("En az 6 karakter");

                    Toast.makeText(ActivityEditProfile.this, "En az 6 karakter giriniz!", Toast.LENGTH_SHORT).show();
                }

            }

        });
        getUserData();
    }

    private void updateProfile(String kullaniciadi, String sifre) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Kullanicilar").child(firebaseUser.getUid());
        HashMap<String, Object> hashMap = new HashMap<>();
        if (sifre.equals("")){
            hashMap.put("kullaniciadi", kullaniciadi);
            Toast.makeText(ActivityEditProfile.this, "Kullanıcı Adı Değiştirildi!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ActivityEditProfile.this,ActivityProfile.class);
            startActivity(intent);
            finish();
        }else{
            hashMap.put("kullaniciadi", kullaniciadi);
            hashMap.put("sifre", sifre);
        }
        reference.updateChildren(hashMap);

    }

    private void resetpasswoord() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String emailaddress = mail;
        auth.sendPasswordResetEmail(emailaddress)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("TAG", "Email sent.");
                            Toast.makeText(getApplicationContext(), "Check Your Email", Toast.LENGTH_SHORT).show();
                            FirebaseAuth.getInstance().signOut();
                            Intent intent = new Intent(ActivityEditProfile.this, ActivityMain.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(ActivityEditProfile.this, "Şifre Değiştirilemedi!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void changePassWord() {
        if (!passwordEdit.getText().toString().equals("") && passwordEdit.getText().toString().length() > 5
                && !kullaniciEdit.getText().toString().equals("") && kullaniciEdit.getText().toString().length() > 4) {
            firebaseUser.updatePassword(passwordEdit.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(ActivityEditProfile.this, ActivityMain.class);
                        startActivity(intent);
                        finish();
                    } else {
                        resetpasswoord();
                        // Toast.makeText(ActivityEditProfile.this, "Şifre Değiştirilemedi!", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        } else {
            if (kullaniciEdit.length() < 4) {
                kullaniciEdit.setError("En az 6 karakter");
            }
            if (passwordEdit.length() < 4) {
                passwordEdit.setError("En az 6 karakter");
            }
            Toast.makeText(ActivityEditProfile.this, "Lütfen en az 6 karakter giriniz!", Toast.LENGTH_SHORT).show();
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }


    private void uploadImage() {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Yükleniyor...");
        pd.show();

        if (mImageUrl != null) {
            final StorageReference fileReference = storageReference.child(System.currentTimeMillis()
                    + "." + getFileExtension(mImageUrl));

            uploadTask = fileReference.putFile(mImageUrl);
            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUr = task.getResult();
                        String myUri = downloadUr.toString();

                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Kullanicilar").child(firebaseUser.getUid());
                        HashMap<String, Object> hash = new HashMap<>();
                        hash.put("resim", "" + myUri);

                        reference.updateChildren(hash);
                        pd.dismiss();
                    } else
                        Toast.makeText(ActivityEditProfile.this, "Yüklenemedi!", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ActivityEditProfile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else Toast.makeText(this, "Fotoğraf seçilmedi!", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            mImageUrl = result.getUri();

            uploadImage();
        } else Toast.makeText(this, "Hata!", Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserData();
    }

    private void getUserData() {
        kullaniciEdit = findViewById(R.id.mailEdit);
        image_profile = findViewById(R.id.imageProfileEditProfile);
        editRef = database.getReference("Kullanicilar/" + firebaseUser.getUid());
        editRefListener = editRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    if (snapshot.hasChildren()) {
                        if (snapshot.child("kullaniciadi").exists()) {
                            kullaniciEdit.setText(snapshot.child("kullaniciadi").getValue(String.class));
                        }
                        if (snapshot.child("mail").exists()) {
                            mail = snapshot.child("mail").getValue(String.class);

                        }
                        if (snapshot.child("resim").exists()) {
                            Picasso.get().load(snapshot.child("resim").getValue(String.class)).into(image_profile);
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
