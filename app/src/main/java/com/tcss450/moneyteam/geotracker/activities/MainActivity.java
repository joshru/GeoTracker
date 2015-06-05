package com.tcss450.moneyteam.geotracker.activities;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.SeekBar;

import com.tcss450.moneyteam.geotracker.Database.LocationDBHelper;
import com.tcss450.moneyteam.geotracker.R;
import com.tcss450.moneyteam.geotracker.Utilities.Poptart;
import com.tcss450.moneyteam.geotracker.Utilities.WebServiceHelper;
import com.tcss450.moneyteam.geotracker.fragments.PipTabListener;
import com.tcss450.moneyteam.geotracker.fragments.AccountFragment;
import com.tcss450.moneyteam.geotracker.fragments.MapFragment;
import com.tcss450.moneyteam.geotracker.fragments.TrackingFragment;
import com.tcss450.moneyteam.geotracker.interfaces.TabInterface;
import com.tcss450.moneyteam.geotracker.services.LocationIntentService;
import com.tcss450.moneyteam.geotracker.services.WebPushIntent;

import java.util.ArrayList;
import java.util.Calendar;

import de.greenrobot.event.EventBus;

/**
 * The Main Activity class. Account and location recording data will be displayed to the user here.
 * @author Brandon Bell
 * @author Alexander Cherry
 * @author Joshua Rueschenberg
 */
public class MainActivity extends Activity implements TabInterface {

    public static final String MAINACTIVITY_TEST_TAG = "MAINACTIVITY.JUNIT.TAG";

    /** String used for logcat debugging*/
    private static final String DEBUG_MAIN = "DEBUG MAIN";
    private static final String SAVE = "SAVE";
    private static final String LOAD = "LOAD";

    /** The account setting tab*/
    private Tab mAccountSettingsTab;

    /** The account settings fragment to be displayed via Account Settings Tab*/
    private AccountFragment mAccountSettingsFragment;

    /** The location tracking tab*/
    private Tab mTrackingTab;

    /** The tracking information fragment to be displayed via Tracking Tab*/
    private TrackingFragment mTrackingFragment;

    /** The Google maps tab*/
    private Tab mMapTab;

    /** Google maps with location data will be displayed to user via Map Tab*/
    private Fragment mMapFragment;

    /** Collection of location objects*/
    private ArrayList<Location> mQueryLocations;

    private int[] mUserRangeStart;

    private int[] mUserRangeEnd;

    /** The gesture detector object*/
    private GestureDetectorCompat mDetector;
    private String mUserEmail;
    private boolean mLocationBool;
    private int mLocationTimer;
    private int mSpinnerPos;
    private boolean mLoginBool;
    private SharedPreferences myPreferences;

    /**
     * Instantiates all fields for the Main Activity, populates tab layout, and sets listeners.
     * @param savedInstanceState
     */
    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //SUPER CALL~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        super.onCreate(savedInstanceState);
        //THIS CALL~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        this.setContentView(R.layout.activity_main_tab);

        //Get Shared pref. reference
        myPreferences = getSharedPreferences(getString(R.string.user_info_main_key), Context.MODE_PRIVATE);

        loadSharedPreferences();
        setLoginStatus("main");

        //CREATE FRAGMENTS (TABS)~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        mAccountSettingsFragment = new AccountFragment();
        mTrackingFragment = new TrackingFragment();
        mMapFragment = new MapFragment();

        //GETS ALL FRAGMENT USED SHAREDPREFERENCES ON STARTUP~~~~~~~~~~~~~~~~~~~~~~~
        Log.i("STARTUP", "MainActivity onCreate: " + mSpinnerPos);
        LocationIntentService.setServiceAlarm(this, mLocationBool, mLocationTimer);
        WebPushIntent.setServerAlarm(this, mLocationBool, mSpinnerPos);
        //WebPushIntent. setWebUploadAlarm(rootView.getContext(), true, serviceGap);

