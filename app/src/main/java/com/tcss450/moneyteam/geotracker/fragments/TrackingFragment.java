package com.tcss450.moneyteam.geotracker.fragments;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.tcss450.moneyteam.geotracker.R;
import com.tcss450.moneyteam.geotracker.Utilities.BootLoader;
import com.tcss450.moneyteam.geotracker.Utilities.Poptart;
import com.tcss450.moneyteam.geotracker.Utilities.WebServiceHelper;
import com.tcss450.moneyteam.geotracker.services.LocationIntentService;

import java.util.ArrayList;

/**
 * Activity for handling logic inside the tracking settings fragment.
 * @author Brandon Bell
 * @author Alexander Cherry
 * @author Joshua Rueschenberg
 */
public class TrackingFragment extends Fragment {

    private SharedPreferences myPreferences;
    private ToggleButton mToggleButton;
    private View rootView;
    private SeekBar mSeekBar;
    private TextView mSeekTimeText;
    private int mPollingTime;
    private ArrayList<Location> mQueryLocations;

    /**
     * Creates the new fragment for handling tracking settings
     * @param inflater the inflater
     * @param container the container
     * @param savedInstanceState the saved instance state
     * @return the root view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //GET REFERENCE TO ROOTVIEW AND INFLATE FRAGMENT~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        rootView = inflater.inflate(R.layout.fragment_tracking_settings, container, false);

        //INSTANTIATE FIELDS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        
        //GET REFERENCES TO UI ELEMENTS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        mToggleButton = (ToggleButton) rootView.findViewById(R.id.f_tracking_toggle_button);
        mSeekBar = (SeekBar) rootView.findViewById(R.id.f_tracking_seeker);
        mSeekTimeText = (TextView) rootView.findViewById(R.id.f_tracking_seeker_time);
        
        //GET SHARED PREFERENCES~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        myPreferences = getActivity().getSharedPreferences(getString(R.string.user_info_main_key), Context.MODE_PRIVATE);
        Boolean locationToggleBool = myPreferences.getBoolean(getString(R.string.saved_location_toggle_boolean), false);

        if(locationToggleBool) {
            toggle0n();
        } else {
            toggle0ff();
        }

        //TOOGLE BUTTON ONCLICK~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        mToggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locationToggle();
            }
        });

        //SEEKER ONPROGRESSCHANGE~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress = 0;

            @Override
            public void onProgressChanged(final SeekBar seekBar, final int progressValue, final boolean fromUser) {
                progress = progressValue;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int minutesPolling = 1;
                if(progress > 0) {
                    minutesPolling = progress * 6;
                }
                mSeekTimeText.setText("Location updates will occur every: " + minutesPolling + " minute(s)");
                mPollingTime = minutesPolling;
            }
        });

        
        return rootView;
    }

    public void locationToggle() {
        if(mToggleButton.isChecked()) {
            toggle0n();
        } else {
            toggle0ff();
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void toggle0n() {
        LocationIntentService.setServiceAlarm(rootView.getContext(), true);

        ComponentName receiver = new ComponentName(rootView.getContext(), BootLoader.class);
        PackageManager pm = rootView.getContext().getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
        //CHANGE TEXT COLOR AND BACKGROUND~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        mToggleButton.setChecked(true);
        myPreferences.edit().putBoolean(getString(R.string.saved_location_toggle_boolean), true).apply();
        mToggleButton.setTextColor(getResources().getColor(R.color.pip_light_neon));
        mToggleButton.setBackground(getResources().getDrawable(R.drawable.edit_text_gradient));
    }


    private void onEvent(WebServiceHelper.LocationEvent event) {
        Poptart.displayCustomDuration(rootView.getContext(), event.mEventMessage, 3);

        if (event.mSuccess) {
            mQueryLocations = event.mLocations;
            //place code or method calls here to handle displaying the points.
        }

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void toggle0ff() {
        LocationIntentService.setServiceAlarm(rootView.getContext(), false);

        ComponentName receiver = new ComponentName(rootView.getContext(), BootLoader.class);
        PackageManager pm = rootView.getContext().getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
        //CHANGE TEXT COLOR AND BACKGROUND~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        mToggleButton.setChecked(false);
        myPreferences.edit().putBoolean(getString(R.string.saved_location_toggle_boolean), false).apply();
        mToggleButton.setTextColor(getResources().getColor(R.color.pip_hint_shade));
        mToggleButton.setBackground(getResources().getDrawable(R.drawable.edit_text_gradient_inverse));
    }
}
