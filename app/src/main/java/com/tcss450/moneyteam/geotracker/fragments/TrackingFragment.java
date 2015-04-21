package com.tcss450.moneyteam.geotracker.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tcss450.moneyteam.geotracker.R;

/**
 * TODO
 * Activity for handling logic inside the tracking settings fragment.
 * @author Alexander Cherry(akac92@uw.edu)
 */
public class TrackingFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //GET REFERENCE TO ROOTVIEW AND INFLATE FRAGMENT~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        View rootView = inflater.inflate(R.layout.fragment_tracking_settings, container, false);
        return rootView;
    }
}
