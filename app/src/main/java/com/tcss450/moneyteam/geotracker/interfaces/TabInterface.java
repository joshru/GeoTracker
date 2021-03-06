package com.tcss450.moneyteam.geotracker.interfaces;


import android.app.Fragment;
import android.location.Location;


import java.util.ArrayList;

/**
 * Interface for tab fragments
 * @author Brandon Bell
 * @author Alexander Cherry
 * @author Joshua Rueschenberg
 */
public interface TabInterface {

    /**
     * Set the current list of locations.
     *
     * @param theLocations new list of locations.
     */
    void setLocations(ArrayList<Location> theLocations);

    public void setGlobalFragment(Fragment current);
    /**
     * Gets the list of locations.
     *
     * @return the list of locations.
     */
    ArrayList<Location> getLocations();

    /**
     * Gets the current user's email.
     * useful for displaying in UI elements.
     *
     * @return the email
     */
     String getUserEmail();

    /**
     * ?
     *
     * @return
     */
     int getLocationTimer();

    /**
     * ?
     *
     * @param minutes
     */
     void setLocationTimer(final int minutes);

    /**
     * @return
     */
     boolean getLocationBool();

    /**
     * Toggles location tracking.
     *
     * @param toggleEnabled
     */
     void setLocationBool(final boolean toggleEnabled);

    /**
     * Gets the spinner position.
     *
     * @return the position
     */
     int getSpinnerPosition();

    /**
     * Sets the spinner position.
     *
     * @param position
     */
     void setSpinnerPosition(final int position);

    /**
     * ?
     */
    void requestListUpdate();

    int[] getUserRangeStart();

    int[] getUserRangeEnd();

    void setUserRange(final int[] start, final int[] end);

    void setPrevStartDate(String day);
    void setPrevEndDate(String date);
    void setPrevStartTime(String time);
    void setPrevEndTime(String time);

    String[] getPrevStartDate();
    String[] getPrevEndDate();

}


