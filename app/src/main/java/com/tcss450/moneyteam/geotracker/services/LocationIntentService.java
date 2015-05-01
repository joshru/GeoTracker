package com.tcss450.moneyteam.geotracker.services;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.Calendar;

/**
 * Created by Alex on 4/30/2015.
 */
public class LocationIntentService extends IntentService {

    /** The rate at which location data is polled. (60 seconds ~6000).*/
    private static final int LOCATION_POLLING_INTERVAL = 6000;
    /** Tag for the location service intent. */
    private static final String LOCATION_SERVICE_TAG = "LocationIntentService" ;

    /** Notification mId for reference */
    private int mId = 1;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public LocationIntentService() {
        super("Location Service");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Log.i(LOCATION_SERVICE_TAG, "service starting");
        //Set up location manager
        //Check for recent data from external sources, if older then 60 seconds, get your own.
        return START_REDELIVER_INTENT;
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(LOCATION_SERVICE_TAG, "Received an Intent: " + intent);
    }

    //TODO add setInexactRepearing and use AlarmManager.REAL_TIME
    public static void setServiceAlarm(Context context, boolean isEnabled) {
        Log.i(LOCATION_SERVICE_TAG, "Location Service Enabled");
        final Calendar calendar =  Calendar.getInstance();
        final AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        final Intent intent = new Intent(context, LocationIntentService.class);
        final PendingIntent alarmIntent = PendingIntent.getService(context, 0, intent, 0);

        if (isEnabled) {
            alarmManager.setRepeating(AlarmManager.RTC,
                    calendar.getTimeInMillis(),
                    LOCATION_POLLING_INTERVAL,
                    alarmIntent);
            Log.i(LOCATION_SERVICE_TAG, "SetRepeating Enabled");
        } else {
            alarmManager.cancel(alarmIntent);
            alarmIntent.cancel();
        }
    }
}
