package com.tcss450.moneyteam.geotracker.Utilities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.tcss450.moneyteam.geotracker.R;
import com.tcss450.moneyteam.geotracker.activities.MainActivity;
import com.tcss450.moneyteam.geotracker.services.LocationIntentService;

/**
 * Created by Alex on 4/30/2015.
 * Starts the tracking service when the system restarts.
 */
public class BootLoader extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        //Check that BOOT_COMPLETE intent was recieved
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED") && (context != null)) {
            SharedPreferences sp = context.getSharedPreferences(context.getString(R.string.user_info_main_key), Context.MODE_PRIVATE);
            //Get current location tracking toggle setting
            Boolean locationTrackBool = sp.getBoolean(context.getString(R.string.saved_location_toggle_boolean), false);
            int locationTimer = sp.getInt(context.getString(R.string.key_location_poll_timer), 0);

            if(locationTimer <= 0) {
                locationTimer = 1;
            }

            LocationIntentService.setServiceAlarm(context, locationTrackBool, locationTimer);
        }
    }
}
