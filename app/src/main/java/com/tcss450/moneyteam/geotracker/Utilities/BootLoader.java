package com.tcss450.moneyteam.geotracker.Utilities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.tcss450.moneyteam.geotracker.Database.LocationDBHelper;
import com.tcss450.moneyteam.geotracker.R;
import com.tcss450.moneyteam.geotracker.activities.MainActivity;
import com.tcss450.moneyteam.geotracker.services.LocationIntentService;
import com.tcss450.moneyteam.geotracker.services.WebPushIntent;

/**
 * Created by Alex on 4/30/2015.
 * Starts the tracking service when the system restarts.
 */
public class BootLoader extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if(context != null) {

            SharedPreferences sp = context.getSharedPreferences(context.getString(R.string.user_info_main_key), Context.MODE_PRIVATE);
            Boolean locationTrackBool = sp.getBoolean(context.getString(R.string.saved_location_toggle_boolean), false);
            int locationTimer = sp.getInt(context.getString(R.string.key_location_poll_timer), 0);
            int spinnerPos = sp.getInt(context.getString(R.string.saved_spinner_position), 0);
            boolean onConnected = sp.getBoolean(context.getString(R.string.saved_on_connected_bool), false);

            //Handles BOOT_COMPLETED intent
            if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
                locationTimer = (locationTimer <= 0) ? (1):(locationTimer);
                LocationIntentService.setServiceAlarm(context, locationTrackBool, locationTimer);
                WebPushIntent.setServerAlarm(context, locationTrackBool, spinnerPos);
            }

            //Handles BATTERY_LOW intent
            if (intent.getAction().equals("android.intent.action.BATTERY_LOW")) {
                /* Force stop all network services. */
                LocationIntentService.setServiceAlarm(context, false, locationTimer);
                WebPushIntent.setServerAlarm(context, false, 0);
            }

            //Handles ACTION_POWER_CONNECTED intent
            if (intent.getAction().equals("android.intent.action.ACTION_POWER_CONNECTED") && onConnected) {
                Log.i("POWER CONNECTED", "Points have been pushed to the server");
                LocationDBHelper db = new LocationDBHelper(context);
                db.pushPointsToServer();
            }
        }
    }
}
