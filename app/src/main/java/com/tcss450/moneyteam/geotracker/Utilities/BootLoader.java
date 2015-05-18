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
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            MainActivity myClass = new MainActivity();
            //SharedPreferences sp = myClass.getSharedPreferences(myClass.getString(R.string.user_info_main_key), Context.MODE_PRIVATE);
            //LocationIntentService.setServiceAlarm(context, sp.getBoolean(myClass.getString(R.string.saved_location_toggle_boolean), false));
        }
    }
}
