package com.tcss450.moneyteam.geotracker.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.tcss450.moneyteam.geotracker.R;
import com.tcss450.moneyteam.geotracker.Utilities.Poptart;

public class MapsActivity extends Activity {
    public GoogleMap mMap;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_map);

        createMap();
    }

    private void createMap() {
        try {
            if (mMap == null) {
                mMap = ((MapFragment) getFragmentManager()
                        .findFragmentById(R.id.fragment_map)).getMap();

                if (mMap == null) {
                    Poptart.display(getApplicationContext(), "The Map dun goofed bud", Toast.LENGTH_LONG);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onResume() {
        super.onResume();
        createMap();
    }


}
