package com.example.manevra.Model;

import android.net.Uri;
import android.widget.ImageView;

public class GonderilenKitap {
    private String kitapID;
    private String kitapAdi;
    private String kitapYazari;
    private String kitapHakkinda;
    private String kitapGorseli;
    private String ekleyenKullanici;

    public GonderilenKitap(String kitapID, String kitapAdi, String kitapYazari, String kitapHakkinda,
                           String  kitapGorseli, String ekleyenKullanici) {
        this.kitapID = kitapID;
        this.kitapAdi = kitapAdi;
        this.kitapYazari = kitapYazari;
        this.kitapHakkinda = kitapHakkinda;
        this.kitapGorseli = kitapGorseli;
        this.ekleyenKullanici = ekleyenKullanici;
    }

    public String getKitapID() {
        return kitapID;
    }

    public void setKitapID(String kitapID) {
        this.kitapID = kitapID;
    }

    public String getKitapAdi() {
        return kitapAdi;
    }

    public void setKitapAdi(String kitapAdi) {
        this.kitapAdi = kitapAdi;
    }

    public String getKitapYazari() {
        return kitapYazari;
    }

    public void setKitapYazari(String kitapYazari) {
        this.kitapYazari = kitapYazari;
    }

    public String getKitapHakkinda() {
        return kitapHakkinda;
    }

    public void setKitapHakkinda(String kitapHakkinda) {
        this.kitapHakkinda = kitapHakkinda;
    }

    public String getKitapGorseli() {
        return kitapGorseli;
    }

    public void setKitapGorseli(String kitapGorseli) {
        this.kitapGorseli = kitapGorseli;
    }

    public String getEkleyenKullanici() {
        return ekleyenKullanici;
    }

    public void setEkleyenKullanici(String ekleyenKullanici) {
        this.ekleyenKullanici = ekleyenKullanici;
    }
}