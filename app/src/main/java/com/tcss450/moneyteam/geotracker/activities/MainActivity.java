package com.tcss450.moneyteam.geotracker.activities;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.util.Log;
import android.view.MenuItem;

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

    /** Strings used for logcat debugging*/
    private static final String DEBUG_MAIN = "DEBUG MAIN";
    private static final String SAVE = "SAVE";
    private static final String LOAD = "LOAD";
    private static final String STORED_FRAGMENT = "Stored Fragment";

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

    private Fragment mListFragment;

    /** Collection of location objects*/
    private ArrayList<Location> mQueryLocations;

    /** Collections of start and end dates and times*/
    private int[] mUserRangeStart;
    private int[] mUserRangeEnd;

    /** The gesture detector object*/
    private GestureDetectorCompat mDetector;

    /** The user's email*/
    private String mUserEmail;

    /** Boolean for if location tracking is enabled or not*/
    private boolean mLocationBool;

    /** Interval for pushing locations*/
    private int mLocationTimer;

    /** The selected position in the interval spinner*/
    private int mSpinnerPos;

    /** User is logged in boolean*/
    private boolean mLoginBool;

    /** Shared preferences object*/
    private SharedPreferences myPreferences;
    private Fragment mGlobalFragment;

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
            mAccountSettingsTab.setText("Account Settings");
            mAccountSettingsTab.setTabListener(new PipTabListener(mAccountSettingsFragment));
            actionBar.addTab(mAccountSettingsTab);

            //LOCATION TRACKING SETTINGS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            mTrackingTab = actionBar.newTab();
            mTrackingTab.setText("Location Tracking Settings");
            mTrackingTab.setTabListener(new PipTabListener(mTrackingFragment));
            actionBar.addTab(mTrackingTab);
            mUserRangeStart = new int[5];
            mUserRangeEnd = new int[5];
            fillStartEnd();

            //MAP TAB~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            mMapTab = actionBar.newTab();
            mMapTab.setText("Map");
            mMapTab.setTabListener(new PipTabListener(mMapFragment));
            actionBar.addTab(mMapTab);
    }

    /**
     * Load saved information from shared preferences
     */
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

    /**
     * Save user options in shared preferences
     */
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
        FragmentManager manager = getFragmentManager();
        manager.putFragment(outState, STORED_FRAGMENT, mGlobalFragment);
        Log.i("TAB LISTENER", "Fragment saved: " + mGlobalFragment.toString());

    }

    /**
     * Calls super onRestoreInstanceState
     * @param inState
     */
    @Override
    protected void onRestoreInstanceState(Bundle inState) {
        super.onRestoreInstanceState(inState);
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        if (inState != null) {
            mGlobalFragment = (Fragment) manager.getFragment(inState, STORED_FRAGMENT);
            Log.i("TAB LISTENER", "Fragment restored: " + mGlobalFragment.toString());
        } else {
            mGlobalFragment = new AccountFragment();
            transaction.add(R.id.account_fragment, mGlobalFragment, STORED_FRAGMENT);
            Log.i("TAB LISTENER", "Fragment restored: " + mGlobalFragment.toString());

            transaction.commit();
        }
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
            mTrackingFragment.setLocations(mQueryLocations);
            Log.i(DEBUG_MAIN, mQueryLocations.toString());
        }
    }

    @Override
    public void setLocations(ArrayList<Location> theLocations) {
        mQueryLocations = theLocations;
    }

    @Override
    public void setGlobalFragment(Fragment current) {
        mGlobalFragment =  current;
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
            savePreferences();
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
        savePreferences();


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
        savePreferences();

    }

    /**
     * Sets the boolean after the user logs in
     * @param b the login status boolean
     */
    private void setLoginBool(boolean b) {
        mLoginBool = b;
    }

    /**
     * Returns the login status of the user
     * @return true if user logged in, false otherwise
     */
    private boolean getLoginBool() {
        return mLocationBool;
    }

    @Override
    public void requestListUpdate() {
        if(mQueryLocations != null && !mQueryLocations.isEmpty()) {
            mTrackingFragment.setLocations(mQueryLocations);
        } else {
            Poptart.display(this,"No Data to display", 2);
        }
    }

    /**
     * Fills start and end time arrays with current date to prevent NPEs
     */
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


    /**
     * Sets the login status of the user
     * @param loginStatus the login status
     */
    public void setLoginStatus(String loginStatus) {
       myPreferences.edit()
               .putString(getString(R.string.logged_in_activity), loginStatus)
               .apply();
    }

    /**
     * Actionbar listener for all tabs on inside MainActivity.
     * @author Brandon Bell
     * @author Alexander Cherry
     * @author Joshua Rueschenberg
     */
    public class PipTabListener implements ActionBar.TabListener {

        /** The fragment context.*/
        Fragment mFragment;

        /**
         * Listener for the tab view
         * @param fragment the fragment selected by user
         */
        public PipTabListener(Fragment fragment) {
            this.mFragment = fragment;
        }

        /**
         * Called when a tab enters the selected state
         * @param tab the tab selected
         * @param ft the fragment transaction
         */
        @Override
        public void onTabSelected(Tab tab, FragmentTransaction ft) {
            //mGlobalFragment = mFragment;
            Log.i("TAB LISTENER", "Fragment selected: " + mFragment.toString());

            ft.replace(R.id.fragment_container, mFragment);
        }

        /**
         * Called when a tab exits the selected state
         * @param tab the tab selected
         * @param ft the fragment transaction
         */
        @Override
        public void onTabUnselected(Tab tab, FragmentTransaction ft){
            Log.i("TAB LISTENER", "Fragment unselected: " + mFragment.toString());
            ft.remove(mFragment);
        }

        /**
         * (Unused) Called when a tab that is already is selected is chosen again by the user
         * @param tab
         * @param ft
         */
        @Override
        public void onTabReselected(Tab tab, FragmentTransaction ft) {
        }
    }

}



