package com.calak.jemmy.spiro.Data.Present;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.calak.jemmy.spiro.Data.Local.SessionManagemen;
import com.calak.jemmy.spiro.MainActivity;
import com.calak.jemmy.spiro.Model.Parkir;
import com.calak.jemmy.spiro.Model.Tarif;
import com.calak.jemmy.spiro.View.LoginRegis.Login.LoginActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PopulateData {
    public void setDataUserLogin(JSONObject data, Activity activity, Fragment fragment, String name) {
        SessionManagemen sessionManager = new SessionManagemen(activity);
        sessionManager.setIsLogin(true);
        try {
            String tgllhr, jnsKelamin, idUser, ballance;

            if (data.getString("id_user").equals("") || data.getString("id_user").equals(null)) {
                idUser = "null";
            } else {
                idUser = data.getString("tgl_lahir");
            }
            if (data.getString("tgl_lahir").equals("") || data.getString("tgl_lahir").equals(null)) {
                tgllhr = "null";
            } else {
                tgllhr = data.getString("tgl_lahir");
            }

            if (data.getString("jenis_kelamin").equals("") || data.getString("jenis_kelamin").equals(null)) {
                jnsKelamin = "null";
            } else {
                jnsKelamin = data.getString("jenis_kelamin");
            }
            if (data.getString("saldo").equals("") || data.getString("saldo").equals(null)) {
                ballance = "null";
            } else {
                ballance = data.getString("saldo");
            }


            sessionManager.setDatauser(
                    idUser,
                    data.getString("api_token"),
                    data.getString("nama_lengkap"),
                    data.getString("phone"),
                    data.getString("email"),
                    tgllhr,
                    jnsKelamin,
                    ballance
            );
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (name.equals(LoginActivity.class.getName())) {
            activity.startActivity(new Intent(activity, MainActivity.class));
            activity.finish();
        }
    }

    public void SetDataParkir(JSONArray data, Activity activity, Fragment fragment, String name) {
        try {
            ArrayList<Parkir> parkirs = new ArrayList<>();
            for (int i = 0; i < data.length(); i++) {
                JSONObject jsonObject = data.getJSONObject(i);
                JSONArray tarif = jsonObject.getJSONArray("tarif");

                ArrayList<Tarif> tarifs = new ArrayList<>();
                for (int j= 0; j < tarif.length(); j++){
                    JSONObject jsonObject1 = tarif.getJSONObject(j);
                    tarifs.add(new Tarif(
                            jsonObject1.getString("id_tarif"),
                            jsonObject1.getString("id_lokasi"),
                            jsonObject1.getString("nama_tipe"),
                            jsonObject1.getString("tarif")
                    ));
                }

                parkirs.add(new Parkir(
                        jsonObject.getString("id_lokasi"),
                        jsonObject.getString("nama_lokasi"),
                        jsonObject.getString("alamat_lokasi"),
                        jsonObject.getString("jumlah_slot"),
                        jsonObject.getString("img_lokasi"),
                        tarifs
                ));
            }
            ((MainActivity)activity).setDataParkir(parkirs);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void SetDataSaldo(String saldo, Activity activity, Fragment fragment, String name) {
        new SessionManagemen(activity).setSaldo(saldo);
        ((MainActivity)activity).setSaldo();
    }
}
