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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.tcss450.moneyteam.geotracker.R;
import com.tcss450.moneyteam.geotracker.Utilities.BootLoader;
import com.tcss450.moneyteam.geotracker.interfaces.TabInterface;
import com.tcss450.moneyteam.geotracker.services.LocationIntentService;
import com.tcss450.moneyteam.geotracker.services.WebPushIntent;

/**
 * The account settings and information fragment for main activity
 * @author Brandon Bell
 * @author Alexander Cherry
 * @author Joshua Rueschenberg
 */
public class AccountFragment extends Fragment {
    /**
     * The user email text view
     */
    private TextView mUserEmailLabel;

    /**
     * The user secret answer text view
     */
    private TextView mUserAnswerLabel;

    /**
     * The user secret question text view
     */
    private TextView mUserQuestionLabel;

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
                if (progress > 0) {
                    minutesPolling = progress * 3;
                }
                updateLocationTimer(minutesPolling);
            }
        }); // END seekBar Listeners

        //EMAIL LAYOUT LISTENER~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        mEmailLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView lineView = (ImageView) getActivity().findViewById(R.id.f_email_line);
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

        return rootView;
    }

    private void setUpSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(rootView.getContext(), R.array.service_spinner_values,
                R.layout.item_spinner);
        adapter.setDropDownViewResource(R.layout.item_spinner_dropdown);
        mServiceSpinner.setAdapter(adapter);
    }


    private void setDisplayPreferences() {
        mUserEmailLabel.setText(mMainActivity.getUserEmail());
        mSeekBar.setProgress(mMainActivity.getLocationTimer());
        changeTimeLabel(mMainActivity.getLocationTimer());
        mToggleButton.setChecked(mMainActivity.getLocationBool());
    }

    private void updateLocationTimer(int minutesPolling) {
        changeTimeLabel(minutesPolling);
        myPreferences.edit().putInt(getResources()
                .getString(R.string.key_location_poll_timer), minutesPolling)
                .apply();
        Log.i("LOCATION TIMER", "Timer Minutes: " + minutesPolling);
    }

    private void updateServerTimer(int minutesGap) {
        myPreferences.edit().putInt(getResources()
                .getString(R.string.key_location_upload_gap), minutesGap)
                .apply();
    }

    /**
     *
     * @param minutesPerTick
     */
    private void changeTimeLabel(final int minutesPerTick) {
        mSeekTimeLabel.setText("Location updates will occur every: " + minutesPerTick + " minute(s)");
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
        LocationIntentService.setServiceAlarm(rootView.getContext(), false, 1);

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
    } //END void toggle0ff()

    /**
     * Toggles location tracking on
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void toggle0n() {
        int pollingTime = myPreferences.getInt(getString(R.string.key_location_poll_timer), 0);
        LocationIntentService.setServiceAlarm(rootView.getContext(), true, pollingTime);

        ComponentName receiver = new ComponentName(rootView.getContext(), BootLoader.class);
        PackageManager pm = rootView.getContext().getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
        //CHANGE TEXT COLOR AND BACKGROUND~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        mToggleButton.setChecked(true);
        myPreferences.edit().putInt(getString(R.string.key_location_poll_timer), pollingTime).apply();
        myPreferences.edit().putBoolean(getString(R.string.saved_location_toggle_boolean), true).apply();
        Log.i("Account Tag", "locationPollTime: " + pollingTime);
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
