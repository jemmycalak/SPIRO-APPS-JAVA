package com.calak.jemmy.spiro;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.calak.jemmy.spiro.Adapter.CustemRecyclerAdapter;
import com.calak.jemmy.spiro.Adapter.Holder.ParkirHolder;
import com.calak.jemmy.spiro.Data.Local.SessionManagemen;
import com.calak.jemmy.spiro.Data.Online.API;
import com.calak.jemmy.spiro.Data.Online.Config;
import com.calak.jemmy.spiro.Data.Online.Socket.EchoWebSocketListener;
import com.calak.jemmy.spiro.Model.Notification;
import com.calak.jemmy.spiro.Model.Parkir;
import com.calak.jemmy.spiro.Util.FormatMoney;
import com.calak.jemmy.spiro.View.Notification.NotificationFragment;
import com.calak.jemmy.spiro.View.map.MapActivity;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        intUIDrawer();
        iniUI();
        getData();

//        RunSocket();
//        connectWebSocket();

    }

    private WebSocketClient mWebSocketClient;

    private void connectWebSocket() {
        URI uri;
        try {
            uri = new URI("ws://103.233.109.183:5678");
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }

        mWebSocketClient = new WebSocketClient(uri) {
            @Override
            public void onOpen(ServerHandshake serverHandshake) {
                Log.i("onOpenWebsocket", "Opened"+ Build.MANUFACTURER+ " - " + Build.MODEL);
                mWebSocketClient.send("Hello from " + Build.MANUFACTURER + " " + Build.MODEL);
            }

            @Override
            public void onMessage(String s) {
                final String message = s;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("onOpenWebsocket", message);
                        Toast.makeText(MainActivity.this, ""+message, Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onClose(int i, String s, boolean b) {
                Log.i("onCloseWebsocket", "Closed " + s);
            }

            @Override
            public void onError(Exception e) {
                Log.i("onErrorWebsocket", "Error " + e.getMessage());
            }
        };
        mWebSocketClient.connect();
    }

    public void sendMessage(String m) {

        mWebSocketClient.send(m);
    }



    private void RunSocket() {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder().url("ws://103.233.109.183:5678").build();

        EchoWebSocketListener echoWebSocketListener = new EchoWebSocketListener("Hallo");
        WebSocket webSocket = client.newWebSocket(request, echoWebSocketListener);
//        client.dispatcher().executorService().shutdown();
    }

    private void getData() {
        API.GET(Config.getDataParkir, this, new Fragment(), MainActivity.class.getName());
    }

    private void iniUI() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void intUIDrawer() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View view = navigationView.getHeaderView(0);
        TextView name = (TextView)view.findViewById(R.id.nameHeader);
        TextView email = (TextView)view.findViewById(R.id.emailHeader);

        name.setText(new SessionManagemen(this).getKeyName());
        email.setText(new SessionManagemen(this).getKeyEmail());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    MenuItem menuItem;
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menuItem = menu.findItem(R.id.nav_saldo);
        menuItem.setTitle("Rp "+ FormatMoney.formatNumber(Double.parseDouble(new SessionManagemen(this).getKeyBallance())));
        return super.onPrepareOptionsMenu(menu);
    }

    public void setSaldo(){
        menuItem.setTitle("Rp "+ FormatMoney.formatNumber(Double.parseDouble(new SessionManagemen(this).getKeyBallance())));
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_logout) {
            new SessionManagemen(this).doLogout(this);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setDataParkir(ArrayList<Parkir> parkirs) {
        CustemRecyclerAdapter adapter = new CustemRecyclerAdapter<Parkir, ParkirHolder>(R.layout.layout_item_parkir, parkirs, Parkir.class, ParkirHolder.class) {
            @Override
            protected void bindView(ParkirHolder holder, final Parkir model, int position) {
                holder.jumlahParkiran.setText(model.getJumlah_slot());
                holder.parkiran.setText(model.getAlamat());
                holder.titleParkiran.setText(model.getNama().toUpperCase());

                holder.layoutParkiran.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, MapActivity.class);
                        intent.putExtra("data", model);
                        startActivity(intent);
                    }
                });
            }
        };

        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        API.GET(Config.getSaldo, this, new Fragment(), MainActivity.class.getName()+"saldo");

    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        EventBus.getDefault().register(this);
//    }

//    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
//    public void onEvent(Notification notification){
//        Log.d("data", notification.getData());
////        getActivity().finish();
//        ShowAlert(notification.getData());
//    }

    private void ShowAlert(String data) {

        Bundle bundle = new Bundle();
        bundle.putString("data", data);

        NotificationFragment fragment = new NotificationFragment();
        fragment.setArguments(bundle);
        fragment.setCancelable(false);
        fragment.show(getSupportFragmentManager(), "");
    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        EventBus.getDefault().unregister(toString());
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        EventBus.getDefault().unregister(this);
//    }
}
