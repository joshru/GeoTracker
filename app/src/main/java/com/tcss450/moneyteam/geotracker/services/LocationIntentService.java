package com.tcss450.moneyteam.geotracker.services;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.tcss450.moneyteam.geotracker.Database.LocationDBHelper;

import java.util.Calendar;

/**
 * Created by Alex on 4/30/2015.
 */
public class LocationIntentService extends Service {

    /** The rate at which location data is polled. (60 seconds ~6000).*/
    private static final int LOCATION_POLLING_INTERVAL = 10000;
    /** Tag for the location service intent. */
    private static final String LOCATION_SERVICE_TAG = "LocationIntentService" ;
    private static Context mContext;

    /** Notification mId for reference */
    private int mId = 1;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public LocationIntentService() {
        super();
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
    public IBinder onBind(Intent intent) {
        //PULL LOCATION DATA AND SETUP MANAGER~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        return null;
    }

    //ALARM MANAGER~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    public static void setServiceAlarm(Context context, boolean isEnabled) {
        mContext = context;
        final Calendar calendar =  Calendar.getInstance();
        final AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        final Intent intent = new Intent(context, LocationIntentService.class);
        final PendingIntent alarmIntent = PendingIntent.getService(context, 0, intent, 0);

        if (isEnabled) {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis(),
                    LOCATION_POLLING_INTERVAL,
                    alarmIntent);
            Log.i(LOCATION_SERVICE_TAG, "SetRepeating Enabled");
        } else {
            alarmManager.cancel(alarmIntent);
            alarmIntent.cancel();
            Log.i(LOCATION_SERVICE_TAG, "SetRepeating Disabled");
        }
    }

    private class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(final Location location) {
            Log.i(LOCATION_SERVICE_TAG, location.toString());
            LocationDBHelper myHelper = new LocationDBHelper(mContext);
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
