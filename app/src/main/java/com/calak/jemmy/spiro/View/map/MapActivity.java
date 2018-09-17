package com.calak.jemmy.spiro.View.map;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.calak.jemmy.spiro.Listener.MapListener;
import com.calak.jemmy.spiro.Model.Parkir;
import com.calak.jemmy.spiro.R;


import butterknife.BindView;
import butterknife.ButterKnife;

public class MapActivity extends AppCompatActivity implements MapListener {


    @BindView(R.id.mainFrame)
    FrameLayout frameLayout;
    Fragment currentFragment;
    FragmentManager manager;
    FragmentTransaction transaction;
    ActionBar actionBar;
    Intent intent;
    Parkir parkir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        ButterKnife.bind(this);

        initTolbar();
        GetData();
        SetMainLayout();

    }

    private void initTolbar() {
        setSupportActionBar((Toolbar)findViewById(R.id.toolbarCustem));
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(null);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                break;
        }

        // User didn't trigger a refresh, let the superclass handle this action
        return super.onOptionsItemSelected(item);
    }

    private void GetData() {
        intent = getIntent();
        parkir = (Parkir) intent.getSerializableExtra("data");
    }

    private void SetMainLayout() {
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", parkir);
        MapFragment fragment = new MapFragment();
        fragment.setArguments(bundle);

        manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();
        transaction.replace(R.id.mainFrame, fragment, MapFragment.class.getName());
        transaction.commit();
        currentFragment = fragment;
    }

    public void SwitchLayout(Fragment fragment){
        transaction = manager.beginTransaction();
        transaction.add(R.id.mainFrame, fragment);
        transaction.addToBackStack(fragment.getClass().getName());
        transaction.commit();
    }

    @Override
    public void OnFinish() {
        Log.d("onFinis", "<<===");
//        ((MapFragment)currentFragment).countDownTimer.start();
        MapFragment frag = (MapFragment) getSupportFragmentManager().findFragmentById(R.id.mainFrame);
        frag.StartCoundown();
    }
}
