package com.redpepper.taxiapp.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkUtil {

    private Context context;

    public NetworkUtil(Context context) {
        this.context = context;
    }

    public boolean isDeviceConnectedToNetwork(){

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork  =cm.getActiveNetworkInfo();

        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();

    }
}
