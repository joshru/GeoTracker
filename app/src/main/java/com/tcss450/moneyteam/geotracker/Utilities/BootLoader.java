package com.tcss450.moneyteam.geotracker.Utilities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.tcss450.moneyteam.geotracker.R;
import com.tcss450.moneyteam.geotracker.activities.MainActivity;
import com.tcss450.moneyteam.geotracker.services.LocationIntentService;

/**
 * Broadcast Reciever that strictly handles boot completion functionality.
 * Created by Alex on 4/30/2015.
 */
public class BootLoader extends BroadcastReceiver {

    /**
     * Listens for boot completion to restart device services.
     * @param context the application context.
     * @param intent the application intent.
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            SharedPreferences sp = context.getSharedPreferences(context.getString(R.string.user_info_main_key), Context.MODE_PRIVATE);
            //SET SERVICE ALARM BASED ON PREFERENCES~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            LocationIntentService.setServiceAlarm(context, sp.getBoolean(context.getString(R.string.saved_location_toggle_boolean), false));
        }
    }
}
