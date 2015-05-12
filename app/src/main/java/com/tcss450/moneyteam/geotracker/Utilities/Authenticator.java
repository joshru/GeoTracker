package com.tcss450.moneyteam.geotracker.Utilities;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The authenticator class for hashing passwords and making sure entered emails are in a valid format.
 * @author Brandon Bell
 * @author Alexander Cherry
 * @author Joshua Rueschenberg
 */
public class Authenticator {

    /** A constant string used to add salting (inconsistency) to the hashing values. */
    private static final String SALT = "MONEY_TEAM_!";

    private Authenticator() {
        //prevent instantiation
    }

    /**
     * Hashes the entered password for security purposes.
     * @param input the entered password string
     * @return the hashed password string
     */
    public static String generateHash(final String input) {
        final String saltedPass = SALT + input;
        StringBuilder hash = new StringBuilder();

        try {
            MessageDigest SHA1 = MessageDigest.getInstance("SHA-1");
            byte[] hashedBytes = SHA1.digest(saltedPass.getBytes());
            char[] digits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                    'a', 'b', 'c', 'd', 'e', 'f' };
            for (int idx = 0; idx < hashedBytes.length;++idx) {
                byte b = hashedBytes[idx];
                hash.append(digits[(b & 0xf0) >> 4]);
                hash.append(digits[b & 0x0f]);
            }
        } catch (NoSuchAlgorithmException e) {
            // handle error here.
        }
        return hash.toString();
    }


    /**
     * Verifies that the email has proper qualities of an email.
     * @param theEmail the email string to be tested.
     * @return the boolean indicating if the email was valid succesful(true), or not(false).
     */
    public static boolean emailFormatCheck(final String theEmail) {
        boolean isValid = false;
        final String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        final CharSequence inputStr = theEmail;
        final Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    /**
     * Verifies that the password is at least 5 characters in length.
     * @param thePass to be verified with the regex.
     * @return the boolean regarding if it has a valid length
     */
    public static boolean passFormatCheck(final String thePass) {
        return (thePass.length() > 5);
    }
}
