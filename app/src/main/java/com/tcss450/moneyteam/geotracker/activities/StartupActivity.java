package com.tcss450.moneyteam.geotracker.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.tcss450.moneyteam.geotracker.R;

/**
 * Simple activity created to test whether or not the user is signed in and opens the appropriate activity.
 * @author Brandon Bell
 * @author Alexander Cherry
 * @author Joshua Rueschenberg
 */
public class StartupActivity extends Activity {

    /**
     * Creates the startup activity and decides what activity to display based on whether a user is
     * logged in or not
     * @param savedInstanceState the saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_startup);

        SharedPreferences sharedPrefs = getSharedPreferences(getString(R.string.shared_pref_key),
                Context.MODE_PRIVATE);

        final boolean loggedIn = sharedPrefs.getBoolean(getString(R.string.logged_in_boolean), false);
        Intent intent;
        //TODO check if mainActivity, loginActivity where destroyed before recreating
        if (loggedIn) {
            intent = new Intent(getApplicationContext(), MainActivity.class);
        } else {
            intent = new Intent(getApplicationContext(), LoginActivity.class);
        }
        startActivity(intent);
        finish();
    }


}
