package com.calak.jemmy.spiro.Data.Online;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.calak.jemmy.spiro.Data.Local.SessionManagemen;
import com.calak.jemmy.spiro.Data.Present.PopulateData;
import com.calak.jemmy.spiro.MainActivity;
import com.calak.jemmy.spiro.Util.AlertManager;
import com.calak.jemmy.spiro.Util.Loader;
import com.calak.jemmy.spiro.Util.Singletone;
import com.calak.jemmy.spiro.View.LoginRegis.Login.LoginActivity;
import com.calak.jemmy.spiro.View.LoginRegis.Regis.RegisActivity;
import com.calak.jemmy.spiro.View.map.MapFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class API extends Config{

    static Loader loader;
    public static void POST(final JSONObject jsonObject, final String url, final Activity activity, final Fragment fragment, final String name) {
        loader = new Loader();
        loader.startLoading(activity);
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Response", "" + response);
                try {
                    JSONObject json = new JSONObject(String.valueOf(response));
                    if (json.getString("status").equals("true")) {
                        if (name.equals(RegisActivity.class.getName())) {
                            new AlertManager().AlertOk(activity, "Selamat registrasi anda berhasil.", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    activity.finish();
                                }
                            });
                        }else if (name.equals(LoginActivity.class.getName())) {
                            new PopulateData().setDataUserLogin(json.getJSONObject("data"), activity, fragment, name);
                        }
                    } else {
                        /*false*/
                        String code = json.getString("code");

                        if (name.equals(RegisActivity.class.getName())) {
                            if (code.equals("404")) {
                                new AlertManager().AlertOk(activity, "Email atau nomor handphone telah terdaftar.", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                loader.stopLoading();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                loader.stopLoading();

                new AlertManager().AlertOkNo(activity, "Terjadi masalah pada koneksi, coba lagi ?", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        POST(jsonObject, url, activity, fragment, name);
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap hashMap = new HashMap();
                hashMap.put("apikey", APIKEY);
                return hashMap;
            }
        };
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(600000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Singletone.getmSingletone(activity).addToRequestqueue(jsonObjectRequest);
    }

    public static void GET(final String url, final Activity activity, final Fragment fragment, final String name) {
        loader = new Loader();
        if (!(name.equals(MainActivity.class.getName()) || name.equals(MainActivity.class.getName()+"saldo"))) {
            loader.startLoading(activity);
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("response", "" + response);
                try {
                    if (response.getString("status").equals("true")) {
                        if (name.equals(MainActivity.class.getName())) {
                            /*Profiel*/
                            new PopulateData().SetDataParkir(response.getJSONArray("data"), activity, fragment, name);
                        }else if(name.equals(MainActivity.class.getName()+"saldo")){
                            new PopulateData().SetDataSaldo(response.getString("saldo"), activity, fragment, name);
                        }
                    }else{
                        /*if false*/
                        new AlertManager().AlertOkNo(activity, "Server sedang penuh, coba lagi ?", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                GET(url, activity, fragment, name);
                            }
                        }, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });

                    }
                    if (!(name.equals(MainActivity.class.getName()) || name.equals(MainActivity.class.getName()+"profile") || name.equals(MainActivity.class.getName()+"tabungan"))) {
                        Log.d(name, "stopload<<====");
                        loader.stopLoading();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (!(name.equals(MainActivity.class.getName()))) {
                    loader.stopLoading();
                }
                new AlertManager().AlertOkNo(activity, "Server sedang penuh, coba lagi ?", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        GET(url, activity, fragment, name);
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap hashMap = new HashMap();
                hashMap.put("apikey", APIKEY);
                hashMap.put("Authorization", "Bearer " + new SessionManagemen(activity).getKeyToken());
                return hashMap;
            }
        };

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Singletone.getmSingletone(activity).addToRequestqueue(jsonObjectRequest);
    }

    public static void POSTS(final JSONObject jsonObject, final String url, final Activity activity, final Fragment fragment, final String name) {
        loader = new Loader();
        loader.startLoading(activity);
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Response", "" + response);
                try {
                    JSONObject json = new JSONObject(String.valueOf(response));
                    if (json.getString("status").equals("true")) {
                        if (name.equals(MapFragment.class.getName())) {
                            ((MapFragment)fragment).OnReservasiSuccess(json.getString("kode_reservasi"));
                        }else if( name.equals(MapFragment.class.getName()+"cancel")){
                            ((MapFragment)fragment).DoBatalReservasi("Reservasi dibatalkan, di lain waktu kami akan melayani anda sepenuh hati.");
                        }
                    } else {
                        /*false*/
                        new AlertManager().AlertOk(activity, response.getString("msg"), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                loader.stopLoading();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                loader.stopLoading();

                new AlertManager().AlertOkNo(activity, "Terjadi masalah pada koneksi, coba lagi ?", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        POST(jsonObject, url, activity, fragment, name);
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap hashMap = new HashMap();
                hashMap.put("apikey", APIKEY);
                hashMap.put("Authorization", "Bearer "+new SessionManagemen(activity).getKeyToken());
                return hashMap;
            }
        };
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(600000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Singletone.getmSingletone(activity).addToRequestqueue(jsonObjectRequest);
    }
}
