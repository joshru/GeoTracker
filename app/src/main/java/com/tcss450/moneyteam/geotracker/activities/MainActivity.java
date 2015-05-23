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

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

/**
 * The Main Activity class. Account and location recording data will be displayed to the user here.
 * @author Brandon Bell
 * @author Alexander Cherry
 * @author Joshua Rueschenberg
 */
public class MainActivity extends Activity implements TabInterface {

    /** String used for logcat debugging*/
    private static final String DEBUG_TAG = "MAIN ACTIVITY DEBUG";

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

    /** The gesture detector object*/
    private GestureDetectorCompat mDetector;
    private String mUserEmail;
    private boolean mLocationBool;
    private int mLocationTimer;

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

        //CREATE FRAGMENTS (TABS)~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        mAccountSettingsFragment = new AccountFragment();
        mTrackingFragment = new TrackingFragment();
        mMapFragment = new MapFragment();

        //GETS ALL FRAGMENT USED SHAREDPREFERENCES ON STARTUP~~~~~~~~~~~~~~~~~~~~~~~
        loadSharedPreferences();
        LocationIntentService.setServiceAlarm(this, mLocationBool, mLocationTimer);
        //WebPushIntent. setWebUploadAlarm(rootView.getContext(), true, serviceGap);

        //GET ACTION BAR AND ADJUST SETTINGS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Logout");
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
       // TabHost
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

        //MAP TAB~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        mMapTab = actionBar.newTab();
        mMapTab.setIcon(getResources().getDrawable(R.drawable.pip_map));
        mMapTab.setText("Map");
        mMapTab.setTabListener(new PipTabListener(mMapFragment));
        actionBar.addTab(mMapTab);

    }

    private void loadSharedPreferences() {
        SharedPreferences myPreferences = getSharedPreferences(getString(R.string.user_info_main_key), Context.MODE_PRIVATE);
        mUserEmail = myPreferences.getString(getString(R.string.saved_email_key), "");
        mLocationBool = myPreferences.getBoolean(getString(R.string.saved_location_toggle_boolean), false);
        mLocationTimer = myPreferences.getInt(getString(R.string.key_location_poll_timer), 0);
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
                //Get the seekbar value
                SeekBar theSeekBar = (SeekBar) findViewById(R.id.seekBar);
//                int theSeekTime = theSeekBar.getProgress();
//                int theSeekMilliSeconds = theSeekTime * 3 * 60000;

                //GET SHARED PREFERERENCES/SET LOGIN IN BOOL~~~~~~~~~~~~~~~~~~~~~~~~
                SharedPreferences myPreferences = getSharedPreferences(getString(R.string.shared_pref_key), Context.MODE_PRIVATE);
                SharedPreferences.Editor myPrefEditor = myPreferences.edit();
                myPrefEditor.putBoolean(getString(R.string.logged_in_boolean), false);
                myPrefEditor.putBoolean(getString(R.string.saved_location_toggle_boolean), false);
//                myPrefEditor.putInt(getString(R.string.key_location_poll_timer), theSeekMilliSeconds);
                myPrefEditor.apply();

                LocationIntentService.setServiceAlarm(getApplicationContext(), false, 1);

                //PUSH ENTRIES TO SERVER~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                LocationDBHelper helper = new LocationDBHelper(this);
                helper.pushPointsToServer();

                //LAUNCH MAIN~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                Intent loginScreen = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(loginScreen);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                this.finish();
                return(true);
            default:
                return(super.onOptionsItemSelected(item));
        }
    }


    //FOLLOWING TOUCH LISTENERS HAVE NOT BEEN IMPLEMENTED (IGNORE)~~~~~~~~~~~~~~~~~~
    //TODO (PHASE II) IMPLEMENTED WANTED EVENTS, CODE REVIEWeRS CAN IGNORE~~~~~~~~~~~

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
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
            Log.i("RANGE DATA", mQueryLocations.toString());
        }
    }

    /**
     * Method used for printing debug messages to the android log for debugging purposes.
     * @param event the motion event
     * @return the on touch event
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int action = MotionEventCompat.getActionMasked(event);

        switch (action) {
            case (MotionEvent.ACTION_DOWN):
                Log.d(DEBUG_TAG, "Action was DOWN");
                return true;
            case (MotionEvent.ACTION_MOVE):
                Log.d(DEBUG_TAG, "Action was MOVE");
                return true;
            case (MotionEvent.ACTION_UP):
                Log.d(DEBUG_TAG, "Action was UP");
                return true;
            case (MotionEvent.ACTION_CANCEL):
                Log.d(DEBUG_TAG, "Action was CANCEL");
                return true;
            case (MotionEvent.ACTION_OUTSIDE):
                Log.d(DEBUG_TAG, "Movement occurred outside bounds " +
                        "of current screen element");
                return true;
            default:
                return super.onTouchEvent(event);
        }
    }

    @Override
    public void setLocations(ArrayList<Location> theLocations) {
        mQueryLocations = theLocations;
    }

    @Override
    public ArrayList<Location> getLocations() {
        Log.i("RANGE DATA", "Getter in main called");
        if(!mQueryLocations.isEmpty()) {
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
        }
    }

    @Override
    public boolean getLocationBool() {
        return mLocationBool;
    }

    @Override
    public void setLocationBool(boolean toggleEnabled) {
        mLocationBool = toggleEnabled;
    }

    @Override
    public void requestListUpdate() {
        if(mQueryLocations != null && !mQueryLocations.isEmpty()) {
            mTrackingFragment.setListAdapter(mQueryLocations);
        } else {
            Poptart.display(this,"No Data to display", 2);
        }
    }
}



