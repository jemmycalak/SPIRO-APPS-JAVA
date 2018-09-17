package com.calak.jemmy.spiro.Util;

import android.app.Activity;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;

public class ValidateData {
    public Boolean isText(String text){
        if(TextUtils.isEmpty(text)){
            return false;
        }
        return true;
    }

    public Boolean isBallance(int ballance, int harga){
        if (!(ballance >= harga)){
            return false;
        }
        return true;
    }
}
