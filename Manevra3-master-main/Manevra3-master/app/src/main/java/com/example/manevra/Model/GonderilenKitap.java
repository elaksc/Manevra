package com.example.manevra.Model;

import android.graphics.Bitmap;
import android.widget.ImageView;

public class GonderilenKitap {
    private String GonderilenKitapID;
    private ImageView GonderilenKitapResmi;
    private String GonderilenKitapHakkinda;
    private String Gonderen;

    public GonderilenKitap() {
    }

    public GonderilenKitap(String gonderilenKitapID, ImageView gonderilenKitapResmi, String gonderilenKitapHakkinda, String gonderen) {
        GonderilenKitapID = gonderilenKitapID;
        GonderilenKitapResmi = gonderilenKitapResmi;
        GonderilenKitapHakkinda = gonderilenKitapHakkinda;
        Gonderen = gonderen;
    }

    public String getGonderilenKitapID() {
        return GonderilenKitapID;
    }

    public void setGonderilenKitapID(String gonderilenKitapID) {
        GonderilenKitapID = gonderilenKitapID;
    }

    public ImageView getGonderilenKitapResmi() {
        return GonderilenKitapResmi;
    }

    public Bitmap setGonderilenKitapResmi(ImageView gonderilenKitapResmi) {
        GonderilenKitapResmi = gonderilenKitapResmi;
        return null;
    }

    public String getGonderilenKitapHakkinda() {
        return GonderilenKitapHakkinda;
    }

    public void setGonderilenKitapHakkinda(String gonderilenKitapHakkinda) {
        GonderilenKitapHakkinda = gonderilenKitapHakkinda;

    }

    public String getGonderen() {
        return Gonderen;
    }

    public void setGonderen(String gonderen) {
        Gonderen = gonderen;
    }
}