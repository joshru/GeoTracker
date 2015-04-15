package com.tcss450.moneyteam.geotracker;

/**
 * Created by Josh on 4/14/2015.
 */
public class User {
    public String mEmail;

    public String mPassword;

    public String mQuestion;

    private String mAnswer;

    //TODO add field for location data and such.

    public User(final String email, final String password, final String question,
                final String answer) {
        mEmail = email;
        mPassword = password;
        mQuestion = question;
        mAnswer = answer;
    }


    public String getmEmail() {
        return mEmail;
    }

    public void setmEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public String getmPassword(final String answer) {
        String result = "";
        if (answer.equals(mAnswer)) {
            result = mPassword;
        } else {
            result = "Wrong answer. Sucks for you";
        }

        return result;
    }

    public void setmPassword(String mPassword) {
        this.mPassword = mPassword;
    }

    public String getmQuestion() {
        return mQuestion;
    }

    public void setmQuestion(String mQuestion) {
        this.mQuestion = mQuestion;
    }
}
