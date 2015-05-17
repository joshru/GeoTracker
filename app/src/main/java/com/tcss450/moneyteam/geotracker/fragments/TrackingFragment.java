package com.tcss450.moneyteam.geotracker.fragments;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.TimePickerDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.tcss450.moneyteam.geotracker.Database.LocationDBHelper;
import com.tcss450.moneyteam.geotracker.R;
import com.tcss450.moneyteam.geotracker.Utilities.BootLoader;
import com.tcss450.moneyteam.geotracker.Utilities.Poptart;
import com.tcss450.moneyteam.geotracker.Utilities.WebServiceHelper;
import com.tcss450.moneyteam.geotracker.services.LocationIntentService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import de.greenrobot.event.EventBus;

/**
 * Activity for handling logic inside the tracking settings fragment.
 * @author Brandon Bell
 * @author Alexander Cherry
 * @author Joshua Rueschenberg
 */
public class TrackingFragment extends Fragment {

    /** Local shared preferences*/
    private SharedPreferences myPreferences;

    /** Toggle button for location tracking*/
    private ToggleButton mToggleButton;

    /** Root view*/
    private View rootView;

    /** Seek bar for tracking frequency*/
    private SeekBar mSeekBar;

    /** Text that displays how often to grab location*/
    private TextView mSeekTimeText;

    /** Location polling interval*/
    private int mPollingTime;

    /** Collection of location objects*/
    private ArrayList<Location> mQueryLocations;

    /** Start date for displayed locations*/
    private TextView mStartDate;

    /** End date for displayed locations*/
    private TextView mEndDate;

    /** End time for displayed locations*/
    private TextView mEndTime;

    /** Start time for displayed locations*/
    private TextView mStartTime;

    /** Active text*/
    private TextView activeText;

    /** Get location tracking button*/
    private Button mGetDataButton;

    /** List view for showing location data*/
    private ListView mLocationList;

    /** Current context*/
    private Context mContext;

    //Month/day/year/hour/minute
    private int[] mGlobalStartDate;
    private int[] mGlobalEndDate;

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

        setRetainInstance(true);
        //GET REFERENCE TO ROOTVIEW AND INFLATE FRAGMENT~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        rootView = inflater.inflate(R.layout.fragment_tracking_settings, container, false);

        //INSTANTIATE FIELDS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        mGlobalStartDate = new int[5];
        mGlobalEndDate = new int[5];

        
        //GET REFERENCES TO UI ELEMENTS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        mToggleButton = (ToggleButton) rootView.findViewById(R.id.f_tracking_toggle_button);
        mSeekBar = (SeekBar) rootView.findViewById(R.id.f_tracking_seeker);
        mSeekTimeText = (TextView) rootView.findViewById(R.id.f_tracking_seeker_time);
        mStartDate = (TextView) rootView.findViewById(R.id.f_location_date_text_start);
        mEndDate = (TextView) rootView.findViewById(R.id.f_location_date_text_end);
        mStartTime = (TextView) rootView.findViewById(R.id.f_location_time_text_start);
        mEndTime = (TextView) rootView.findViewById(R.id.f_location_time_text_end);
        mGetDataButton = (Button) rootView.findViewById(R.id.f_location_get_Data);
        mLocationList = (ListView) rootView.findViewById(R.id.list_location_listview);
//        ListView list = (ListView) rootView.findViewById(R.id.list_location);



        //GET SHARED PREFERENCES~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        //
        myPreferences = getActivity().getSharedPreferences(getString(R.string.user_info_main_key), Context.MODE_PRIVATE);
        Boolean locationToggleBool = myPreferences.getBoolean(getString(R.string.saved_location_toggle_boolean), false);

        if(locationToggleBool) {
            toggle0n();
        } else {
            toggle0ff();
        }

        //GET DATA ONCLICK LISTENER~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        mGetDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebServiceHelper myHelper = new WebServiceHelper(rootView.getContext());
                LocationDBHelper dbHelper = new LocationDBHelper(rootView.getContext());
                String startDate = mStartDate.getText().toString();
                String endDate = mEndDate.getText().toString();
                String startTime = mStartTime.getText().toString();
                String endTime = mStartTime.getText().toString();

