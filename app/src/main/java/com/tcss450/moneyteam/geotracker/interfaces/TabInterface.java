package com.tcss450.moneyteam.geotracker.interfaces;

import android.location.Location;

import java.util.ArrayList;

/**
 * Created by Josh Rueschenberg on 5/22/2015.
 */
public interface TabInterface {

    /**
     * Set the current list of locations.
     *
     * @param theLocations new list of locations.
     */
    public void setLocations(ArrayList<Location> theLocations);

    /**
     * Gets the list of locations.
     *
     * @return the list of locations.
     */
    public ArrayList<Location> getLocations();

    /**
     * Gets the current user's email.
     * useful for displaying in UI elements.
     *
     * @return the email
     */
    public String getUserEmail();

    /**
     * ?
     *
     * @return
     */
    public int getLocationTimer();

    /**
     * ?
     *
     * @param minutes
     */
    public void setLocationTimer(final int minutes);

    /**
     * @return
     */
    public boolean getLocationBool();

    /**
     * Toggles location tracking.
     *
     * @param toggleEnabled
     */
    public void setLocationBool(final boolean toggleEnabled);

    /**
     * Gets the spinner position.
     *
     * @return the position
     */
    public int getSpinnerPosition();

    /**
     * Sets the spinner position.
     *
     * @param position
     */
    public void setSpinnerPosition(final int position);

    /**
     * ?
     */
    void requestListUpdate();

    int[] getUserRangeStart();

    int[] getUserRangeEnd();

    void setUserRange(final int[] start, final int[] end);
}


