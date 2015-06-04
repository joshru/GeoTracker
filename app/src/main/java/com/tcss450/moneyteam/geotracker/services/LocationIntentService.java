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

import java.math.BigDecimal;
import java.util.Calendar;

/**
 * Polls for user location in the background.
 * @author Alexander Cherry
 */
public class LocationIntentService extends IntentService {

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
    protected void onHandleIntent(Intent intent) {
        Log.i(LOCATION_SERVICE_TAG, "Location Updated");
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
            SharedPreferences prefs = mContext.getSharedPreferences(
                    getString(R.string.shared_pref_key),
                    Context.MODE_PRIVATE);
            SharedPreferences.Editor eddy = prefs.edit();

            //get most recent location
            //avoid repeating multiple accesses.
            float prevLat         = prefs.getFloat(getString(R.string.previous_lat_key), -1);
            float  prevLong       = prefs.getFloat(getString(R.string.previous_long_key), -1);
            double newLat         = location.getLatitude(), newLong = location.getLongitude();

            //might be overkill...
            //Comparing floats to doubles can get sticky
            BigDecimal prevLatBD  = new BigDecimal(""+prevLat);
            BigDecimal prevLongBD = new BigDecimal(""+prevLong);
            BigDecimal newLatBD   = new BigDecimal(""+newLat);
            BigDecimal newLongBD  = new BigDecimal(""+newLong);

            Log.i(LOCATION_SERVICE_TAG, location.toString());
            if (!prevLatBD.equals(newLatBD) && !prevLongBD.equals(newLongBD)) {

                //It's simpler to store the lat and long than to store
                //the location object. This avoids having to write a parcelable
                //to shared preferences.
                eddy.putFloat(getString(R.string.previous_lat_key), (float) newLat);
                eddy.putFloat(getString(R.string.previous_long_key), (float) newLong);
                eddy.commit();
                LocationDBHelper myHelper = new LocationDBHelper(getApplicationContext());
                myHelper.addLocation(location, (System.currentTimeMillis() / 1000L));


            } else {
                Log.i(LOCATION_SERVICE_TAG, "Repeat location discarded.");
            }



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
