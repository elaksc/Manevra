package com.example.manevra.Model;

public class CommentModel {
    private String ekleyenKullanici;
    private String text;
    private long date;

    public CommentModel(String ekleyenKullanici, String text, long date) {
        this.ekleyenKullanici = ekleyenKullanici;
        this.text = text;
        this.date = date;
    }

    public String getEkleyenKullanici() {
        return ekleyenKullanici;
    }

    public void setEkleyenKullanici(String ekleyenKullanici) {
        this.ekleyenKullanici = ekleyenKullanici;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }
}

