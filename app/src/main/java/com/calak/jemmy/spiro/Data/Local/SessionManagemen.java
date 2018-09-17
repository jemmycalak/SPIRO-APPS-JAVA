package com.calak.jemmy.spiro.Data.Local;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.calak.jemmy.spiro.View.LoginRegis.Login.LoginActivity;

public class SessionManagemen {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;
    private int MODE=0;

    public static final String PREF_NAME="pref_ibc";

    public static final String KEY_TOKEN ="token";
    public static final String KEY_IDUSER ="iduser";
    public static final String KEY_ISLOGIN = "islogin";
    public static final String KEY_NAME ="name";
    public static final String KEY_PHONE ="phone";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_TGLLAHIR ="tgl_lahir";
    public static final String KEY_JENIS_KELAMIN = "jenis_kelamin";
    public static final String KEY_BALLANCE = "ballance";

    public static final String KEY_LOCATION="location";
    public static final String KEY_LATITUDE="latitude";
    public static final String KEY_LONGITUDE="longitude";
    public static final String KEY_LOCALCITY ="localcity";
    private String saldo;

    public SessionManagemen(Context context){
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void setDatauser(String id, String token, String name, String phone,
                            String email, String tgllahir, String jeniskelamin,
                            String ballance){
        editor.putString(KEY_IDUSER, id);
        editor.putString(KEY_TOKEN, token);
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_PHONE, phone);
        editor.putString(KEY_TGLLAHIR, tgllahir);
        editor.putString(KEY_JENIS_KELAMIN, jeniskelamin);
        editor.putString(KEY_BALLANCE, ballance);

        editor.commit();
    }


    public void setIsLogin(Boolean stts){
        editor.putBoolean(KEY_ISLOGIN, stts);
        editor.commit();
    }


    public void setLocation(String location, String latitude, String longitude, String kota){
        editor.putString(KEY_LOCATION, location);
        editor.putString(KEY_LATITUDE, latitude);
        editor.putString(KEY_LONGITUDE, longitude);
        editor.putString(KEY_LOCALCITY, kota);
        editor.commit();
    }

    public void doLogout(Activity activity){
        SharedPreferences logout = activity.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        logout.edit().clear().commit();

        Intent intent = new Intent(activity, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(intent);
    }

    public String getLocaCity(){
        return sharedPreferences.getString(KEY_LOCALCITY, "null");
    }
    public String getLongitude(){
        return sharedPreferences.getString(KEY_LONGITUDE, "null");
    }
    public String getLatitude(){
        return sharedPreferences.getString(KEY_LATITUDE, "null");
    }

    public Boolean isLogin(){
        return sharedPreferences.getBoolean(KEY_ISLOGIN, false);
    }

    public String getKeyToken(){
        return sharedPreferences.getString(KEY_TOKEN, "null");
    }

    public String getKeyName(){
        return sharedPreferences.getString(KEY_NAME, "null");
    }
    public String getKeyEmail(){
        return sharedPreferences.getString(KEY_EMAIL, "null");
    }
    public String getKeyPhone(){
        return sharedPreferences.getString(KEY_PHONE, "null");
    }
    public String getKeyJenisKelamin(){
        return sharedPreferences.getString(KEY_JENIS_KELAMIN, "null");
    }
    public String getKeyBallance(){
        return sharedPreferences.getString(KEY_BALLANCE, "0");
    }

    public void setSaldo(String saldo) {
        editor.putString(KEY_BALLANCE, saldo);
        editor.commit();
    }
}
