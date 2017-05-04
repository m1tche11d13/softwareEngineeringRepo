package com.mitchell.daniel.bricktracker;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;

import android.widget.ImageView;

import java.io.InputStream;
import java.net.URL;


class DownloadImagesTask extends AsyncTask<Object, Void, Drawable> {

    private ImageView imageView = null;

    @Override
    protected Drawable doInBackground(Object... objects) {
        this.imageView = (ImageView)objects[0];
        return download_Image((String)objects[1]);
    }

    @Override
    protected void onPostExecute(Drawable result) {
        imageView.setImageDrawable(result);
    }

    private Drawable download_Image(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            return Drawable.createFromStream(is, "src name");
        } catch (Exception e) {
            return null;
        }
    }
}