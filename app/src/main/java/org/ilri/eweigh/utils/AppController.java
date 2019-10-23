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
        setLocale("sw");
    }

    public void setLocale(String localeName){
        Locale locale = new Locale(localeName);
        Locale.setDefault(locale);
        Resources res = getResources();
        Configuration config = res.getConfiguration();

        if (Build.VERSION.SDK_INT >= 21) {
            config.setLocale(locale);
            getApplicationContext().createConfigurationContext(config);

            Log.e(TAG, "Updating locale: " + localeName);
        }
        else {
            config.locale = locale;
            res.updateConfiguration(config, res.getDisplayMetrics());
        }
    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }
}

