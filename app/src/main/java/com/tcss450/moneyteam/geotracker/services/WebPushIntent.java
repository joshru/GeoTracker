package com.tcss450.moneyteam.geotracker.services;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Looper;
import android.util.Log;

import com.tcss450.moneyteam.geotracker.Database.LocationDBHelper;
import com.tcss450.moneyteam.geotracker.R;

import java.util.Calendar;

/**
 * Service that handles pushing the points to the server
 * at the user-defined interval.
 * @author Brandon Bell
 * @author Alexander Cherry
 * @author Joshua Rueschenberg
 */
public class WebPushIntent extends IntentService {

    /*Needs review, don't think we use this*/
    public static final int WEBSERVICE_INTERVAL = 3600000;
    /** Tag for the location service intent. */
    private static final String WEB_SERVICE_TAG = "WebIntentService" ;
    /** Reference to the application context*/
    private static Context mContext;



    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public WebPushIntent() {
        super("WebService");

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return START_REDELIVER_INTENT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(WEB_SERVICE_TAG, "Intent Called");
        LocationDBHelper db = new LocationDBHelper(mContext);
        db.pushPointsToServer();
    }

    //ALARM MANAGER~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Enables or disables the alarm for pushing points to the server.
     * @param context that this alarm affects.
     * @param isEnabled toggle
     * @param position  used to determine the interval
     */
    public static void setServerAlarm(final Context context, boolean isEnabled,
                                       final int position) {
        if(position == 4) {
            Log.i(WEB_SERVICE_TAG, "Intent Called");
            SharedPreferences sp = context.getSharedPreferences(context.getString(R.string.user_info_main_key), Context.MODE_PRIVATE);
            sp.edit().putBoolean(context.getString(R.string.saved_on_connected_bool), true).apply();
            return;
        }

        mContext = context;
        final Calendar calendar =  Calendar.getInstance();
        final AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        final Intent intent = new Intent(context, WebPushIntent.class);
        final PendingIntent alarmIntent = PendingIntent.getService(context, 0, intent, 0);

        final int msTimer = getPositionTime(position);

        //Enable or disable alarm
        if (isEnabled) {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis(),
                    msTimer,
                    alarmIntent);
            Log.i(WEB_SERVICE_TAG, "Server Updates enabled, Interval: " + msTimer + " Spinner Position: " + position);
        } else {
            alarmManager.cancel(alarmIntent);
            alarmIntent.cancel();
            Log.i(WEB_SERVICE_TAG, "Server Updates disabled.");
        }

    }

    /**
     * Translate a position into a number of hours.
     * @param position of the slider from account fragment
     * @return a number of hours
     */
    private static int getPositionTime(int position) {
        int hours = 1;
        switch (position) {
            case 0:
                hours = 1;
                break;
            case 1:
                hours = 2;
                break;
            case 2:
                hours = 12;
                break;
            case 3:
                hours = 24;
                break;
        }
        return hours * 60 * 60 * 1000;  /* Hours * (min/hour) * (sec/min) * (ms/sec) */
    }
}
