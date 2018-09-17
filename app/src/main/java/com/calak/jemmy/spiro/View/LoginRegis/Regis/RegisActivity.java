package com.calak.jemmy.spiro.View.LoginRegis.Regis;

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

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RegisActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.email)
    EditText email;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.nama)
    EditText nama;
    @BindView(R.id.tgl)
    EditText tgl;
    @BindView(R.id.gender)
    EditText gender;
    @BindView(R.id.phone)
    EditText phone;

    @BindView(R.id.btDaftar)
    Button btDaftar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regis);
        ButterKnife.bind(this);
        initUI();
    }

    private void initUI() {
        btDaftar.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btDaftar:
                DoRegis();
                break;
        }
    }

    private void DoRegis() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("nama_lengkap",nama.getText().toString());
            jsonObject.put("email",email.getText().toString());
            jsonObject.put("password",password.getText().toString());
            jsonObject.put("c_password",password.getText().toString());
            jsonObject.put("tgl_lahir",tgl.getText().toString());
            jsonObject.put("jenis_kelamin",gender.getText().toString());
            jsonObject.put("phone",phone.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        API.POST(jsonObject, (String) Config.register, this, new Fragment(), RegisActivity.class.getName());
    }
}
