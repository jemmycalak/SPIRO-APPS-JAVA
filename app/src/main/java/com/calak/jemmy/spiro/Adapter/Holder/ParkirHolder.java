package com.calak.jemmy.spiro.Adapter.Holder;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.calak.jemmy.spiro.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ParkirHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.titleParkiran)
    public TextView titleParkiran;
    @BindView(R.id.parkiran)
    public TextView parkiran;
    @BindView(R.id.jumlahParkir)
    public TextView jumlahParkiran;
    @BindView(R.id.layoutParkiran)
    public CardView layoutParkiran;

    public ParkirHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
