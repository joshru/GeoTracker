package com.tcss450.moneyteam.geotracker.Utilities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.tcss450.moneyteam.geotracker.services.LocationIntentService;

/**
 * Created by Alex on 4/30/2015.
 */
public class BootLoader extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            // Set the alarm here.
            LocationIntentService.setServiceAlarm(context, true);
        }
    }
}
