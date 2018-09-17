package com.calak.jemmy.spiro.View.map;


import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.calak.jemmy.spiro.Data.Local.SessionManagemen;
import com.calak.jemmy.spiro.Data.Online.API;
import com.calak.jemmy.spiro.Data.Online.Config;
import com.calak.jemmy.spiro.Model.Notification;
import com.calak.jemmy.spiro.Model.Parkir;
import com.calak.jemmy.spiro.Model.Tarif;
import com.calak.jemmy.spiro.R;
import com.calak.jemmy.spiro.Util.AlertManager;
import com.calak.jemmy.spiro.Util.DirectionsJSONParser;
import com.calak.jemmy.spiro.Util.ValidateData;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener, GoogleApiClient.ConnectionCallbacks, LocationListener {

    @BindView(R.id.reservasi)
    Button reservasi;
    @BindView(R.id.btBatal)
    Button btBatal;
    @BindView(R.id.namaPakiran)
    TextView namaParkiran;
    @BindView(R.id.jumlahSlot)
    TextView jumlahSlot;
    @BindView(R.id.harga)
    TextView harga;
    @BindView(R.id.timer)
    TextView timer;
    @BindView(R.id.switchKendaraan)
    Switch switchKendaraan;
    @BindView(R.id.platNomor)
    EditText platNomor;
    @BindView(R.id.layoutPlatnomor)
    RelativeLayout layoutPlatnomor;
    @BindView(R.id.layoutTimer)
    RelativeLayout layoutTimer;


    private GoogleApiClient googleApiClient;
    GoogleMap googleMap;

    private static final int overview = 0;

    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_map, container, false);
        ButterKnife.bind(this, view);

        initUI();
        initMap();
        getData();

        return  view;
    }

    Bundle bundle;
    Parkir parkir;
    int hargaParkirMobil =0;
    int hargaParkirMotor =0;
    String idTarifMobil, idTarifMotor;

    private void getData() {
        bundle = getArguments();
        parkir = (Parkir) bundle.getSerializable("data");

        setDataParkir();
    }

    private void setDataParkir() {
        namaParkiran.setText(parkir.getNama());
        jumlahSlot.setText(parkir.getJumlah_slot());

        ArrayList<Tarif> tarifs = parkir.getTarifs();
        for (int i=0; i< tarifs.size(); i++){
            if (tarifs.get(i).getTipe().equals("Mobil")){
                hargaParkirMobil = Integer.parseInt(tarifs.get(i).getTarif());
                idTarifMobil = tarifs.get(i).getId();
            }
            if(tarifs.get(i).getTipe().equals("Motor")){
                hargaParkirMotor = Integer.parseInt(tarifs.get(i).getTarif());
                idTarifMotor = tarifs.get(i).getId();
            }
        }
        setHarga();
    }

    private void setHarga() {

        if (mobil){
            harga.setText("1 JAM = Rp."+hargaParkirMobil);
        }else{
            harga.setText("1 JAM = Rp."+hargaParkirMotor);
        }
    }

    boolean mobil = true;

    private void initUI() {
        switchKendaraan.setChecked(true);

        reservasi.setOnClickListener(this);
        btBatal.setOnClickListener(this);

        switchKendaraan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (mobil){
                    switchKendaraan.setText("Motor");
                    mobil = false;
                    platNomor.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.ic_motor), null);
                }else{
                    switchKendaraan.setText("Mobil");
                    mobil = true;
                    platNomor.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.ic_car), null);

                }
                setHarga();
            }
        });
    }

    private void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        initGoogleApiClient();
    }

    private void initGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(getActivity())
//                .addApi(Places.GEO_DATA_API)
//                .addApi(Places.PLACE_DETECTION_API)
//                .enableAutoManage(getActivity(), this)
//                .build();

                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();
    }

    @Override
    public void onMapReady(GoogleMap map) {

        googleMap = map;
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        googleMap.setMyLocationEnabled(true);

    }

    ArrayList<LatLng> point= new ArrayList();
    MarkerOptions markerOptions;

    private void setMylocations(Location location) {
        LatLng newLat = new LatLng(location.getLatitude(), location.getLongitude());

        point.add(newLat);

        markerOptions = new MarkerOptions();
        markerOptions.position(newLat);
        markerOptions.title("You are here.");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));
        googleMap.addMarker(markerOptions);
