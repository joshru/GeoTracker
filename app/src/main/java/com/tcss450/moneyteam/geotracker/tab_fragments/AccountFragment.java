package com.tcss450.moneyteam.geotracker.tab_fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.tcss450.moneyteam.geotracker.R;

/**
 * Created by Alex on 4/18/2015.
 */
public class AccountFragment extends Fragment {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_account_settings, container, false);
            return rootView;
        }
}
