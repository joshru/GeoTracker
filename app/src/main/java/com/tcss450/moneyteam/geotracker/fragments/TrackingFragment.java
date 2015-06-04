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
import com.tcss450.moneyteam.geotracker.interfaces.TabInterface;
import com.tcss450.moneyteam.geotracker.services.LocationIntentService;

import java.security.spec.ECField;
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
    private TabInterface mMainActivity;

    /**
     * year/month/day/hour/minute
     */
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
        mStartDate = (TextView) rootView.findViewById(R.id.f_location_date_text_start);
        mEndDate = (TextView) rootView.findViewById(R.id.f_location_date_text_end);
        mStartTime = (TextView) rootView.findViewById(R.id.f_location_time_text_start);
        mEndTime = (TextView) rootView.findViewById(R.id.f_location_time_text_end);
        mGetDataButton = (Button) rootView.findViewById(R.id.f_location_get_Data);
        mLocationList = (ListView) rootView.findViewById(R.id.list_location_listview);
        mLocationList.setScrollContainer(false);

        //GET DATA ONCLICK LISTENER~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        mGetDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebServiceHelper myHelper = new WebServiceHelper(rootView.getContext());
                LocationDBHelper dbHelper = new LocationDBHelper(rootView.getContext());
                mMainActivity.setUserRange(mGlobalStartDate, mGlobalEndDate);

                if(!mStartDate.getText().toString().isEmpty() &&        /* Start date not empty. */
                        !mEndDate.getText().toString().isEmpty() &&     /* End date not empty. */
                        !mStartTime.getText().toString().isEmpty() &&   /* Start time not empty. */
                        !mEndTime.getText().toString().isEmpty()) {     /* End time not empty. */

                    dbHelper.pushPointsToServer();                      /* SQL -> Server. */

                    Calendar start = Calendar.getInstance();            /* Start Date / TIME*/
                    start.set(mGlobalStartDate[0],                      /* Start year. */
                              mGlobalStartDate[1],                      /* Start month. */
                              mGlobalStartDate[2],                      /* Start day. */
                              mGlobalStartDate[3],                      /* Start hour. */
                              mGlobalStartDate[4]);                     /* Start minute. */

                    Calendar end = Calendar.getInstance();              /* End Date / TIME*/
                    end.set(mGlobalEndDate[0],                          /* End year. */
                            mGlobalEndDate[1],                          /* End month. */
                            mGlobalEndDate[2],                          /* End day. */
                            mGlobalEndDate[3],                          /* End hour. */
                            mGlobalEndDate[4]);                         /* End minute. */

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

    @Override
    public void onResume() {
        super.onResume();
        mMainActivity.requestListUpdate();
//        updateRangeDisplay();
    }

    /**
     * Attaches, gets current context
     * @param activity current activity
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        updateRangeDisplay();
        try {
            mMainActivity = (TabInterface) activity;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public void updateRangeDisplay() {
        mGlobalStartDate = mMainActivity.getUserRangeStart();
        mGlobalEndDate = mMainActivity.getUserRangeEnd();
        if(!mStartDate.getText().toString().isEmpty() &&        /* Start date not empty. */
                !mEndDate.getText().toString().isEmpty() &&     /* End date not empty. */
                !mStartTime.getText().toString().isEmpty() &&   /* Start time not empty. */
                !mEndTime.getText().toString().isEmpty()) {     /* End time not empty. */

            Calendar start = Calendar.getInstance();            /* Start Date / TIME*/
            start.set(mGlobalStartDate[0],                      /* Start year. */
                    mGlobalStartDate[1],                        /* Start month. */
                    mGlobalStartDate[2],                        /* Start day. */
                    mGlobalStartDate[3],                        /* Start hour. */
                    mGlobalStartDate[4]);                       /* Start minute. */

            Calendar end = Calendar.getInstance();              /* End Date / TIME*/
            end.set(mGlobalEndDate[0],                          /* End year. */
                    mGlobalEndDate[1],                          /* End month. */
                    mGlobalEndDate[2],                          /* End day. */
                    mGlobalEndDate[3],                          /* End hour. */
                    mGlobalEndDate[4]);                         /* End minute. */
        }

    }

    public void setListAdapter(ArrayList<Location> locationList) {
        Log.i("RANGE DATA", "Location Data sent to Fragment");

        ArrayList<String> stringList = new ArrayList<>();   /* List to hold locations strings. */

        // Parse the locations into a list of Strings
        for(Location l : locationList) {
            String longit = l.getLongitude() + ", Lat: ";
            String latit = l.getLatitude() + "";

            Date date = new Date(l.getTime() * 1000);
            DateFormat df = new SimpleDateFormat("EE, MM/dd, yyyy HH:mm a");
            stringList.add(df.format(date) + ", Long: " + longit + latit);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(rootView.getContext(),
                R.layout.list_row, stringList);
        mLocationList.setAdapter(adapter);
    }


    /**
     * Custom date listener inner class for listening for selected start and end dates
     */
    private class customDateListener implements DatePickerDialog.OnDateSetListener {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            activeText.setText((monthOfYear + 1) + "/" + dayOfMonth + "/" + year);
            if(activeText.getId() == R.id.f_location_date_text_start) {
                mGlobalStartDate[0] = year;
                mGlobalStartDate[1] = monthOfYear;
                mGlobalStartDate[2] = dayOfMonth;
            } else {
                mGlobalEndDate[0] = year;
                mGlobalEndDate[1] = monthOfYear;
                mGlobalEndDate[2] = dayOfMonth;
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
