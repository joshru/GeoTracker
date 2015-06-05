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

        final String loggedIn = sharedPrefs.getString(getString(R.string.logged_in_activity), "");
        Intent intent;
        if (loggedIn != null && loggedIn.equals("main")) {
            intent = new Intent(getApplicationContext(), MainActivity.class);
        } else {
            intent = new Intent(getApplicationContext(), LoginActivity.class);
        }
        startActivity(intent);
        finish();
    }


}
