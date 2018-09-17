package com.calak.jemmy.spiro.View.Notification;


import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.calak.jemmy.spiro.Model.Notification;
import com.calak.jemmy.spiro.R;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationFragment extends DialogFragment {

    @BindView(R.id.kode)
    TextView kode;
    @BindView(R.id.tipe)
    TextView tipe;
    @BindView(R.id.lokasi)
    TextView lokasi;
    @BindView(R.id.lamaParkir)
    TextView lama;
    @BindView(R.id.jamMasuk)
    TextView jamMasuk;
    @BindView(R.id.jamKeluar)
    TextView jamKeluar;
    @BindView(R.id.biaya)
    TextView biaya;

    @BindView(R.id.btClose)
    ImageView btClose;

    public NotificationFragment() {
        // Required empty public constructor
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //content
        RelativeLayout root = new RelativeLayout(getActivity());
        root.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        //create the fullscreen dialog
        Dialog dialog = new Dialog(getActivity());
        dialog.setCancelable(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(root);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        ButterKnife.bind(this, view);

        btClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        getData();
        return  view;
    }

    private void getData() {
        Bundle bundle = getArguments();
        String data= bundle.getString("data");
        Log.d("data", data);

        SetData(data);
    }

    private void SetData(String data) {
        try {
            JSONObject json = new JSONObject(data);
            kode.setText(": "+json.getString("kode_reservasi"));
            lokasi.setText(": "+json.getString("nama_lokasi"));
            lama.setText(": "+json.getString("lama_parkir"));
            jamMasuk.setText(": "+json.getString("waktu_masuk"));
            jamKeluar.setText(": "+json.getString("waktu_keluar"));
            biaya.setText(": "+json.getString("biaya_parkir"));
            tipe.setText(": "+json.getString("tipe_kendaraan"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