                if(!startDate.isEmpty() && !endDate.isEmpty()
                        && !startTime.isEmpty() && !endTime.isEmpty()) {
                    dbHelper.pushPointsToServer();
                    Calendar start = Calendar.getInstance();
                    Calendar end = Calendar.getInstance();
                    start.set(mGlobalStartDate[2], mGlobalStartDate[0], mGlobalStartDate[1], mGlobalStartDate[3], mGlobalStartDate[4]);

                    end.set(mGlobalEndDate[2], mGlobalEndDate[0], mGlobalEndDate[1], mGlobalEndDate[3],mGlobalEndDate[4]);

                    Log.i("DATE", "START: " + start.toString());
                    Log.i("DATE", "END: " + end.toString());
                    myHelper.getRange(start.getTime(), end.getTime());
                } else {
                    Poptart.display(rootView.getContext(), "Please fill in all fields", Toast.LENGTH_LONG);
                }
            }
        });


        //SET DATE AND TIME DIALOGS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        mStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activeText = mStartDate;
                dateDialog();
            }
        });

        mEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activeText = mEndDate;
                dateDialog();
            }
        });

        mStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activeText = mStartTime;
                timeDialog();
                Log.i("START DATE", "Start Date has been clicked");
            }
        });

        mEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activeText = mEndTime;
                timeDialog();
            }
        });

        //TOOGLE BUTTON ONCLICK~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        mToggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locationToggle();
            }
        });

        //SEEKER LISTENER~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
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

    /**
     * Updates the selected date dialog
     */
    private void dateDialog() {

        // Process to get Current Date
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        // Launch Date Picker Dialog
        DatePickerDialog dpd = new DatePickerDialog(rootView.getContext(), new customDateListener(), mYear, mMonth, mDay);
        dpd.show();

    }

    /**
     * Updates the selected time dialog
     */
    private void timeDialog() {
        // Process to get Current Time
        final Calendar c = Calendar.getInstance();
        int mHour = c.get(Calendar.HOUR_OF_DAY);
        int mMinute = c.get(Calendar.MINUTE);

        TimePickerDialog tpd = new TimePickerDialog(rootView.getContext(), new customTimeListener(), mHour, mMinute, true);
        tpd.show();

    }

    /**
     * Starts, registers the Event Bus
     */
    @Override
    public void onStart() {
        super.onStart();

        EventBus.getDefault().register(this);
    }


    /**
     * Destroys, unregisters the Event Bus
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
      //  EventBus.getDefault().unregister(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }
    /**
     * Detaches, unregisters the Event Bus
     *//*
    @Override
    public void onDetach() {
        super.onDetach();
        EventBus.getDefault().unregister(this);

    }

    *//**
     * Destroys the view, unregisters Event Bus
     *//*
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);

    }*/

    /**
     * Attaches, gets current context
     * @param activity current activity
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity.getApplicationContext();
    }

    /**
     * Visually toggles the location tracking button
     */
    public void locationToggle() {
        if(mToggleButton.isChecked()) {
            toggle0n();
        } else {
            toggle0ff();
        }
    }

    /**
     * Toggles location tracking on
     */
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

    /**
     * Event handler for Event bus. Gets fired locations and adds them to the list view
     * @param event
     */
    public void onEvent(WebServiceHelper.LocationEvent event) {
        Poptart.displayCustomDuration(rootView.getContext(), event.mEventMessage, 3);

        if (event.mSuccess) {
            // TODO add the list items here
            mQueryLocations = event.mLocations;
            Log.i("RANGE DATA", mQueryLocations.toString());

            ArrayList<String> locations = new ArrayList<String>();
            for(Location l : mQueryLocations) {
                String time = l.getTime() * 1000 + ", Long: ";
                String longit = l.getLongitude() + ", Lat: ";
                String latit = l.getLatitude() + "";

                Date date = new Date(l.getTime() * 1000);
                DateFormat df = new SimpleDateFormat("EE, MM/dd, yyyy HH:mm a");

                locations.add(df.format(date) + ", Long: " + longit + latit);
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(rootView.getContext(), R.layout.list_row, locations);
            mLocationList.setAdapter(adapter);

        }

    }

    /**
     * Toggles location tracking off
     */
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

    /**
     * Custom date listener inner class for listening for selected start and end dates
     */
    private class customDateListener implements DatePickerDialog.OnDateSetListener {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            activeText.setText(monthOfYear + "/" + dayOfMonth + "/" + year);
            if(activeText.getId() == R.id.f_location_date_text_start) {
                mGlobalStartDate[0] = monthOfYear;
                mGlobalStartDate[1] = dayOfMonth;
                mGlobalStartDate[2] = year;
            } else {
                mGlobalEndDate[0] = monthOfYear;
                mGlobalEndDate[1] = dayOfMonth;
                mGlobalEndDate[2] = year;
            }
        }
    }

    /**
     * Custom time listener for listening for selected start and end times
     */
    private class customTimeListener implements  TimePickerDialog.OnTimeSetListener {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            activeText.setText(hourOfDay + ":" + minute);
            if(activeText.getId() == R.id.f_location_time_text_start) {
                mGlobalStartDate[3] = hourOfDay;
                mGlobalStartDate[4] = minute;
            } else {
                mGlobalEndDate[3] = hourOfDay;
                mGlobalEndDate[4] = minute;
            }
        }
    }
}
