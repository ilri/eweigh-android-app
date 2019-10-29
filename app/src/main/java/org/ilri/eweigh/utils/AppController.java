package org.ilri.eweigh.utils;

import android.app.Application;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;

import com.marcinorlowski.fonty.Fonty;

import java.util.Locale;

public class AppController extends Application {
    public static final String TAG = AppController.class.getSimpleName();

    private static AppController mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        // Set custom fonts
        Fonty.context(this)
            .normalTypeface("Lato-Regular.ttf")
            .boldTypeface("Lato-Bold.ttf")
            .italicTypeface("Lato-Italic.ttf")
            .done();

        // Update locale
        Utils.setLocale(this, "sw");
    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }
}

