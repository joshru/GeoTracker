package com.tcss450.moneyteam.geotracker;

/**
 * Created by Josh on 4/14/2015.
 */
public class UserEvent {
    public User mUser;

    public UserEvent(final User theUser) {
        mUser = theUser;
    }

    public User getUser() {
        return mUser;
    }
}
