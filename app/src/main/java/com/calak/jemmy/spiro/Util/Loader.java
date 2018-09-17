package com.calak.jemmy.spiro.Util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;

import com.calak.jemmy.spiro.R;

public class Loader {

    private ProgressDialog progressDialog;

    public void startLoading(Activity context) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(context);
            progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            progressDialog.setCancelable(false);
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            progressDialog.setIndeterminate(false);
        }
        progressDialog.show();
        progressDialog.setContentView(R.layout.layout_cusprogresbar);
    }

    public void stopLoading() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}
