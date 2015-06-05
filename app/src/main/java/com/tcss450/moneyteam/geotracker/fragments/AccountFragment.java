package com.tcss450.moneyteam.geotracker.fragments;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.tcss450.moneyteam.geotracker.Database.LocationDBHelper;
import com.tcss450.moneyteam.geotracker.R;
import com.tcss450.moneyteam.geotracker.Utilities.Poptart;
import com.tcss450.moneyteam.geotracker.interfaces.TabInterface;
import com.tcss450.moneyteam.geotracker.receivers.BootLoader;
import com.tcss450.moneyteam.geotracker.receivers.NetworkStatusReceiver;

/**
 * The account settings and information fragment for main activity
 * @author Brandon Bell
 * @author Alexander Cherry
 * @author Joshua Rueschenberg
 */
public class AccountFragment extends Fragment {

    public static String BUNDLE_TAG = "MAP";

    public static String ACCOUNT_TEST_LOG = "AccountFragment.junit.log";

    /**
     * The user email text view
     */
    private TextView mUserEmailLabel;

    /**
     * Password reset button
     */
    private Button mPasswordResetButton;

    /**
     * Security question layout
     */
    private RelativeLayout mEmailLayout;

    /**
     * Root view
     */
    private View rootView;

    /** */
    private ToggleButton mToggleButton;

    /**
     * Local shared preferences
     */
    private SharedPreferences myPreferences;
    private SeekBar mSeekBar;
    private ToggleButton mLocationTrackButton;
    private TextView mSeekTimeLabel;
    private Spinner mServiceSpinner;
    private TabInterface mMainActivity;

    /**
     * Password reset button
     */
    private Button mSyncButton;

    /**
     * Creates the account information fragment and assigns all relevant listeners
     *
     * @param inflater           the inflater
     * @param container          the container
     * @param savedInstanceState the saved instance state
     * @return the root view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //GET ROOT VIEW REFERENCE AND INFLATE FRAGMENT~~~~~~~~~~~~~~~~~~~~~~~~~~
        rootView = inflater.inflate(R.layout.fragment_account_settings, container, false);

        //GET REFERENCE TO VIEW FIELDS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        mUserEmailLabel = (TextView) rootView.findViewById(R.id.f_account_email);
        mPasswordResetButton = (Button) rootView.findViewById(R.id.account_password_reset);
        mSyncButton = (Button) rootView.findViewById(R.id.f_db_button);
        mEmailLayout = (RelativeLayout) rootView.findViewById(R.id.f_email_layout);
        mSeekBar = (SeekBar) rootView.findViewById(R.id.seekBar);
        mSeekTimeLabel = (TextView) rootView.findViewById(R.id.f_seek_time_label);
        mToggleButton = (ToggleButton) rootView.findViewById(R.id.toggleButton);
        mServiceSpinner = (Spinner) rootView.findViewById(R.id.server_spinner);

        //SET USER FIELDS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        myPreferences = rootView.getContext().getSharedPreferences(getString(R.string.shared_pref_key), Context.MODE_PRIVATE);
        setDisplayPreferences();
        setUpSpinner();

        //SET ONCLICK LISTENERS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        mPasswordResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ForgotPasswordDialog dialog = ForgotPasswordDialog.newInstance();
                dialog.show(getFragmentManager(), "forgotPW");
            }
        });

        mSyncButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Poptart.display(rootView.getContext(), "Data pushed to server", 3);
                Log.i("ACCOUNT FRAGMENT", "Synced to Server.");
                LocationDBHelper mDB = new LocationDBHelper(rootView.getContext());
                mDB.pushPointsToServer();
            }
        });

        //TOGGLE BUTTON ONCLICK~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
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
                Log.d(ACCOUNT_TEST_LOG, "Seekbar value changed to: " + progressValue);
                updateLocationTimer(progressValue * 3);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int minutesPolling = 1;
                if (progress > 0) {
                    minutesPolling = progress * 3;
                }
                updateLocationTimer(minutesPolling);
            }
        });

        //EMAIL LAYOUT LISTENER~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        mEmailLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelativeLayout lineView = (RelativeLayout) getActivity().findViewById(R.id.f_email_layout);
                TextView answer = (TextView) getActivity().findViewById(R.id.f_account_email);
                ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 1.0f, 1.0f, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f);
                anim.setDuration(700);
                lineView.startAnimation(anim);
                if (answer.getVisibility() == View.INVISIBLE) {
                    answer.startAnimation(anim);
                    answer.setVisibility(View.VISIBLE);
                } else {
                    answer.setVisibility(View.INVISIBLE);
                }
            }
        });

        //SPINNER ON SELECT LISTENER~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        mServiceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mMainActivity.setSpinnerPosition(i);
                mServiceSpinner.setSelection(i, true); //obscure bug fixed here
                Log.i("SPINNER ITEM", "Spinner Item #" + i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });


        return rootView;
    }

    private void setUpSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(rootView.getContext(),
                R.array.service_spinner_values,
                R.layout.item_spinner);
        adapter.setDropDownViewResource(R.layout.item_spinner_dropdown);
        mServiceSpinner.setAdapter(adapter);
        mServiceSpinner.setSelection(mMainActivity.getSpinnerPosition());
    }


    private void setDisplayPreferences() {
        mUserEmailLabel.setText(mMainActivity.getUserEmail());
        mSeekBar.setProgress(mMainActivity.getLocationTimer() / 3);
        changeTimeLabel(mMainActivity.getLocationTimer());
        mToggleButton.setChecked(mMainActivity.getLocationBool());
        locationToggle();
    }

    private void updateLocationTimer(int minutesPolling) {
        changeTimeLabel(minutesPolling);
        mMainActivity.setLocationTimer(minutesPolling);

        Log.i("LOCATION TIMER", "Timer Minutes: " + minutesPolling);
    }

    /**
     *
     * @param minutesPerTick
     */
    private void changeTimeLabel(final int minutesPerTick) {
        mSeekTimeLabel.setText("Location updates will occur every: "
                + minutesPerTick + " minute(s)");
    } //END void changeTimeLabel

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mMainActivity = (TabInterface) activity;
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    //TOGGLE METHODS ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void toggle0ff() {
        Log.i("TOGGLE", "Location Toggle-0ff");
        Log.d(ACCOUNT_TEST_LOG, "Location tracking toggled off.");

