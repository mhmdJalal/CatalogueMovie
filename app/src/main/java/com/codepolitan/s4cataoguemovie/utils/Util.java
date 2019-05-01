package com.codepolitan.s4cataoguemovie.utils;

import android.annotation.SuppressLint;
import android.view.View;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Util {

    @SuppressLint("SimpleDateFormat")
    public static String parseDate(String date) throws ParseException {
        String inputFormat = "yyyy-MM-dd";
        String outputFormat = "EEE, dd MMM yyyy";

        Date firstDate = new SimpleDateFormat(inputFormat).parse(date);
        SimpleDateFormat format = new SimpleDateFormat(outputFormat);
        return format.format(firstDate);
    }

}
