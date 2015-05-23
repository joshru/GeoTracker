package com.tcss450.moneyteam.geotracker.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.database.Cursor;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.tcss450.moneyteam.geotracker.Database.LocationDBHelper;
import com.tcss450.moneyteam.geotracker.R;
import com.tcss450.moneyteam.geotracker.Utilities.WebServiceHelper;
import com.tcss450.moneyteam.geotracker.interfaces.TabInterface;

import java.util.ArrayList;
import java.util.Date;

import de.greenrobot.event.EventBus;

/**
 * The fragment for displaying Google Map activity.
 * @author Brandon Bell
 * @author Alexander Cherry
 * @author Joshua Rueschenberg
 */
public class MapFragment extends Fragment implements OnMapReadyCallback {

    /** The Google Map view*/
    private MapView mMapView;

    /** The actual Google Map*/
    private GoogleMap mMap;

    private View mRootView;

    /** Collection of location objects*/
    private ArrayList<Location> mQueryLocations;

    private WebServiceHelper mWebHelper;

    private LocationDBHelper mDBHelper;

    private TabInterface mMainActivity;

    /**
     * Creates the new Google Map fragment
     * @param inflater the inflater
     * @param container the container
     * @param savedInstanceState the saved instance state
     * @return the root view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //GET ROOT VIEW AND INFLATE FRAGMENT~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        mRootView = inflater.inflate(R.layout.fragment_map, container, false);
        mWebHelper = new WebServiceHelper(mRootView.getContext());
        mDBHelper = new LocationDBHelper(mRootView.getContext());

        //REFERENCES FOR MAP~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        mMapView = (MapView) mRootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMap = mMapView.getMap();
        //DISPLAY MAP NOW~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        mMapView.onResume();

        //TRY TO LOAD MAP~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        //GENERATE MARKER~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                LatLng sydney = new LatLng(-33.867, 151.206);

        //SETUP MAP~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        if (mMap != null) {
             mMap.setMyLocationEnabled(true);
             mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        }

        mQueryLocations = new ArrayList<>();
//
//        Date start = new Date(0);
//        Date end = new Date();
//        mWebHelper.getRange(start, end);
//        EventBus.getDefault().register(this);
//        updateMarkers();

        //PERFORM CAMERA UPDATES HERE~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        return mRootView;
    }

    public void onEvent(WebServiceHelper.LocationEvent event) {
        Log.i("MAP EVENT", "This map has seen some shit");
        if (event.mSuccess) {
            mMap.clear();
            mQueryLocations = event.mLocations;
            updateMarkers();

        }

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mMainActivity = (TabInterface) activity;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void updateMarkers() {
        mQueryLocations = mMainActivity.getLocations();
        if (mQueryLocations != null && !mQueryLocations.isEmpty()) {
            for (Location l : mQueryLocations) {
                LatLng point = new LatLng(l.getLatitude(), l.getLongitude());
                mMap.addMarker(new MarkerOptions().position(point));
            }
        }

    }

    //UNIMPLEMENTED CAN BE IGNORED~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        updateMarkers();
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
        updateMarkers();

    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
//        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    /**
     * Shows the map when it becomes ready
     * @param googleMap the map
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng mapCenter = new LatLng(47.2414, 122.4594);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mapCenter, 13));

        // Flat markers will rotate when the map is rotated,
        // and change perspective when the map is tilted.
        mMap.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pip_columbus))
                .position(mapCenter)
                .flat(true)
                .rotation(245));

        CameraPosition cameraPosition = CameraPosition.builder()
                .target(mapCenter)
                .zoom(13)
                .bearing(90)
                .build();

        // Animate the change in camera view over 2 seconds
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition),
                2000, null);
    }
}
