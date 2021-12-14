package com.example.manevra.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.manevra.Model.GonderilenKitap;
import com.example.manevra.Model.Kullanici;
import com.example.manevra.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.bumptech.glide.Glide;

import java.util.List;

public class GonderilenKitapAdapter extends RecyclerView.Adapter<GonderilenKitapAdapter.ViewHolder> {
    public Context context;
    public List<GonderilenKitap> mGonderi;

    private FirebaseUser mevcutFirebaseUser;

    public GonderilenKitapAdapter(Context context, List<GonderilenKitap> mGonderi) {
        this.context = context;
        this.mGonderi = mGonderi;
    }

    public GonderilenKitapAdapter() {

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.gonderilen_kitaplar, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        mevcutFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        GonderilenKitap gonderi = mGonderi.get(position);
        Glide.with(context).load(gonderi.setGonderilenKitapResmi(holder.gonderiResmi));
        holder.txtkitapHakkindaYazi.setVisibility(View.VISIBLE);
        holder.txtkitapHakkindaYazi.setText(gonderi.getGonderilenKitapHakkinda());

        //Log.d("getVeri1", String.valueOf(gonderi));

        //gonderenBilgisi(holder.profilResmi, holder.txtkullaniciAdi, holder.txtgonderen);
        DatabaseReference veriyolu = FirebaseDatabase.getInstance().getReference("Kullanıcılar").child("KullanıcıId");
        veriyolu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Kullanici kullanici = snapshot.getValue(Kullanici.class);
                Glide.with(context).load(kullanici.setResimUri(holder.profilResmi));
                holder.txtkullaniciAdi.setText(kullanici.getKullaniciAdi());
                holder.txtgonderen.setText(kullanici.getKullaniciAdi());
                Log.d("gelenler",String.valueOf(veriyolu));
               // Log.d("getVeri", kullanici.setKullaniciAdi());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return mGonderi.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView profilResmi, gonderiResmi, begeniResmi, yorumResmi;
        public TextView txtkullaniciAdi, txtfavori,
                txtgonderen, txtkitapHakkindaYazi, txtyorumlari;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profilResmi = itemView.findViewById(R.id.profilResmiGonderiOgesi);
            gonderiResmi = itemView.findViewById(R.id.KitapResmi_GonderiOgesi);
            begeniResmi = itemView.findViewById(R.id.begeni_GonderiOgesi);
            yorumResmi = itemView.findViewById(R.id.yorum_GonderiOgesi);

            txtkullaniciAdi = itemView.findViewById(R.id.txtKullaniciAdi_GonderiOgesi);
            txtfavori = itemView.findViewById(R.id.txt_begeniler_GonderiOgesi);
            txtgonderen = itemView.findViewById(R.id.txt_gonderen_GonderiOgesi);
            txtkitapHakkindaYazi = itemView.findViewById(R.id.txt_kitapHakkinda_GonderiOgesi);
            txtyorumlari = itemView.findViewById(R.id.txt_yorumlar_GonderiOgesi);

        }
    }

    /*private void gonderenBilgisi(ImageView profilR, TextView kullaniciadi, TextView gonderen) {
        DatabaseReference veriyolu = FirebaseDatabase.getInstance().getReference("Kullanıcılar").child("KullanıcıId");
        veriyolu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Kullanıcı kullanici = snapshot.getValue(Kullanıcı.class);
                Glide.with(context).load(kullanici.getResimUri()).into(profilR);
                kullaniciadi.setText(kullanici.getKullaniciAdi());
                gonderen.setText(kullanici.getKullaniciAdi());

                //Log.d("getVeri", String.valueOf(veriyolu));
                //Log.d("getVeri", kullanici.getKullaniciAdi());

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }*/
}