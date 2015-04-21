package com.tcss450.moneyteam.geotracker.fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tcss450.moneyteam.geotracker.R;

/**
 * TODO
 * @author Alexander Cherry(akac92@uw.edu)
 */
public class AccountFragment extends Fragment {
    /** TODO */
    private TextView mUserEmailLabel;
    private TextView mUserAnswerLabel;
    private TextView mUserQuestionLabel;

    /**
     * TODO
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
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

            //GET SHARED PREFERENCES~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            SharedPreferences myPreferences = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
            String userEmail = myPreferences.getString(getString(R.string.saved_email_key), "");
            String userQuestion = myPreferences.getString(getString(R.string.saved_question_key), "");
            String userAnswer = myPreferences.getString(getString(R.string.saved_question_answer_key), "");

            //SET USER FIELDS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            mUserEmailLabel.setText(userEmail);
            mUserQuestionLabel.setText(userQuestion);
            mUserAnswerLabel.setText(userAnswer);

            //SET ONCLICK LISTENERS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            return rootView;
        }
}
