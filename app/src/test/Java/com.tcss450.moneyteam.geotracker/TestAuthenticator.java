package com.tcss450.moneyteam.geotracker;

import com.tcss450.moneyteam.geotracker.Utilities.Authenticator;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;

import static junit.framework.Assert.assertTrue;


public class TestAuthenticator {

    /**
     * Runs before tests, nothing to set up here though.
     * Kept for completeness
     */
    @Before
    public void setUp() {
        //nothing to do, static class
    }

    /**
     * Runs after tests, nothing to tear down as
     * the authenticator uses static methods
     */
    @After
    public void tearDown() {
        //Nothing to do, no resources to free.
    }

    /**
     * Tests the email format checker method.
     * Makes sure invalid emails aren't accepted and
     * valid are recognized as valid.
     */
    @Test
    public void testEmailFormat() {
        final String badEmail = "bademailatemail.com";
        final String goodEmail = "goodemail@email.com";

        assertFalse("Should not return true for false email addresses.",
                Authenticator.emailFormatCheck(badEmail));

        assertTrue("Should return true for valid email addresses.",
                Authenticator.emailFormatCheck(goodEmail));
    }

    /**
     * Tests our hashing method.
     * Makes sure that the hash is, in fact different from the original password and
     * makes sure that generating a hash for the same password twice will yield
     * the same result.
     */
    @Test
    public void testHash() {
        final String passwordToHash = "iamavalidpassword4";
        final String alternateHash  = "iamadifferentpassword8";
        final String hashedPassword = Authenticator.generateHash(passwordToHash);

        assertFalse("Hash should not be the same as original",
                passwordToHash.equals(hashedPassword));

        assertEquals("Hash should return the same string for the same password",
                Authenticator.generateHash(passwordToHash),
                Authenticator.generateHash(passwordToHash));
        assertFalse("Hash should be different for different passwords.",
                Authenticator.generateHash(passwordToHash)
                .equals(Authenticator.generateHash(alternateHash))
        );
    }

    /**
     * Tests the password format checker. It only tests to make
     * sure the lengths is > 5 so nothing too special here.
     */
    @Test
    public void testPassFormat() {
        final String badPass = "2shrt";
        final String goodPass = "longenough";

        assertFalse("Password should be > 5 characters",
                Authenticator.passFormatCheck(badPass));

        assertTrue("Password should be > 5 characters",
                Authenticator.passFormatCheck(goodPass));


    }

}