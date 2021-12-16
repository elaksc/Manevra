package com.example.manevra;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ActivitySearch extends AppCompatActivity {
    DatabaseReference mref;
    private ListView listdata;
    private AutoCompleteTextView txtSearch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        mref = FirebaseDatabase.getInstance().getReference("Eklenen_Kitaplar ");
        listdata=(ListView) findViewById(R.id.listData);
        txtSearch=(AutoCompleteTextView)findViewById(R.id.txtSearch);

        ValueEventListener event =new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                populateSearch(snapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        mref.addListenerForSingleValueEvent(event);
    }

    private void populateSearch(DataSnapshot snapshot) {
        Log.d("Eklenen_Kitaplar","Reading Data");
        ArrayList<String> names=new ArrayList<>();
        if(snapshot.exists())
        {
            for (DataSnapshot ds:snapshot.getChildren()){
                String name=ds.child("kitapAdi").getValue(String.class);
                names.add(name);
            }
            ArrayAdapter adapter =new ArrayAdapter(this, android.R.layout.simple_list_item_1,names);
            txtSearch.setAdapter(adapter);
            txtSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String name=txtSearch.getText().toString();
                    searchUser(name);
                }
            });

        }else{
            Log.d("Eklenen_Kitaplar","No data found");
        }
    }

    private void searchUser(String name) {

    }
}
