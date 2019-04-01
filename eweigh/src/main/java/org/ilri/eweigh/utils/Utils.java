package org.ilri.eweigh.utils;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;
import org.ilri.eweigh.R;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Utils {

    /**
     *
     * Capitalize all words in a sentence
     *
     * i am joe = I Am Joe
     *
     * */
    public static String capitalizeAll(String string){
        String[] words = string.split("\\s+");
        String newStr = "";

        for (String word: words) {
            newStr = String.format("%s %s", newStr, StringUtils.capitalize(word.toLowerCase()));
        }

        return newStr.trim();
    }

    public static String formatNumber(double number){
        DecimalFormat formatter = new DecimalFormat("#,###,###.##");
        return formatter.format(number);
    }

    public static boolean isNetworkAvailable(Context context){
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static String getCurrentDate(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.ENGLISH);

        return df.format(c.getTime());
    }

    public static ProgressDialog getProgressDialog(Context context, String message, boolean cancellable){
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage(message);
        dialog.setCancelable(cancellable);

        return dialog;
    }

    public static DatePickerDialog getDatePickerDialog(Context context, DatePickerDialog.OnDateSetListener listener){
        Calendar now = Calendar.getInstance();

        int year = now.get(Calendar.YEAR);
        int month = now.get(Calendar.MONTH);
        int day = now.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(context,
                android.R.style.Theme_Holo_Dialog, listener, year, month, day);
    }

    public static AlertDialog getSimpleDialog(Context context, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message);
        builder.setNeutralButton("Close", null);

        return builder.create();
    }

    public static AlertDialog getSimpleDialog(Context context, String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setNeutralButton("Close", null);

        return builder.create();
    }

    public static AlertDialog getCustomDialog(Context context, View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(view);

        return builder.create();
    }

    public static String getPaymentMode(int paymentId){
        String paymentMode = "";

        switch (paymentId) {

            case 1:
                paymentMode = "Cash";
                break;

            case 2:
                paymentMode = "M-Pesa";
                break;

            case 3:
                paymentMode = "Bank";
                break;
        }

        return paymentMode;
    }

    public static View getBlankState(Context context, String message){
        View blankState = LayoutInflater.from(context).inflate(R.layout.blank_state, null);
        TextView blankText = blankState.findViewById(R.id.txt_blank_state);

        blankText.setText(message);

        return blankState;
    }
}
