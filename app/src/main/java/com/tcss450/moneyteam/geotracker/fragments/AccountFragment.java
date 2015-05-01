package com.tcss450.moneyteam.geotracker.fragments;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.tcss450.moneyteam.geotracker.R;
import com.tcss450.moneyteam.geotracker.Utilities.BootLoader;
import com.tcss450.moneyteam.geotracker.services.LocationIntentService;

/**
 * The account settings and information fragment for main activity
 * @author Brandon Bell
 * @author Alexander Cherry
 * @author Joshua Rueschenberg
 */
public class AccountFragment extends Fragment {
    /** The user email text view*/
    private TextView mUserEmailLabel;

    /** The user secret answer text view*/
    private TextView mUserAnswerLabel;

    /** The user secret question text view*/
    private TextView mUserQuestionLabel;
    private Button mPasswordResetButton;
    private RelativeLayout mAnswerLayout;
    private View rootView;
    private ToggleButton mToggleButton;
    private SharedPreferences myPreferences;

    /**
     * Createst the account information fragment and assigns all relevant listeners
     * @param inflater the inflater
     * @param container the container
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
            mUserQuestionLabel = (TextView) rootView.findViewById(R.id.f_account_question);
            mUserAnswerLabel = (TextView) rootView.findViewById(R.id.f_account_answer);
            mPasswordResetButton = (Button) rootView.findViewById(R.id.account_password_reset);
            mAnswerLayout = (RelativeLayout) rootView.findViewById(R.id.account_answer_layout);
            mToggleButton = (ToggleButton) rootView.findViewById(R.id.f_account_toggle_button);

            //GET SHARED PREFERENCES~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            myPreferences = getActivity().getSharedPreferences(getString(R.string.user_info_main_key), Context.MODE_PRIVATE);
            String userEmail = myPreferences.getString(getString(R.string.saved_email_key), "");
            String userQuestion = myPreferences.getString(getString(R.string.saved_question_key), "");
            String userAnswer = myPreferences.getString(getString(R.string.saved_question_answer_key), "");
            Boolean locationToggleBool = myPreferences.getBoolean(getString(R.string.saved_location_toggle_boolean), false);

            if(locationToggleBool) {
                toggle0n();
            } else {
                toggle0ff();
            }

            //SET USER FIELDS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            mUserEmailLabel.setText(userEmail);
            mUserQuestionLabel.setText(userQuestion);
            mUserAnswerLabel.setText(userAnswer);

            //SET ONCLICK LISTENERS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            mPasswordResetButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ForgotPasswordDialog dialog = ForgotPasswordDialog.newInstance();
                    dialog.show(getFragmentManager(), "forgotPW");
                }
            });

            mAnswerLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageView lineView = (ImageView) getActivity().findViewById(R.id.account_line_bar);
                    TextView answer = (TextView) getActivity().findViewById(R.id.f_account_answer);
                    ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 1.0f, 1.0f, Animation.RELATIVE_TO_SELF,1.0f, Animation.RELATIVE_TO_SELF, 0.5f);
                    anim.setDuration(700);
                    lineView.startAnimation(anim);
                    if(answer.getVisibility() == View.INVISIBLE) {
                        answer.startAnimation(anim);
                        answer.setVisibility(View.VISIBLE);
                    }else{
                        answer.setVisibility(View.INVISIBLE);
                    }
                }
            });

            mToggleButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    locationToggle();
                }
            });

            return rootView;
        }

    /**
     * OnClickListener for change password button.
     */
    public void changePassword() {
        View lineView = getActivity().findViewById(R.id.account_line_bar);
        ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 1.0f, 1.0f, Animation.RELATIVE_TO_SELF,1.0f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setDuration(700);
        lineView.startAnimation(anim);
        ForgotPasswordDialog dialog = ForgotPasswordDialog.newInstance();
       dialog.show(getFragmentManager(), "forgotPW");
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