        //GET ACTION BAR AND ADJUST SETTINGS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Logout");
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        //ACCOUNT SETTINGS TAB~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        mAccountSettingsTab = actionBar.newTab();
        mAccountSettingsTab.setIcon(getResources().getDrawable(R.drawable.pip_gains));
        mAccountSettingsTab.setText("Account Settings");
        mAccountSettingsTab.setTabListener(new PipTabListener(mAccountSettingsFragment));
        actionBar.addTab(mAccountSettingsTab);

        //LOCATION TRACKING SETTINGS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        mTrackingTab = actionBar.newTab();
        mTrackingTab.setIcon(getResources().getDrawable(R.drawable.pip_spy));
        mTrackingTab.setText("Location Tracking Settings");
        mTrackingTab.setTabListener(new PipTabListener(mTrackingFragment));
        actionBar.addTab(mTrackingTab);
        mUserRangeStart = new int[5];
        mUserRangeEnd = new int[5];
        fillStartEnd();

        //MAP TAB~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        mMapTab = actionBar.newTab();
        mMapTab.setIcon(getResources().getDrawable(R.drawable.pip_map));
        mMapTab.setText("Map");
        mMapTab.setTabListener(new PipTabListener(mMapFragment));
        actionBar.addTab(mMapTab);

    }

    private void loadSharedPreferences() {
        Log.i("LOAD", "Preferences Loaded in Main");
        SharedPreferences myPreferences = getSharedPreferences(getString(R.string.user_info_main_key), Context.MODE_PRIVATE);
        mUserEmail = myPreferences.getString(getString(R.string.saved_email_key), "");
        mLocationBool = myPreferences.getBoolean(getString(R.string.saved_location_toggle_boolean), false);
        mLocationTimer = myPreferences.getInt(getString(R.string.key_location_poll_timer), 1);
        mSpinnerPos = myPreferences.getInt(getString(R.string.saved_spinner_position), 4);
        mLoginBool = true;
        setLoginBool(true);

        Log.i(LOAD, "Email: " + mUserEmail +
                " LocationBool: " + mLocationBool +
                " LocationTimer: " + mLocationTimer +
                " Spinner Position: " + mSpinnerPos +
                " Logged In: " + mLoginBool);

    }

    private void savePreferences() {
        Log.i("SAVE", "Preferences Saved in Main");
        SharedPreferences.Editor edit = myPreferences.edit();
        edit.putString(getString(R.string.saved_email_key), mUserEmail);
        edit.putBoolean(getString(R.string.saved_location_toggle_boolean), mLocationBool);
        edit.putInt(getString(R.string.key_location_poll_timer), mLocationTimer);
        edit.putInt(getString(R.string.saved_spinner_position), mSpinnerPos);
        edit.putBoolean(getString(R.string.logged_in_boolean), mLoginBool);

        edit.apply();

        Log.i(SAVE, "Email: " + mUserEmail +
                " LocationBool: " + mLocationBool +
                " LocationTimer: " + mLocationTimer +
                " Spinner Position: " + mSpinnerPos +
                " Logged In: " + mLoginBool);
    }

    /**
     * Calls super onSaveInstanceState
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    /**
     * Calls super onRestoreInstanceState
     * @param savedInstanceState
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    /**
     * Overides the return to home as a call to logout the user.
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //GET SHARED PREFERERENCES/SET LOGIN IN BOOL~~~~~~~~~~~~~~~~~~~~~~~~
                setLocationBool(false);
                setLoginBool(false);

                LocationIntentService.setServiceAlarm(this, false, 1);
                WebPushIntent.setServerAlarm(this, false, mSpinnerPos);

                //PUSH ENTRIES TO SERVER~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                LocationDBHelper helper = new LocationDBHelper(this);
                helper.pushPointsToServer();

                //LAUNCH MAIN~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                Intent loginScreen = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(loginScreen);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                setLoginStatus("login");
                this.finish();
                return(true);
            default:
                return(super.onOptionsItemSelected(item));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("ONSTOP", "onStop called in mainActivity");
        EventBus.getDefault().unregister(this);
        savePreferences();
    }

    /**
     * Event handler for Event bus. Gets fired locations and adds them to the list view
     * @param event
     */
    public void onEvent(WebServiceHelper.LocationEvent event) {
        Poptart.displayCustomDuration(this, event.mEventMessage, 3);

        if (event.mSuccess) {
            mQueryLocations = event.mLocations;
            mTrackingFragment.setListAdapter(mQueryLocations);
            Log.i(DEBUG_MAIN, mQueryLocations.toString());
        }
    }

    @Override
    public void setLocations(ArrayList<Location> theLocations) {
        mQueryLocations = theLocations;
    }

    @Override
    public ArrayList<Location> getLocations() {

        Log.i(DEBUG_MAIN, "Getter in main called");
        if(mQueryLocations != null && !mQueryLocations.isEmpty()) {
            return mQueryLocations;
        }
        return null;
    }

    @Override
    public String getUserEmail() {
        return mUserEmail;
    }

    @Override
    public int getLocationTimer() {
        if(mLocationTimer > 0) {
            return mLocationTimer;
        }
        return 1;
    }

    @Override
    public void setLocationTimer(int minutes) {
        if(minutes > 0) {
            mLocationTimer = minutes;

            if(mLocationBool) {
                LocationIntentService.setServiceAlarm(this, mLocationBool, mLocationTimer);
            }
        }
    }

    @Override
    public boolean getLocationBool() {
        return mLocationBool;
    }

    @Override
    public void setLocationBool(boolean toggleEnabled) {
        Log.d(DEBUG_MAIN, "setLocationBool called, spinner pos: " + mSpinnerPos);
        mLocationBool = toggleEnabled;
        LocationIntentService.setServiceAlarm(this, mLocationBool, mLocationTimer);
        WebPushIntent.setServerAlarm(this, mLocationBool, mSpinnerPos);

        Log.d(MAINACTIVITY_TEST_TAG, "Location tracking enabled");

    }

    @Override
    public int getSpinnerPosition() {
        return mSpinnerPos;
    }

    @Override
    public void setSpinnerPosition(int position) {
        Log.d(DEBUG_MAIN, "setSpinnerPosition called, position: " + position);
        Log.d(MAINACTIVITY_TEST_TAG, "Spinner position changed.");
        mSpinnerPos = position;
        if(mLocationBool) {
            Log.d(MAINACTIVITY_TEST_TAG, "Push interval updated.");
            WebPushIntent.setServerAlarm(this, mLocationBool, mSpinnerPos);
        }
    }

    private void setLoginBool(boolean b) {
        mLoginBool = b;
    }

    private boolean getLoginBool() {
        return mLocationBool;
    }

    @Override
    public void requestListUpdate() {
        if(mQueryLocations != null && !mQueryLocations.isEmpty()) {
            mTrackingFragment.setListAdapter(mQueryLocations);
        } else {
            Poptart.display(this,"No Data to display", 2);
        }
    }

    public void fillStartEnd() {
        final Calendar c = Calendar.getInstance();
        mUserRangeStart[0] = c.get(Calendar.YEAR);
        mUserRangeStart[1] = c.get(Calendar.MONTH);
        mUserRangeStart[2] = c.get(Calendar.DAY_OF_MONTH);
        mUserRangeStart[3] = c.get(Calendar.HOUR_OF_DAY);
        mUserRangeStart[4] = c.get(Calendar.MINUTE);
        mUserRangeEnd[0] = c.get(Calendar.YEAR);
        mUserRangeEnd[1] = c.get(Calendar.MONTH);
        mUserRangeEnd[2] = c.get(Calendar.DAY_OF_MONTH);
        mUserRangeEnd[3] = c.get(Calendar.HOUR_OF_DAY);
        mUserRangeEnd[4] = c.get(Calendar.MINUTE);
    }

    @Override
    public int[] getUserRangeStart() {
        return mUserRangeStart;
    }

    @Override
    public int[] getUserRangeEnd() {
        return mUserRangeEnd;
    }

    @Override
    public void setUserRange(int[] start, int[] end) {
        mUserRangeStart = start;
        mUserRangeEnd = end;
    }


    public void setLoginStatus(String loginStatus) {
       myPreferences.edit()
               .putString(getString(R.string.logged_in_activity), loginStatus)
               .apply();
    }
}



