package com.tcss450.moneyteam.geotracker.activities;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;

import com.tcss450.moneyteam.geotracker.R;
import com.tcss450.moneyteam.geotracker.PipTabListener;
import com.tcss450.moneyteam.geotracker.fragments.AccountFragment;
import com.tcss450.moneyteam.geotracker.fragments.ForgotPasswordDialog;
import com.tcss450.moneyteam.geotracker.fragments.MapFragment;
import com.tcss450.moneyteam.geotracker.fragments.TrackingFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * The Main Activity class. Account and location recording data will be displayed to the user here.
 * @author Brandon Bell
 * @author Alexander Cherry
 * @author Joshua Rueschenberg
 */
public class MainActivity extends Activity implements GestureDetector.OnGestureListener,
        GestureDetector.OnDoubleTapListener {

    /** String used for logcat debugging*/
    private static final String DEBUG_TAG = "MAIN ACTIVITY DEBUG";

    /** The account setting tab*/
    private Tab mAccountSettingsTab;

    /** The account settings fragment to be displayed via Account Settings Tab*/
    private Fragment mAccountSettingsFragment;

    /** The location tracking tab*/
    private Tab mTrackingTab;

    /** The tracking information fragment to be displayed via Tracking Tab*/
    private Fragment mTrackingFragment;

    /** The Google maps tab*/
    private Tab mMapTab;

    /** Google maps with location data will be displayed to user via Map Tab*/
    private Fragment mMapFragment;

    /** The gesture detector object*/
    private GestureDetectorCompat mDetector;

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

        //GET ACTION BAR AND ADJUST SETTINGS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
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

        //MAP TAB~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        mMapTab = actionBar.newTab();
        mMapTab.setIcon(getResources().getDrawable(R.drawable.pip_map));
        mMapTab.setText("Map");
        mMapTab.setTabListener(new PipTabListener(mMapFragment));
        actionBar.addTab(mMapTab);

        //GESTURE DETECTOR~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~!
        mDetector = new GestureDetectorCompat(this,this);
        mDetector.setOnDoubleTapListener(this);

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
                //GET SHARED PREFERERENCES/SET LOGGIN IN BOOL~~~~~~~~~~~~~~~~~~~~~~~~
                SharedPreferences myPreferences = getSharedPreferences(getString(R.string.shared_pref_key), Context.MODE_PRIVATE);
                SharedPreferences.Editor myPrefEditor = myPreferences.edit();
                myPrefEditor.putBoolean(getString(R.string.logged_in_boolean), true);
                myPrefEditor.apply();
                //LAUNCH MAIN~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                Intent loginScreen = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(loginScreen);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                return(true);
        }

        return(super.onOptionsItemSelected(item));
    }


    //FOLLOWING TOUCH LISTENERS HAVE NOT BEEN IMPLEMENTED (IGNORE)~~~~~~~~~~~~~~~~~~
    //TODO (PHASE II) IMPLEMENTED WANTED EVENTS, CODE REVIEWRS CAN IGNORE~~~~~~~~~~~

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

    /**
     * For debugging single taps on main activity.
     * @param e the motion event.
     * @return true
     */
    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        Log.d(DEBUG_TAG, "onDSingleTapConfirmed: " + e.toString());
        return true;
    }

    /**
     * For debugging double taps on main activity.
     * @param e the motion event.
     * @return true
     */
    @Override
    public boolean onDoubleTap(MotionEvent e) {
        Log.d(DEBUG_TAG, "onDoubleTap: " + e.toString());
        return true;
    }

    /**
     * For debugging double tap events on the main activity.
     * @param e the motion event.
     * @return true
     */
    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        Log.d(DEBUG_TAG, "onDoubleTapEvent: " + e.toString());
        return true;
    }

    /**
     * For debugging touch activity on the main activity.
     * @param e the motion event.
     * @return true.
     */
    @Override
    public boolean onDown(MotionEvent e) {
        Log.d(DEBUG_TAG, "onDown: " + e.toString());
        return true;
    }

    /**
     * For debugging long holds to show data on main activity.
     * @param e the motion event.
     */
    @Override
    public void onShowPress(MotionEvent e) {
        Log.d(DEBUG_TAG, "onShowPress: " + e.toString());
    }

    /**
     * For debugging when the user taps on the Main Event
     * @param e the the motion event.
     * @return true
     */
    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        Log.d(DEBUG_TAG, "onSingleTapUp: " + e.toString());
        return true;
    }

    /**
     * For debugging when the user has scrolled on the Main Event
     * @param e1 the motion event 1
     * @param e2 the motion event 2
     * @param distanceX the x distance
     * @param distanceY the y distance
     * @return true
     */
    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        Log.d(DEBUG_TAG, "onScroll: e1:" + e1.toString() + " e2:" + e2.toString() + "(dX, dY):("
                          + distanceX + ", " + distanceY + ")");
        return true;
    }

    /**
     * For debugging when the user long presses on the main event.
     * @param e the motion event
     */
    @Override
    public void onLongPress(MotionEvent e) {
        Log.d(DEBUG_TAG, "onLongPress: " + e.toString());
    }

    /**
     * For debugging when the user performs a fling on main event.
     * @param e1 the motion event 1
     * @param e2 the motion event 2
     * @param velocityX x velocity
     * @param velocityY y velocity
     * @return true
     */
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        Log.d(DEBUG_TAG, "onFling: e1:" + e1.toString() + " e2:" + e2.toString() + "(vX, vY):("
                          + velocityX + ", " + velocityY + ")");
        return true;
    }
}



