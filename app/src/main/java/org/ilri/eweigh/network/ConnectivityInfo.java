package org.ilri.eweigh.network;

import android.content.Context;
import android.net.ConnectivityManager;

public class ConnectivityInfo {

    private Context context;

    public static final String MOBILE_DATA = "MOBILE_DATA";
    public static final String WIFI = "WIFI";
    public static final String NO_INTERNET = "NO_INTERNET";

    public ConnectivityInfo(Context context) {
        this.context = context;
    }

    public String getConnectionType(Context context) {
        String networkStatus = "";

        final ConnectivityManager manager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final android.net.NetworkInfo networkType = manager.getActiveNetworkInfo();

        if (networkType != null) {
            if (!networkType.isAvailable()) {
                // No active connection found
                return NO_INTERNET;
            } else {
                switch (networkType.getType()) {

                    case ConnectivityManager.TYPE_WIFI:
                        networkStatus = WIFI;
                        break;

                    case ConnectivityManager.TYPE_MOBILE:
                        networkStatus = MOBILE_DATA;
                        break;

                    default:
                        break;
                }

                return networkStatus;
            }
        } else {
            return NO_INTERNET;
        }
    }

    public boolean hasInternetConnectivity() {
        return !getConnectionType(context).equals(NO_INTERNET);
    }
}
