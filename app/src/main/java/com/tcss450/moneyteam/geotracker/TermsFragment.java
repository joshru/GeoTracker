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
        return theInflater.inflate(R.layout.fragment_terms, theContainer, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        Bundle args = getArguments();
        if (args != null) {
            updateTOSView(args.getInt("position"));
        } else if (mCurrentPosition != -1) {
            updateTOSView(mCurrentPosition);
        }
    }

    public void updateTOSView(int position) {
        TextView tos = (TextView) getActivity().findViewById(R.id.terms);
        tos.setText("Terms of Service go here");
        mCurrentPosition = position;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("position", mCurrentPosition);
    }

}

