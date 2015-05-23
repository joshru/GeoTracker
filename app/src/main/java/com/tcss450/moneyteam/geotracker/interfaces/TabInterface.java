package com.tcss450.moneyteam.geotracker.interfaces;

import android.location.Location;

import java.util.ArrayList;

/**
 * Created by Josh Rueschenberg on 5/22/2015.
 */
public interface TabInterface {

    public void setLocations(ArrayList<Location> theLocations);

    public ArrayList<Location> getLocations();

    public String getUserEmail();

    public int getLocationTimer();

    public void setLocationTimer(final int minutes);

    public boolean getLocationBool();

    public void setLocationBool(final boolean toggleEnabled);

    void requestListUpdate();
}
