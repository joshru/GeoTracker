package com.tcss450.moneyteam.geotracker;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Josh on 4/14/2015.
 */
public class TermsFragment extends Fragment {
    public int mCurrentPosition = -1;


    @Override
    public View onCreateView(LayoutInflater theInflater, ViewGroup theContainer,
                             Bundle theSavedState) {
        if (theSavedState != null) {
            mCurrentPosition = theSavedState.getInt("position");
        }
        return theInflater.inflate(R.layout.terms_view, theContainer, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        Bundle args = getArguments();
        if (args != null) {
            updateTOSView()
        }
    }

    public void updateTOSView() {
        TextView tos = (TextView) getActivity().findViewById(R.id.article);
    }

}

