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

import com.tcss450.moneyteam.geotracker.R;

import java.util.Calendar;

/**
 * Created by Alex_MAC on 5/19/15.
 */
public class WebPushIntent extends IntentService {

    
    public static final int WEBSERVICE_INTERVAL = 3600000;
    /** Tag for the location service intent. */
    private static final String WEB_SERVICE_TAG = "WebIntentService" ;
    /** Reference to the application context*/
    private static Context mContext;

    /** Notification mId for reference */
    //private int mId = 1;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public WebPushIntent() {
        super("WebService");

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return START_REDELIVER_INTENT;
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(WEB_SERVICE_TAG, "Intent Called");

        //Push all the information to the WebService.
    }

    //ALARM MANAGER~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public static void setWebUploadAlarm(final Context context, boolean isEnabled,
                                       final int minutesBetweenPushes) {
        mContext = context;
        SharedPreferences sp = context.getSharedPreferences(context.getString(R.string.user_info_main_key), Context.MODE_PRIVATE);
        final Calendar calendar =  Calendar.getInstance();
        final AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        final Intent intent = new Intent(context, WebPushIntent.class);
        final PendingIntent alarmIntent = PendingIntent.getService(context, 0, intent, 0);

        //Store the new webservice gap in SP if minutes > -2 (Means no change from previous)
        if(minutesBetweenPushes > -2) {
            SharedPreferences.Editor mEdit = sp.edit();
            mEdit.putInt(context.getString(R.string.key_location_upload_gap), minutesBetweenPushes).apply();
        }
        //Listen for charging Event, set alarm false.
        if(minutesBetweenPushes == -2) {
            //TODO finish this ish
            //sp.edit().put
            isEnabled = false;
        }

        //
        final int milliSecondsGap = minutesBetweenPushes * 60000;

        //Enable or disable alarm
        if (isEnabled) {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis(),
                    milliSecondsGap,
                    alarmIntent);
            Log.i(WEB_SERVICE_TAG, "SetRepeating Enabled with time intervals of: " + milliSecondsGap);
        } else {
            alarmManager.cancel(alarmIntent);
            alarmIntent.cancel();
            Log.i(WEB_SERVICE_TAG, "SetRepeating Disabled");
        }

    }

    
private static void parseTimeVal(int minutesBetweenPushes) {
    }}
