package org.ilri.eweigh.utils;

import android.app.Application;

import com.marcinorlowski.fonty.Fonty;

public class AppController extends Application {
    public static final String TAG = AppController.class.getSimpleName();

    private static AppController mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        // Set custom fonts
        Fonty.context(this)
            .normalTypeface("Roboto-Light.ttf")
            .boldTypeface("Roboto-Bold.ttf")
            .italicTypeface("Roboto-LightItalic.ttf")
            .done();
    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }
}

