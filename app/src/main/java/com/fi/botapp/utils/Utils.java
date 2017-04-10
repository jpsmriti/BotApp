package com.fi.botapp.utils;

import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

public class Utils {

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

    public static void checkForInternet(Context ctx) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        boolean isAvailable = activeNetworkInfo != null && activeNetworkInfo.isConnected();
        if (!isAvailable) {
            showShortToast(ctx, "Please connect to the Internet !");
        }
    }

    public static void showDialog(Context ctx, Dialog.OnKeyListener listener) {
        final Dialog dialog = new Dialog(ctx);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setOnKeyListener(listener);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

}
