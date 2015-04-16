package com.tcss450.moneyteam.geotracker;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Alex on 4/16/2015.
 */
public class Authenticator {

    private static final String SALT = "MONEY_TEAM_!";

    public static String generateHash(final String input) {
        final String saltedPass = SALT + input;
        StringBuilder hash = new StringBuilder();

        try {
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            byte[] hashedBytes = sha.digest(saltedPass.getBytes());
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
     * @return the boolean indicating if its succesful(true), or not(false).
     */
    public static boolean emailFormatCheck(final String theEmail) {
        boolean testBool = (theEmail.contains("@")) && (theEmail.contains(".")) && (theEmail.length() > 4);
        return testBool;
    }

    /**
     * Verifies that the password contains proper values.
     * <ul>
     *     <li>Must be minimum 8 characters in length.</li>
     *     <li>Must contain minimum 1, alphabetic character.</li>
     *     <li>Must contain minimum 1, numeric character.</li>
     *     <li>Must contain minimum 1, special character (@#$%^&+=).</li>
     * </ul>
     * @param thePass to be verified with the regex.
     * @return the boolean regarding if it has a valid form
     */
    public static boolean passFormatCheck(final String thePass) {
        final String passPattern = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}";
        return thePass.matches(passPattern);
    }
}
