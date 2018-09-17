package com.calak.jemmy.spiro.Util;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class Singletone {
    private static Singletone mSingletone;
    private RequestQueue requestQueue;
    private static Context mcontext;

    private Singletone(Context context){
        mcontext = context;
        requestQueue = getRequestquee();
    }

    public RequestQueue getRequestquee(){
        if(requestQueue == null){
            requestQueue = Volley.newRequestQueue(mcontext.getApplicationContext());
        }
        return requestQueue;
    }

    public static synchronized Singletone getmSingletone(Context context){
        if(mSingletone == null){
            mSingletone = new Singletone(context);
        }
        return  mSingletone;
    }

    public<T> void addToRequestqueue(Request<T> request){
        requestQueue.add(request);
    }

    public void CancleRequest(Object tag){
        if(requestQueue != null){
            Log.d("request cancled","<<======");
            requestQueue.cancelAll(tag);
        }else{
            Log.d("request called","<<======");
        }
    }
}
