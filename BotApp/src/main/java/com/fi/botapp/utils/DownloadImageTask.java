package com.fi.botapp.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.io.InputStream;

public class DownloadImageTask extends AsyncTask<Void, Void, Bitmap> {
    ImageView bmImage;
    ProgressBar loader;
    String url;

    public DownloadImageTask(ImageView bmImage, ProgressBar loader, String imageUrl) {
        this.bmImage = bmImage;
        this.loader = loader;
        this.url = imageUrl;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        loader.setVisibility((!TextUtils.isEmpty(this.url))?View.VISIBLE:View.GONE);
        bmImage.setVisibility(View.GONE);
    }

    @Override
    protected Bitmap doInBackground(Void... params) {
        Bitmap icon = null;
        if (!TextUtils.isEmpty(this.url)) {
            try {
                InputStream in = new java.net.URL(this.url).openStream();
                icon = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Logger.D("Error", e.getMessage());
                e.printStackTrace();
            }
        } else {
            Logger.D("urldisplay empty");
        }
        return icon;
    }

    protected void onPostExecute(Bitmap result) {
        loader.setVisibility(View.GONE);
        if (result != null) {
            bmImage.setVisibility(View.VISIBLE);
            bmImage.setImageBitmap(result);
        } else {
            bmImage.setVisibility(View.GONE);
        }
    }
}
