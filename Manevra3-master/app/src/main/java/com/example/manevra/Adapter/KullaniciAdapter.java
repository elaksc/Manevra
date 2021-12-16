package com.example.manevra.Adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.manevra.Model.Kullanici;
import com.example.manevra.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class KullaniciAdapter  extends RecyclerView.Adapter<KullaniciAdapter.ViewHolder>
{

    private Context context;
    public List<Kullanici> mKullanici;
    private FirebaseUser firebaseUser;

    public KullaniciAdapter(Context context, List<Kullanici> mKullanici) {
        this.context = context;
        this.mKullanici = mKullanici;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.kullanici_ogesi, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final Kullanici kullanici = mKullanici.get(position);
        holder.kullaniciadi.setText(kullanici.getKullaniciAdi());
        holder.ad.setText(kullanici.getAd());
        Glide.with(context).load(kullanici.getResimUri()).into(holder.profil);
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView kullaniciadi;
        public TextView ad;
        public CircleImageView profil;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            kullaniciadi = itemView.findViewById(R.id.txtKullaniciAdi_GonderiOgesi);
            ad = itemView.findViewById(R.id.txt_gonderen_GonderiOgesi);
            profil = itemView.findViewById(R.id.profilResmiGonderiOgesi);

        }
    }
}