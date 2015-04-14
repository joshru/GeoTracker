package com.tcss450.moneyteam.geotracker;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;

import android.app.Activity;

//import android.app.FragmentTransaction;
import android.app.Fragment;
import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.animation.Animation;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
//import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentActivity;
//import android.app.FragmentTransaction;


public class LoginActivity extends Activity {

    private EditText mPassText;
    private EditText mEmailText;
    private Button mLoginButton;
    private TextView mRegisterLabel;

    private Button mTermsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mPassText = (EditText) findViewById(R.id.passphrase_text);
        mEmailText = (EditText) findViewById(R.id.email_text);
        mLoginButton = (Button) findViewById(R.id.login_button);
        mRegisterLabel = (TextView) findViewById(R.id.register_label);

        final Animation animAlpha  = AnimationUtils.loadAnimation(this, R.anim.anim_alpha);

        //Verify credentials format, then legitimacy.
        mLoginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                final String emailCred = mEmailText.getText().toString();
                final String passCred = mPassText.getText().toString();
                v.startAnimation(animAlpha);
                if(emailFormatCheck(emailCred) && passFormatCheck(passCred)) {
                    if(emailCred.equals("valid@email.com")) {
                        //Launch MainActivity
                        //Starting a new Intent
                        Intent nextScreen = new Intent(getApplicationContext(), MainActivity.class);
                        //key,values to send to main.
                        nextScreen.putExtra("email", emailCred);
                        Log.e("d", emailCred + " succesfully logged in.");
                        startActivity(nextScreen);

                    }else{
                        Toast failedLoginToast = Toast.makeText(LoginActivity.this, "Invalid Credentials: If you are a new user, please register using the link provided.", Toast.LENGTH_LONG);
                        failedLoginToast.setGravity(Gravity.CENTER, 0, 0);
                        failedLoginToast.show();
                    }
                };
            }
        });

        mRegisterLabel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentActivity FA = new FragmentActivity();

                TermsFragment tFrag = new TermsFragment();
                FragmentTransaction trans = FA.getSupportFragmentManager().beginTransaction();
                trans.add(R.id.fragment_container, tFrag);
                trans.commit();

//                tFrag.setArguments(getIntent().getExtras());
//                FA.getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, tFrag).commit();



            }
        });
    }

    private boolean emailFormatCheck(final String theEmail) {
        return true;
    }

    private boolean passFormatCheck(final String thePassphrase) {
        return true;
    }
}



