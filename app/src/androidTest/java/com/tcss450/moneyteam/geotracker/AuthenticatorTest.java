package com.tcss450.moneyteam.geotracker;

import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;

/**
 * Created by Brandon on 5/21/2015.
 */
public class AuthenticatorTest extends ActivityInstrumentationTestCase2 {

    private Solo mSolo;

    public AuthenticatorTest(Class activityClass) {
        super(activityClass);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mSolo = new Solo(getInstrumentation(), getActivity());
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }

}
