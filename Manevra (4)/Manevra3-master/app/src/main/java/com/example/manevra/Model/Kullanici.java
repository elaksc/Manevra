package com.example.manevra.Model;

import android.graphics.Bitmap;
import android.widget.ImageView;

public class Kullanici {
    private String id;
    private String kullaniciAdi;
    private String Sifre;
    private String ad;
    private ImageView resimUri;

    public Kullanici(){

    }

    public String getSifre() {
        return Sifre;
    }

    public void setSifre(String sifre) {
        Sifre = sifre;
    }

    public Kullanici(String id, String kullaniciAdi, String sifre, String ad, ImageView resimUri) {
        this.id = id;
        this.kullaniciAdi = kullaniciAdi;
        Sifre = sifre;
        this.ad = ad;
        this.resimUri = resimUri;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKullaniciAdi() {
        return kullaniciAdi;
    }

    public String setKullaniciAdi(String kullaniciAdi)
    {
        this.kullaniciAdi = kullaniciAdi;
        return kullaniciAdi;
    }

    public String getAd() {
        return ad;
    }

    public void setAd(String ad) {
        this.ad = ad;
    }


    public ImageView getResimUri() {
        return resimUri;
    }

    public ImageView setResimUri(ImageView resimUri) {
        this.resimUri = resimUri;
        return null;
    }
}