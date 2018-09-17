package com.calak.jemmy.spiro.Model;

import java.io.Serializable;
import java.util.ArrayList;

public class Parkir implements Serializable{
    String id, nama, alamat, jumlah_slot, img;
    ArrayList<Tarif> tarifs = new ArrayList<>();

    public Parkir(String id, String nama, String alamat, String jumlah_slot, String img, ArrayList<Tarif> tarifs) {
        this.id = id;
        this.nama = nama;
        this.alamat = alamat;
        this.jumlah_slot = jumlah_slot;
        this.img = img;
        this.tarifs = tarifs;
    }

    public String getId() {
        return id;
    }

    public String getNama() {
        return nama;
    }

    public String getAlamat() {
        return alamat;
    }

    public String getJumlah_slot() {
        return jumlah_slot;
    }

    public String getImg() {
        return img;
    }

    public ArrayList<Tarif> getTarifs() {
        return tarifs;
    }
}
