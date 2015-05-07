package com.tcss450.moneyteam.geotracker.services;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.tcss450.moneyteam.geotracker.Database.LocationDBHelper;

import java.util.Calendar;

import static android.app.AlarmManager.RTC_WAKEUP;

/**
 * Handles running a background services for polling location data.
 * Created by Alex on 4/30/2015.
 */
public class LocationIntentService extends IntentService {
    //POLLING RATE CONSTANTS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    /** The rate at which location data is polled. (60 seconds ~60000).*/
    private static final int LOCATION_POLLING_INTERVAL_DEFAULT = 6000;
    /** The rate at which location data is polled. (60 seconds ~60000).*/
    private int adjustedPollingInterval;
    //FIELDS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
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

    /**
     * Handles the onStartCommand super call and logging.
     * @param intent the intent passed.
     * @param flags the flags given for onStartCommand.
     * @param startId the startId.
     * @return
     */
    @Override
    public int onStartCommand(final Intent intent, final int flags, final int startId) {
        super.onStartCommand(intent, flags, startId);
        Log.i(LOCATION_SERVICE_TAG, "onStartedCommand Called: #" + startId);
        return START_REDELIVER_INTENT;
    }


    @Override
    protected void onHandleIntent(final Intent intent) {
        Log.i(LOCATION_SERVICE_TAG, "Received an Intent: " + intent);
        //LOCATION DATA~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        LocationManager locationManager = (LocationManager) this.getSystemService(
                Context.LOCATION_SERVICE);

        //LOCATION LISTENER~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                Log.i(LOCATION_SERVICE_TAG, location.toString());
                //INSTANTIATE SQLITE DATABASE~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                //LocationDBHelper myDB = new LocationDBHelper();
            }

            //EMPTY METHOD~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            public void onStatusChanged(final String theProvider, final int theStatus, final Bundle theExtras) {}
            public void onProviderEnabled(final String theProvider) {}
            public void onProviderDisabled(final String theProvider) {}
        };

        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                0, 0, locationListener);
    }

    /**
     * Handles setting or disabling the alarm manager for this service.
     * @param context the application context.
     * @param isEnabled the boolean referencing if the alarm should be enabled or not.
     */
    public static void setServiceAlarm(final Context context, final boolean isEnabled) {
        Log.i(LOCATION_SERVICE_TAG, "setServiceAlarm Called: " + isEnabled);
        final Calendar calendar =  Calendar.getInstance();
        final AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        final Intent intent = new Intent(context, LocationIntentService.class);
        final PendingIntent alarmIntent = PendingIntent.getService(context, 0, intent, 0);
        //ENABLES ALARM TO CALL ON SERVICE
        if (isEnabled) {
            alarmManager.setRepeating(RTC_WAKEUP,
                    calendar.getTimeInMillis(),
                    LOCATION_POLLING_INTERVAL_DEFAULT,
                    alarmIntent);
            Log.i(LOCATION_SERVICE_TAG, "setRepeating Enabled");
        //ENABLES ALARM TO CALL ON SERVICE
        } else {
            alarmManager.cancel(alarmIntent);
            alarmIntent.cancel();
            Log.i(LOCATION_SERVICE_TAG, "setRepeating Disabled");
        }
    }
}
