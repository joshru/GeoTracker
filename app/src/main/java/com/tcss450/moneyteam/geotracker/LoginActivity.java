package com.tcss450.moneyteam.geotracker;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;

import android.app.Activity;

//import android.app.FragmentTransaction;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.animation.Animation;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import android.support.v4.app.FragmentActivity;
//import android.app.FragmentTransaction;
import android.support.v4.app.FragmentTransaction;

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
                final boolean emailForm = emailFormatCheck(emailCred);
                final boolean passForm = passFormatCheck(passCred);
                v.startAnimation(animAlpha);
                if(emailForm && passForm) {
                    if(emailCred.equals("valid@email.com")) {
                        //Launch MainActivity, starting a new intent.
                        Intent nextScreen = new Intent(getApplicationContext(), MainActivity.class);
                        //key,values to send to main.
                        nextScreen.putExtra("email", emailCred);
                        Log.e("d", emailCred + " succesfully logged in.");
                        startActivity(nextScreen);

                    }else{
                        Toast failedLoginToast = Toast.makeText(LoginActivity.this, getString(R.string.bad_creds_toast), Toast.LENGTH_LONG);
                        failedLoginToast.show();
                    }
                } else if(!emailForm) {
                    Toast.makeText(LoginActivity.this, getString(R.string.bad_email_toast), Toast.LENGTH_LONG).show();
                } else if(!passForm) {
                    Toast.makeText(LoginActivity.this, getString(R.string.bad_pass_toast), Toast.LENGTH_LONG).show();
                }

            }
        });

        mRegisterLabel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentActivity FA = new FragmentActivity();
                FA.setContentView(R.layout.fragment_terms);

                TermsFragment tFrag = new TermsFragment();
                tFrag.setArguments(getIntent().getExtras());
                FA.getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, tFrag).commit();
            }
        });
    }

    /**
     * Verifies that the email has proper qualities of an email.
     * @param theEmail the email string to be tested.
     * @return the boolean indicating if its succesful(true), or not(false).
     */
    private boolean emailFormatCheck(final String theEmail) {
        boolean testBool = (theEmail.contains("@")) && (theEmail.contains(".")) && (theEmail.length() < 6);
        return testBool;
    }

    private boolean passFormatCheck(final String thePassphrase) {
        return true;
    }
}



