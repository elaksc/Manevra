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
import com.example.manevra.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class GonderilenKitapAdapter extends RecyclerView.Adapter<GonderilenKitapAdapter.ViewHolder> {
    private List<GonderilenKitap> liste;
    DatabaseReference likereference;
    Context mContext;
    Boolean testclick = false;
    private FirebaseUser firebaseUser;
    private GonderilenKitapAdapter.OnItemClickListener mListener;

    public GonderilenKitapAdapter(List<GonderilenKitap> liste, Context mContext) {
        this.liste = liste;
        this.mContext = mContext;
    }

    public void setCacheMenuRes(List<GonderilenKitap>  dataList) {
        this.liste = dataList;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(GonderilenKitapAdapter.OnItemClickListener listener) {
        this.mListener = listener;
    }


    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return liste.size();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.gonderilen_kitaplar, parent, false);
        return new ViewHolder(itemView, mListener);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GonderilenKitap gonderilenKitap = liste.get(position);
        holder.txtkullaniciAdi.setText(liste.get(position).getEkleyenKullanici());
        holder.txtkitapHakkindaYazi.setText(liste.get(position).getKitapHakkinda());
        holder.txtgonderen.setText(liste.get(position).getKitapAdi());
        Picasso.get().load(liste.get(position).getKitapGorseli()).into(holder.gonderiResmi);

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final String userid = firebaseUser.getUid();
        final String kitapid = gonderilenKitap.getKitapID();

        holder.getlikebuttonstatus(kitapid, userid);

        holder.begeniResmi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                testclick = true;

                likereference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (testclick == true) {
                            if (snapshot.child(kitapid).hasChild(userid)) {
                                likereference.child(kitapid).child(userid).removeValue();
                                testclick = false;
                            } else {
                                likereference.child(kitapid).child(userid).setValue(true);
                                testclick = false;
                            }

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }
        });


    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        public ImageView profilResmi, gonderiResmi, begeniResmi, begeniResmi2, yorumResmi;
        public TextView txtkullaniciAdi, txtfavori,
                txtgonderen, txtkitapHakkindaYazi, txtyorumlari;

        public ViewHolder(@NonNull View itemView, OnItemClickListener mListener) {
            super(itemView);

            profilResmi = itemView.findViewById(R.id.profilResmiGonderiOgesi);
            gonderiResmi = itemView.findViewById(R.id.KitapResmi_GonderiOgesi);
            begeniResmi = itemView.findViewById(R.id.begeni_GonderiOgesi);
            begeniResmi2 = itemView.findViewById(R.id.begeni_GonderiOgesiDolu);
            yorumResmi = itemView.findViewById(R.id.yorum_GonderiOgesi);
            txtkullaniciAdi = itemView.findViewById(R.id.txtKullaniciAdi_GonderiOgesi);
            txtfavori = itemView.findViewById(R.id.txt_begeniler_GonderiOgesi);
            txtgonderen = itemView.findViewById(R.id.txt_gonderen_GonderiOgesi);
            txtkitapHakkindaYazi = itemView.findViewById(R.id.txt_kitapHakkinda_GonderiOgesi);
            txtyorumlari = itemView.findViewById(R.id.txt_yorumlar_GonderiOgesi);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mListener.onItemClick(position);
                        }
                    }
                }
            });

        }

        public void getlikebuttonstatus(final String postkey, final String userid) {
            likereference = FirebaseDatabase.getInstance().getReference("likes");
            likereference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.child(postkey).hasChild(userid)) {
                        int likecount = (int) snapshot.child(postkey).getChildrenCount();
                        txtfavori.setText(likecount + " likes");
                        begeniResmi.setImageResource(R.drawable.ic_baseline_favorite_24);
                    } else {
                        int likecount = (int) snapshot.child(postkey).getChildrenCount();
                        txtfavori.setText(likecount + " likes");
                        begeniResmi.setImageResource(R.mipmap.ic_begeni);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }


    }




   /*
        GonderilenKitap gonderi = mGonderi.get(position);
        Glide.with(context).load(gonderi.setGonderilenKitapResmi(holder.gonderiResmi));
        holder.txtkitapHakkindaYazi.setVisibility(View.VISIBLE);
        holder.txtkitapHakkindaYazi.setText(gonderi.getGonderilenKitapHakkinda());

        //Log.d("getVeri1", String.valueOf(gonderi));

        //gonderenBilgisi(holder.profilResmi, holder.txtkullaniciAdi, holder.txtgonderen);
        DatabaseReference veriyolu = FirebaseDatabase.getInstance().getReference("Kullanıcılar").child("KullanıcıId");
        veriyolu.addValueEventListener(new ValueEventListener() {

            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Kullanici kullanici = snapshot.getValue(Kullanici.class);
                Glide.with(context).load(kullanici.setResimUri(holder.profilResmi));
                holder.txtkullaniciAdi.setText(kullanici.getKullaniciAdi());
                holder.txtgonderen.setText(kullanici.getKullaniciAdi());
                Log.d("gelenler", String.valueOf(veriyolu));
                // Log.d("getVeri", kullanici.setKullaniciAdi());





    */

 /*
        public ImageView profilResmi, gonderiResmi, begeniResmi, yorumResmi;
        public TextView txtkullaniciAdi, txtfavori,
                txtgonderen, txtkitapHakkindaYazi, txtyorumlari;


            profilResmi = itemView.findViewById(R.id.profilResmiGonderiOgesi);
            gonderiResmi = itemView.findViewById(R.id.KitapResmi_GonderiOgesi);
            begeniResmi = itemView.findViewById(R.id.begeni_GonderiOgesi);
            yorumResmi = itemView.findViewById(R.id.yorum_GonderiOgesi);
            txtkullaniciAdi = itemView.findViewById(R.id.txtKullaniciAdi_GonderiOgesi);
            txtfavori = itemView.findViewById(R.id.txt_begeniler_GonderiOgesi);
            txtgonderen = itemView.findViewById(R.id.txt_gonderen_GonderiOgesi);
            txtkitapHakkindaYazi = itemView.findViewById(R.id.txt_kitapHakkinda_GonderiOgesi);
            txtyorumlari = itemView.findViewById(R.id.txt_yorumlar_GonderiOgesi);


  */
}