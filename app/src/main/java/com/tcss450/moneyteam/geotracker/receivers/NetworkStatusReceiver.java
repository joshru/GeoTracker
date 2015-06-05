package com.tcss450.moneyteam.geotracker.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.tcss450.moneyteam.geotracker.R;
import com.tcss450.moneyteam.geotracker.services.LocationIntentService;
import com.tcss450.moneyteam.geotracker.services.WebPushIntent;

/**
 * Receiver that listens for changes in network connectivity.
 * Will shut off
 * @author Brandon Bell
 */
public class NetworkStatusReceiver extends BroadcastReceiver {

    /**
     * Handles network status changed handler.
     * @param context application context
     * @param intent  intent with info about the network.
     */
    @Override
    public void onReceive(Context context, Intent intent) {

        SharedPreferences sp = context.getSharedPreferences(context
                .getString(R.string.user_info_main_key), Context.MODE_PRIVATE);
        Boolean locationTrackBool = sp.getBoolean(context.getString(R.string.saved_location_toggle_boolean), false);
        int locationTimer = sp.getInt(context.getString(R.string.key_location_poll_timer), 1);
        int spinnerPos = sp.getInt(context.getString(R.string.saved_spinner_position), 0);

        Log.i("ANDROID INTENT", "NetworkStatusReceiver intent received, "
                + intent + " Context: " + context);
        String action = intent.getAction();

        /*This is the only action this broadcaster is listening for, but
        * included this condition for clarity.
        */
        if (action.equalsIgnoreCase(ConnectivityManager.CONNECTIVITY_ACTION)) {
            //Get network info
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            boolean isConnected = networkInfo != null && networkInfo.isConnectedOrConnecting();

            if (!isConnected) {
                //stop
                LocationIntentService.setServiceAlarm(context, false, locationTimer);
                WebPushIntent.setServerAlarm(context, false, 0);

                Log.i("ANDROID INTENT", "Connectivity lost, shutting down services.");

            }
            else {
                //start back up
                locationTimer = (locationTimer <= 0) ? (1) : (locationTimer);
                Log.i("ANDROID INTENT", "Location Bool: " + locationTrackBool
                + " Timer: " + locationTimer + " {position: " + spinnerPos);
                LocationIntentService.setServiceAlarm(context, locationTrackBool, locationTimer);
                WebPushIntent.setServerAlarm(context, locationTrackBool, spinnerPos);
                Log.i("ANDROID INTENT", "Connection regained, services restored to previous state.");
            }



        }



    }
}
