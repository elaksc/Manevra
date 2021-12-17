package com.example.manevra;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.imageview.ShapeableImageView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class ActivityProfile extends AppCompatActivity {

    ImageButton backBtn;
    ImageButton editbtn;
    ImageView editBtnn;
    ShapeableImageView profil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        editBtnn=findViewById(R.id.editBtnn);
        editBtnn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ıntent=new Intent(ActivityProfile.this,ActivityEditProfile.class);
                startActivity(ıntent);
            }
        });
        ImageView edittodo=findViewById(R.id.edittodo);
        edittodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ıntent=new Intent(ActivityProfile.this,ToDoListActivity.class);
                startActivity(ıntent);
            }
        });
        ImageView editfavori=findViewById(R.id.editfavori);
        editfavori.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ıntent=new Intent(ActivityProfile.this,ActivityFavori.class);
                startActivity(ıntent);
            }
        });
        ImageView editexit=findViewById(R.id.editexit);
        editexit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder uyariPenceresi = new AlertDialog.Builder(ActivityProfile.this);
                uyariPenceresi.setTitle("Çıkış");
                uyariPenceresi.setMessage("Çıkış Yapılsın Mı?");
                uyariPenceresi.setPositiveButton("EVET", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finishAffinity();
                        Intent intent = new Intent(ActivityProfile.this, ActivityMain.class);
                        startActivity(intent);
                    }
                });
                uyariPenceresi.setNegativeButton("HAYIR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                uyariPenceresi.show();
            }
        });

        backBtn=findViewById(R.id.geributon);
        backBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent ıntent=new Intent(ActivityProfile.this,ActivityAnasayfa.class);
                startActivity(ıntent);
            }
        });
        editbtn=findViewById(R.id.editbtn);
        editbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ıntent=new Intent(ActivityProfile.this,ActivityEditProfile.class);
                startActivity(ıntent);
            }
        });
     /*   profil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, 2);
            }
        });*/
    }
  /*  @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                profil.setImageBitmap(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(ActivityProfile.this, "Something went wrong", Toast.LENGTH_LONG).show();
            }

        } else {
            Toast.makeText(ActivityProfile.this, "You haven't picked Image", Toast.LENGTH_LONG).show();
        }
    }*/


}