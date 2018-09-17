package com.calak.jemmy.spiro.View.Splash;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.calak.jemmy.spiro.Data.Local.SessionManagemen;
import com.calak.jemmy.spiro.MainActivity;
import com.calak.jemmy.spiro.R;
import com.calak.jemmy.spiro.View.LoginRegis.Login.LoginActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        final SessionManagemen sessionManagemen = new SessionManagemen(this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!sessionManagemen.isLogin()) {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    finish();
                } else {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                }
            }
        }, 5000);
    }
}