        /*Disable the bootloader receiver*/
        ComponentName receiver = new ComponentName(rootView.getContext(), BootLoader.class);
        mMainActivity.setLocationBool(false);
        PackageManager pm = rootView.getContext().getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
        /*Disable the network receiver*/

        receiver = new ComponentName(rootView.getContext(), NetworkStatusReceiver.class);
        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
        //CHANGE TEXT COLOR AND BACKGROUND~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        mToggleButton.setChecked(false);
        mToggleButton.setTextColor(getResources().getColor(R.color.pip_hint_shade));
        mToggleButton.setBackground(getResources().getDrawable(R.drawable.edit_text_gradient_inverse));
    } //END void toggle0ff()

    /**
     * Toggles location tracking on
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void toggle0n() {
        Log.d(ACCOUNT_TEST_LOG, "Location tracking toggled on.");
        Log.i("TOGGLE", "Location Toggle-0n");

        /*Enable the boot loader receiver*/
        ComponentName receiver = new ComponentName(rootView.getContext(), BootLoader.class);
        mMainActivity.setLocationBool(true);
        PackageManager pm = rootView.getContext().getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);

        /*Enable the network receiver*/
        receiver = new ComponentName(rootView.getContext(), NetworkStatusReceiver.class);
        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);


        //CHANGE TEXT COLOR AND BACKGROUND~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        mToggleButton.setChecked(true);
        mToggleButton.setTextColor(getResources().getColor(R.color.pip_light_neon));
        mToggleButton.setBackground(getResources().getDrawable(R.drawable.edit_text_gradient));
    } //END void toggle0n()

    /**
     * Visually toggles the location tracking button
     */
    public void locationToggle() {
        if(mToggleButton.isChecked()) {
            toggle0n();
        } else {
            toggle0ff();
        }
    } //END void locationToggle()
} //END AccountFragment.class
