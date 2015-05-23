package com.tcss450.moneyteam.geotracker.services;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import com.tcss450.moneyteam.geotracker.Database.LocationDBHelper;
import com.tcss450.moneyteam.geotracker.R;

import java.util.Calendar;

/**
 * Created by Alex on 4/30/2015.
 * Polls for user location in the background.
 */
public class LocationIntentService extends IntentService {

    /** The rate at which location data is polled. (6 seconds ~6000).*/
    public static final int LOCATION_POLLING_INTERVAL = 360000;
    /** Tag for the location service intent. */
    private static final String LOCATION_SERVICE_TAG = "LocationIntentService" ;
    /** Reference to the application context*/
    private static Context mContext;

    /** Notification mId for reference */
    //private int mId = 1;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public LocationIntentService() {
        super("IntentService");

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        LocationManager locationManager = (LocationManager) this.getSystemService(
                Context.LOCATION_SERVICE);
        LocationListener locationListener = new MyLocationListener();
        locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER,
                locationListener, Looper.myLooper());
        return START_REDELIVER_INTENT;
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(LOCATION_SERVICE_TAG, "Intent Called");
        LocationManager locationManager = (LocationManager) this.getSystemService(
                Context.LOCATION_SERVICE);
        LocationListener locationListener = new MyLocationListener();
        locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER,
                locationListener, Looper.myLooper());
    }

    //ALARM MANAGER~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public static void setServiceAlarm(final Context context, final boolean isEnabled,
                                       final int locationMinutes) {
        mContext = context;
        final Calendar calendar =  Calendar.getInstance();
        final AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        final Intent intent = new Intent(context, LocationIntentService.class);
        final PendingIntent alarmIntent = PendingIntent.getService(context, 0, intent, 0);

        //Convert locationInterval into milliseconds
        int customTimeInterval = locationMinutes * 60000;

        //Enable or disable alarm
        if (isEnabled) {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis(),
                    customTimeInterval,
                    alarmIntent);
            //WebPushIntent.setWebUploadAlarm();
            Log.i(LOCATION_SERVICE_TAG, "Background Location Tracking Enabled, Intervals: " + locationMinutes);
        } else {
            alarmManager.cancel(alarmIntent);
            alarmIntent.cancel();
            Log.i(LOCATION_SERVICE_TAG, "Background Location Tracking Disabled");
        }

    }

    /**
     * Listens for changes in location and adds points to the database accordingly.
     */
    private class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(final Location location) {
            Log.i(LOCATION_SERVICE_TAG, location.toString());
            LocationDBHelper myHelper = new LocationDBHelper(getApplicationContext());
            myHelper.addLocation(location, (System.currentTimeMillis() / 1000L));
        }
        @Override
        public void onProviderDisabled(String provider) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onProviderEnabled(String provider) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            // TODO Auto-generated method stub

        }

    }
}
