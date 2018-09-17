package com.calak.jemmy.spiro.View.LoginRegis.Login;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.calak.jemmy.spiro.Data.Online.API;
import com.calak.jemmy.spiro.Data.Online.Config;
import com.calak.jemmy.spiro.R;
import com.calak.jemmy.spiro.Services.Firebase.FirebaseInstans;
import com.calak.jemmy.spiro.View.LoginRegis.Regis.RegisActivity;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    @BindView(R.id.email)
    EditText email;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.btDaftar)
    TextView btDaftar;
    @BindView(R.id.btLogin)
    Button btLogin;

    String idFirebase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        initUI();
    }

    private void initUI() {
        btDaftar.setOnClickListener(this);
        btLogin.setOnClickListener(this);

        idFirebase = FirebaseInstanceId.getInstance().getToken();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btLogin:
                validate();
                break;
            case  R.id.btDaftar:
                startActivity(new Intent(this, RegisActivity.class));
                break;
        }
    }

    private void validate() {
        DoLogin();
    }

    private void DoLogin() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("email", email.getText().toString().toLowerCase());
            jsonObject.put("password", password.getText().toString());
            jsonObject.put("token_firebase", idFirebase);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        API.POST(jsonObject, (String) Config.login, this, new Fragment(), LoginActivity.class.getName());
    }

}
