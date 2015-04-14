package com.tcss450.moneyteam.geotracker;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;

import android.app.Activity;

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

        mTermsButton = (Button) findViewById(R.id.frag_button);

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

        mTermsButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                TermsFragment termFrag = (TermsFragment) getSupportFragmentManager().findFragmentById(R.id.terms);
            }
        });

        //Direct user to registration activity.
        mRegisterLabel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }

    private boolean emailFormatCheck(final String theEmail) {
        return true;
    }

    private boolean passFormatCheck(final String thePassphrase) {
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}



