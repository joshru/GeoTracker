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
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

/**
 * Created by Alex on 4/30/2015.
 */
public class LocationIntentService extends IntentService {

    /** The rate at which location data is polled. (60 seconds).*/
    private static final int LOCATION_POLLING_INTERVAL = 6000;
    /** Tag for the location service intent. */
    private static final String LOCATION_SERVICE_TAG = "LocationIntentService" ;
    /** Notification mId for reference */
    private int mId = 22;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     * @param name Used to name the worker thread, important only for debugging.
     */
    public LocationIntentService(String name) {
        super(name);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Log.i(LOCATION_SERVICE_TAG, "service starting");
        return START_REDELIVER_INTENT;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(LOCATION_SERVICE_TAG, "Received an Intent: " + intent);

        //Set up location manager
        //Check for recent data from external sources, if older then 60 seconds, get your own.

        generateNotification(); //TODO Eventually pass the pulled location data.

    }

    //TODO add setInexactRepearing and use AlarmManager.REAL_TIME
    public static void setServiceAlarm(Context context, boolean isOn) {
        Intent i = new Intent(context, LocationIntentService.class);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, i, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        if (isOn) {
            alarmManager.setRepeating(AlarmManager.RTC, System.currentTimeMillis()
                    , LOCATION_POLLING_INTERVAL, pendingIntent);
        } else {
            alarmManager.cancel(pendingIntent);
            pendingIntent.cancel();
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void generateNotification() {
        final NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(android.R.drawable.bottom_bar)
                        .setContentTitle("Location Service")
                        .setContentText("Location Updated!");
        final Intent resultIntent = new Intent(this, com.tcss450.moneyteam.geotracker.activities.MainActivity.class);
        final TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);

        stackBuilder.addParentStack(com.tcss450.moneyteam.geotracker.activities.LoginActivity.class);
        stackBuilder.addNextIntent(resultIntent);

        final PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        final NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(mId, mBuilder.build());
    }

}
