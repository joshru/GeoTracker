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
    private Button mPasswordResetButton;
    private RelativeLayout mAnswerLayout;

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
            View rootView = inflater.inflate(R.layout.fragment_account_settings, container, false);

            //GET REFERENCE TO VIEW FIELDS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            mUserEmailLabel = (TextView) rootView.findViewById(R.id.f_account_email);
            mUserQuestionLabel = (TextView) rootView.findViewById(R.id.f_account_question);
            mUserAnswerLabel = (TextView) rootView.findViewById(R.id.f_account_answer);
            mPasswordResetButton = (Button) rootView.findViewById(R.id.account_password_reset);
            mAnswerLayout = (RelativeLayout) rootView.findViewById(R.id.account_answer_layout);

            //GET SHARED PREFERENCES~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            SharedPreferences myPreferences = getActivity().getSharedPreferences(getString(R.string.user_info_main_key), Context.MODE_PRIVATE);
            String userEmail = myPreferences.getString(getString(R.string.saved_email_key), "");
            String userQuestion = myPreferences.getString(getString(R.string.saved_question_key), "");
            String userAnswer = myPreferences.getString(getString(R.string.saved_question_answer_key), "");

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
}
