package com.tcss450.moneyteam.geotracker.activities;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.tcss450.moneyteam.geotracker.R;
import com.tcss450.moneyteam.geotracker.PipTabListener;
import com.tcss450.moneyteam.geotracker.fragments.AccountFragment;
import com.tcss450.moneyteam.geotracker.fragments.MapFragment;
import com.tcss450.moneyteam.geotracker.fragments.TrackingFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO javadoc dis
 * @author Alexander Cherry(akac92@uw.edu)
 */
public class MainActivity extends Activity implements GestureDetector.OnGestureListener,
        GestureDetector.OnDoubleTapListener {

    /** TODO javadoc these up yo.
     */
    private static final String DEBUG_TAG = "MAIN ACTIVITY DEBUG";

    private Tab mAccountSettingsTab;
    private Fragment mAccountSettingsFragment;

    private Tab mTrackingTab;
    private Fragment mTrackingFragment;

    private Tab mMapTab;
    private Fragment mMapFragment;

    private GestureDetectorCompat mDetector;

    /**
     *
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

    //FOLLOWING TOUCH LISTENERS HAVE NOT BEEN IMPLEMENTED (IGNORE)~~~~~~~~~~~~~~~~~~
    //TODO (PHASE II) IMPLEMENTED WANTED EVENTS, CODE REVIEWRS CAN IGNORE~~~~~~~~~~~
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
    public boolean onSingleTapConfirmed(MotionEvent e) {
        Log.d(DEBUG_TAG, "onDSingleTapConfirmed: " + e.toString());
        return true;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        Log.d(DEBUG_TAG, "onDoubleTap: " + e.toString());
        return true;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        Log.d(DEBUG_TAG, "onDoubleTapEvent: " + e.toString());
        return true;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        Log.d(DEBUG_TAG, "onDown: " + e.toString());
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {
        Log.d(DEBUG_TAG, "onShowPress: " + e.toString());
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        Log.d(DEBUG_TAG, "onSingleTapUp: " + e.toString());
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        Log.d(DEBUG_TAG, "onScroll: e1:" + e1.toString() + " e2:" + e2.toString() + "(dX, dY):(" + distanceX + ", " + distanceY + ")");
        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        Log.d(DEBUG_TAG, "onLongPress: " + e.toString());
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        Log.d(DEBUG_TAG, "onFling: e1:" + e1.toString() + " e2:" + e2.toString() + "(vX, vY):(" + velocityX + ", " + velocityY + ")");
        return true;
    }
}



