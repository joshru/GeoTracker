package com.tcss450.moneyteam.geotracker.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.tcss450.moneyteam.geotracker.R;

/**
 * Simple activity created to test whether or not the user is signed in and open the appropriate activity.
 */
public class StartupActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_startup);

        SharedPreferences sharedPrefs = getSharedPreferences(getString(R.string.shared_pref_key),
                Context.MODE_PRIVATE);

        final boolean loggedIn = sharedPrefs.getBoolean(getString(R.string.logged_in_boolean), false);
        Intent intent;
        if (loggedIn) {
            intent = new Intent(getApplicationContext(), MainActivity.class);
        } else {
            intent = new Intent(getApplicationContext(), LoginActivity.class);
        }
        startActivity(intent);
        finish();
    }


}
