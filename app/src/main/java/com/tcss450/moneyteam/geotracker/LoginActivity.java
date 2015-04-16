package com.tcss450.moneyteam.geotracker;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;

import android.app.Activity;

//import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

public class LoginActivity extends FragmentActivity {

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
                final String passCredHash = Authenticator.generateHash(mPassText.getText().toString());
                final String userEmail;
                final String userPassHash;
                final boolean emailForm = emailFormatCheck(emailCred);
                final boolean passForm = passFormatCheck(passCredHash);

                //Get Shared Preferences
                SharedPreferences myPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                userEmail = myPreferences.getString("userEmail", "");
                userPassHash = myPreferences.getString("userPassphraseHash", "");

                v.startAnimation(animAlpha);
                if(emailForm && passForm && userEmail != null) {
                    if((emailCred.equals(userEmail)) &&(passCredHash.equals(userPassHash))) {
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
                //Launch Register Activity, starting a new intent.
                Intent nextScreen = new Intent(getApplicationContext(), RegisterActivity.class);
                //key,values to send to main.
                //Take this
                nextScreen.putExtra("email", mEmailText.getText().toString());
                Log.e("d", "Changed to RegisterActivity");
                startActivity(nextScreen);
            }
        });
    }

    /**
     * Verifies that the email has proper qualities of an email.
     * @param theEmail the email string to be tested.
     * @return the boolean indicating if its succesful(true), or not(false).
     */
    private boolean emailFormatCheck(final String theEmail) {
        boolean testBool = (theEmail.contains("@")) && (theEmail.contains(".")) && (theEmail.length() > 4);
        return testBool;
    }

    /**
     * Verifies that the password contains proper values.
     * <ul>
     *     <li>Must be minimum 8 characters in length.</li>
     *     <li>Must contain minimum 1, alphabetic character.</li>
     *     <li>Must contain minimum 1, numeric character.</li>
     *     <li>Must contain minimum 1, special character (@#$%^&+=).</li>
     * </ul>
     * @param thePass to be verified with the regex.
     * @return the boolean regarding if it has a valid form
     */
    private boolean passFormatCheck(final String thePass) {
        final String passPattern = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}";
        return thePass.matches(passPattern);
    }
}



