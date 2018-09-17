package com.calak.jemmy.spiro.Model;

import java.io.Serializable;

public class Tarif implements Serializable{
    String id, lokasi, tipe, tarif;

    public Tarif(String id, String lokasi, String tipe, String tarif) {
        this.id = id;
        this.lokasi = lokasi;
        this.tipe = tipe;
        this.tarif = tarif;
    }

    public String getId() {
        return id;
    }

    public String getLokasi() {
        return lokasi;
    }

    public String getTipe() {
        return tipe;
    }

    public String getTarif() {
        return tarif;
    }
}
