package com.tcss450.moneyteam.geotracker.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;

public class MapFragment extends SupportMapFragment {
    private static final String TRACK_ID = "TRACK_ID";

    private GoogleMap mGoogleMap;

    public static MapFragment newInstance(long trackId) {
        Bundle args = new Bundle();
        args.putLong(TRACK_ID, trackId);
        MapFragment mf = new MapFragment();
        mf.setArguments(args);
        return mf;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, parent, savedInstanceState);
        mGoogleMap = getMap();
        mGoogleMap.setMyLocationEnabled(true);
        return v;
    }
}