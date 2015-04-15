package com.tcss450.moneyteam.geotracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.app.Activity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.tcss450.moneyteam.geotracker.R;


public class TermsFragment extends Fragment {
    private static Button mAgree;
    private static Button mRefuse;
    TermsListener registerActivity;

    // Interface that is passed to the main activity.
    public interface TermsListener {
        public void termsAgreed(boolean termsAccepted);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try{
            // Main Activity is casted as a TopSectionListener and is required to implement its methods
            // which consist of only createMeme()
            registerActivity = (TermsListener) activity;
        } catch(ClassCastException e) {
            throw new ClassCastException(activity.toString());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_terms, container, false);
        mAgree = (Button) view.findViewById(R.id.tos_accept_button);
        mRefuse = (Button) view.findViewById(R.id.tos_refuse_button);

        mAgree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerActivity.termsAgreed(true);
                terminateTermsFragment();
            }
        });

        mRefuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerActivity.termsAgreed(false);
                terminateTermsFragment();
            }
        });
        return view;
    }

    private void terminateTermsFragment() {
        //Launch MainActivity, starting a new intent.
        Intent nextScreen = new Intent(getActivity(), RegisterActivity.class);
        Log.e("d", "TOS accepted.");
        startActivity(nextScreen);
    }
}

