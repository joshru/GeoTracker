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
import android.view.WindowManager;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mPassText = (EditText) findViewById(R.id.passphrase_text);
        mEmailText = (EditText) findViewById(R.id.email_text);
        final TextView mRegisterLabel = (TextView) findViewById(R.id.register_label);

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
     * Acting OnClickListener for the login button.
     * @param view
     */
    public void loginUser(View view) {
        String toastString = "";
        final Animation animAlpha = AnimationUtils.loadAnimation(this, R.anim.anim_alpha);
        final String emailCred = mEmailText.getText().toString();
        final String passCredHash = Authenticator.generateHash(mPassText.getText().toString());
        final String userEmail;
        final String userPassHash;
        final boolean emailForm = Authenticator.emailFormatCheck(emailCred);
        final boolean passForm = Authenticator.passFormatCheck(mPassText.getText().toString());

        //Get Shared Preferences
        SharedPreferences myPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        userEmail = myPreferences.getString("userEmail", "");
        userPassHash = myPreferences.getString("userPassphraseHash", "");

        view.startAnimation(animAlpha);
        if (emailForm && passForm && userEmail != null) {
            if ((emailCred.equals(userEmail)) && (passCredHash.equals(userPassHash))) {
                //Launch MainActivity, starting a new intent.
                Intent nextScreen = new Intent(this, MainActivity.class);
                //key,values to send to main.
                nextScreen.putExtra("email", emailCred);
                Log.e("pass", userPassHash);
                Log.e("pass", passCredHash);
                Log.e("d", emailCred + " succesfully logged in.");
                startActivity(nextScreen);

            } else {
                toastString = getString(R.string.bad_creds_toast);
            }
        } else if (!emailForm) {
            toastString = getString(R.string.bad_email_toast);
        } else if (!passForm) {
            toastString = getString(R.string.bad_pass_toast);
        }
        Toast.makeText(LoginActivity.this, toastString, Toast.LENGTH_LONG).show();
    }
}