//        googleMap.moveCamera(CameraUpdateFactory.newLatLng(newLat));
//        googleMap.animateCamera(CameraUpdateFactory.zoomTo(17));

        search(parkir.getNama());
    }

    private LocationRequest locationRequest;
    private Double currentLatitude, currentLongitude;

    public void getDetailLocation(Location location) {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(getActivity(), Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(currentLatitude, currentLongitude, 1);
            setMylocations(location);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static final int LOCATION_REQ = 101;
    private boolean checkPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQ);
            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQ);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (checkPermission()) {
            //get Current position when connected
            try {
                Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
                if (location == null) {
                    LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, (LocationListener) this);
                } else {
                    //If everything went fine lets get latitude and longitude
                    currentLatitude = location.getLatitude();
                    currentLongitude = location.getLongitude();
                }
                getDetailLocation(location);
            } catch (Exception e) {

            }
        }
    }

    LatLng locationDestinetion;
    public void search(String data) {
        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());

        try {
            List<Address> addressList = geocoder.getFromLocationName(data, 5);

            List<Address> addressList1 = geocoder.getFromLocation(addressList.get(0).getLatitude(), addressList.get(0).getLongitude(), 100);
            if (addressList1.size() > 0) {
                Double lat = (double) (addressList1.get(0).getLatitude());
                Double lon = (double) (addressList1.get(0).getLongitude());

                locationDestinetion = new LatLng(lat, lon);
                point.add(locationDestinetion);

                //setNew Marker
                markerOptions = new MarkerOptions();
                markerOptions.position(locationDestinetion);
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

                googleMap.addMarker(markerOptions);

                googleMap.moveCamera(CameraUpdateFactory.newLatLng(point.get(1)));
                googleMap.animateCamera(CameraUpdateFactory.zoomTo(12));

            }

            DirectionLocation();

        } catch (Exception e) {
            e.printStackTrace();
            Log.d("ErrorSearch", "<<===");
        }
    }

    private void DirectionLocation() {
        String url = getDirectionsUrl(point.get(0), point.get(1));
        DownloadTask downloadTask = new DownloadTask();

        // Start downloading json data from Google Directions API
        downloadTask.execute(url);

    }

    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";
        String mode = "mode=driving";

        String time = "departure_time=1380456000";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + mode +"&"+time;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;


        return url;
    }


    public void OnReservasiSuccess(String kode_reservasi) {
        kodeReservasi = kode_reservasi;
        ReservasiSuccessFragment fragment = new ReservasiSuccessFragment();
        fragment.setCancelable(false);
        fragment.show(getFragmentManager(), "");
    }

    private class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {

            String data = "";

            try {
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            parserTask.execute(result);

        }
    }

    String duration ="0";

    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList points = new ArrayList();
            PolylineOptions lineOptions = new PolylineOptions();
            MarkerOptions markerOptions = new MarkerOptions();

            Log.d("size result", result.size()+"");

            if(result.size()>0){
                for (int i = 0; i < result.size(); i++) {

                    List<HashMap<String, String>> path = result.get(i);

                    for (int j = 0; j < path.size(); j++) {
                        HashMap<String, String> point = path.get(j);

                        double lat = Double.parseDouble(point.get("lat"));
                        double lng = Double.parseDouble(point.get("lng"));
                        LatLng position = new LatLng(lat, lng);

                        String[] dur = point.get("duration").split(" ");
                        duration = dur[0];
                        points.add(position);
                    }

                    lineOptions.addAll(points);
                    lineOptions.width(12);
                    lineOptions.color(Color.RED);
                    lineOptions.geodesic(true);

                }

                if(points.size()!=0)googleMap.addPolyline(lineOptions);
                InitTimer();
            }else{
                DirectionLocation();
            }
        }
    }

    /**
     * A method to download json data from url
     */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.connect();

            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("ExceptionError", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case  R.id.reservasi:
                if(!(new ValidateData().isText(platNomor.getText().toString()))){
                    new AlertManager().AlertOk(getActivity(), "Plat nomor masih kosong", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) { }
                    });
                    return;
                }
                if(mobil){
                    if (!(new ValidateData().isBallance(Integer.parseInt(new SessionManagemen(getActivity()).getKeyBallance()), hargaParkirMobil))){
                        new AlertManager().AlertOk(getActivity(), "Saldo anda tidak cukup", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) { }
                        });
                        return;
                    }
                }else{
                    if (!(new ValidateData().isBallance(Integer.parseInt(new SessionManagemen(getActivity()).getKeyBallance()), hargaParkirMotor))){
                        new AlertManager().AlertOk(getActivity(), "Saldo anda tidak cukup", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) { }
                        });
                        return;
                    }
                }
                DoReservasi();
                break;
            case R.id.btBatal:
                new AlertManager().AlertOkNo(getActivity(), "Apakah anda yakin akan membatalkan reservasi ini?", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DoBatal();
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                break;
        }
    }

    public void DoBatalReservasi(String msg) {

        new AlertManager().AlertOk(getActivity(), msg, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        countDownTimer.cancel();
        layoutPlatnomor.setVisibility(View.VISIBLE);
        layoutTimer.setVisibility(View.GONE);

    }

    String kodeReservasi;
    private void DoBatal() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("kode_reservasi", kodeReservasi);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        API.POSTS(jsonObject, Config.batalreservasi, getActivity(), MapFragment.this, MapFragment.class.getName()+"cancel");
    }

    private void DoReservasi() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("nomor_plat", platNomor.getText().toString());
            jsonObject.put("id_lokasi", parkir.getId());
            if (mobil){
                jsonObject.put("id_tarif", idTarifMobil);
            }else{
                jsonObject.put("id_tarif", idTarifMotor);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        API.POSTS(jsonObject, Config.reservasi, getActivity(), MapFragment.this, MapFragment.class.getName());
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d("onConnectionSuspended", "<<---");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d("onConnectionFailed", "<<---");
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d("locationChange", location.getLatitude()+"----"+location.getLongitude());
        googleMap.clear();
        setMylocations(location);

        LatLng newLat = new LatLng(location.getLatitude(), location.getLongitude());
        point.add(newLat);
        point.add(locationDestinetion);
        DirectionLocation();
    }

    public CountDownTimer countDownTimer;
    public void InitTimer(){
        countDownTimer = new CountDownTimer(Integer.valueOf(duration) * 60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long millis = millisUntilFinished;
                //Convert milliseconds into hour,minute and seconds
                String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis), TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)), TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));

                timer.setText(""+hms);
            }

            @Override
            public void onFinish() {
                DoBatalReservasi("Reservasi di batalkan dikarenakan terlalu lama, harap coba lagi.");
            }
        };
    }

    public void StartCoundown(){
        layoutPlatnomor.setVisibility(View.GONE);
        layoutTimer.setVisibility(View.VISIBLE);
        countDownTimer.start();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onEvent(Notification notification){
        Log.d("data", notification.getData());
        getActivity().finish();
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }
    private static final int FINE_LOCATION_PERMISSION_REQUEST = 1;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case FINE_LOCATION_PERMISSION_REQUEST: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                }

            }
        }
    }
}
