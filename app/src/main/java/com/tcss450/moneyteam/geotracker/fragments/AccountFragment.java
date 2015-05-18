package com.tcss450.moneyteam.geotracker.fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
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

    /** Password reset button*/
    private Button mPasswordResetButton;

    /** Security question layout*/
    private RelativeLayout mEmailLayout;

    /** Root view*/
    private View rootView;

    /** */
    private ToggleButton mToggleButton;

    /** Local shared preferences*/
    private SharedPreferences myPreferences;

    /**
     * Creates the account information fragment and assigns all relevant listeners
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
            mPasswordResetButton = (Button) rootView.findViewById(R.id.account_password_reset);
            mEmailLayout = (RelativeLayout) rootView.findViewById(R.id.f_email_layout);

            //GET SHARED PREFERENCES~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            myPreferences = getActivity().getSharedPreferences(getString(R.string.user_info_main_key), Context.MODE_PRIVATE);
            String userEmail = myPreferences.getString(getString(R.string.saved_email_key), "");

            //SET USER FIELDS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            mUserEmailLabel.setText(userEmail);

            //SET ONCLICK LISTENERS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            mPasswordResetButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ForgotPasswordDialog dialog = ForgotPasswordDialog.newInstance();
                    dialog.show(getFragmentManager(), "forgotPW");
                }
            });

            mEmailLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageView lineView = (ImageView) getActivity().findViewById(R.id.f_email_line);
                    TextView answer = (TextView) getActivity().findViewById(R.id.f_account_email);
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

            return rootView;
        }

    /**
     * OnClickListener for change password button.
     */
    public void changePassword() {
        ForgotPasswordDialog dialog = ForgotPasswordDialog.newInstance();
        dialog.show(getFragmentManager(), "forgotPW");
    }
}
