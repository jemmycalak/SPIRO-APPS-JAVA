package com.calak.jemmy.spiro.Util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

public class FormatMoney {
    public static String formatNumber(double ballance){
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.US);
        DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
        symbols.setCurrencySymbol(""); // Don't use null.
        formatter.setDecimalFormatSymbols(symbols);
        String value = formatter.format(ballance);
        return value;
    }
}
