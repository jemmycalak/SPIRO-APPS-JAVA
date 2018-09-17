package com.calak.jemmy.spiro.Util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.calak.jemmy.spiro.R;

public class AlertManager {
    public static void AlertOk(Context context, String title, DialogInterface.OnClickListener OK){
        new AlertDialog.Builder(context, R.style.AlertDialogCustom)
                .setMessage(title)
                .setCancelable(false)
                .setPositiveButton("Ya", OK)
                .create()
                .show();
    }
    public void AlertOkNo(Context context, String title, DialogInterface.OnClickListener OK, DialogInterface.OnClickListener NO){
        new AlertDialog.Builder(context, R.style.AlertDialogCustom)
                .setMessage(title)
                .setCancelable(false)
                .setPositiveButton("Ya", OK)
                .setNegativeButton("Tidak", NO)
                .create()
                .show();
    }

    public static void ShowEmptyData(Activity activity) {
        AlertOk(activity, "Maaf data belum tersedia", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
    }
}
