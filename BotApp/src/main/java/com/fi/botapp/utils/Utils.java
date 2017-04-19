package com.fi.botapp.utils;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Utils {
    private static ProgressDialog mProgressDialog;

    public static void showShortToast(Context context, String toastMsg){
        Toast.makeText(context, toastMsg, Toast.LENGTH_SHORT).show();
    }

    public static void showLongToast(Context context, String toastMsg){
        Toast.makeText(context, toastMsg, Toast.LENGTH_LONG).show();
    }

    public static void hideKeyboard(Context context, View field) {
        final InputMethodManager imm = (InputMethodManager) context.getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(field.getWindowToken(), 0);
    }

    public static boolean checkForInternet(Context ctx) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        boolean isAvailable = activeNetworkInfo != null && activeNetworkInfo.isConnected();
        if (!isAvailable) {
            showShortToast(ctx, "Please connect to the Internet !");
        }
        return isAvailable;
    }

    public static void showDialog(Context ctx, Dialog.OnKeyListener listener) {
        final Dialog dialog = new Dialog(ctx);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setOnKeyListener(listener);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }


    public static void showProgressDialog(Context ctx) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(ctx);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.show();
        }
    }

    public static void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    public static String getDateTime(long timestamp) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(timestamp);
        DateFormat dateFormat = new SimpleDateFormat("\"dd-MM-yyyy", Locale.US);
        final String date = dateFormat.format(timestamp);
        DateFormat hoursFormat = new SimpleDateFormat("HH:mm", Locale.US);
        final String time = hoursFormat.format(timestamp);
        return date + ", " + time;
    }
}
