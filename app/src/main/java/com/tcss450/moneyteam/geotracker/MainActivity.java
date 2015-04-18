package com.tcss450.moneyteam.geotracker;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.Window;

import com.tcss450.moneyteam.geotracker.tab_fragments.AccountFragment;
import com.tcss450.moneyteam.geotracker.tab_fragments.MapFragment;
import com.tcss450.moneyteam.geotracker.tab_fragments.TrackingFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements GestureDetector.OnGestureListener,
        GestureDetector.OnDoubleTapListener {

    private static final String DEBUG_TAG = "MAIN ACTIVITY DEBUG";
    private List<Fragment> tabFragmentList = new ArrayList<>();

    private Tab mAccountSettingsTab;
    private Fragment mAccountSettingsFragment;

    private Tab mTrackingTab;
    private Fragment mTrackingFragment;

    private Tab mMapTab;
    private Fragment mMapFragment;

    private GestureDetectorCompat mDetector;

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_tab);

        //Create fragments
        mAccountSettingsFragment = new AccountFragment();
        mTrackingFragment = new TrackingFragment();
        mMapFragment = new MapFragment();

        ActionBar actionBar = getActionBar();

        // Hide Actionbar Icon
        actionBar.setDisplayShowHomeEnabled(false);

        // Hide Actionbar Title
        actionBar.setDisplayShowTitleEnabled(false);

        // Create Actionbar Tabs
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
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }
}


























/*

    // GOOGLE MAPS FUNCTIONALITY (DO NOT TOUCH)~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    @Override
    protected void onResume() {
        Log.e(MAIN_ACTIVITY_ERROR_KEY, "onResume");
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link com.google.android.gms.maps.SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     *//*
    private void setUpMapIfNeeded() {
        Log.e(MAIN_ACTIVITY_ERROR_KEY, "setUpMapIfNeeded");
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

/*
    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     *//*
    private void setUpMap() {
        Log.e(MAIN_ACTIVITY_ERROR_KEY, "setUpMap");

        Marker defaultMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(0,0)).title("Fallout Seattle Zone")
                .icon(BitmapDescriptorFactory
                        .defaultMarker(140.0F)));
    }
}
        */


