package com.tcss450.moneyteam.geotracker.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Used by us to store and retrieve information about the user.
 * Created by Brandon on 4/21/2015.
 */
public class UserDB {

    private UserDBHelper mDBHelper;
    private Context mContext;
    //private SQLiteDatabase db;

    /**
     *
     * @param context
     */
    public UserDB(Context context) {
        mDBHelper = new UserDBHelper(context);
        mContext = context;
    }

    /**
     *
     * @param ID
     * @param userName
     * @param password
     * @param securityQuestion
     * @param securityAnswer
     * @return
     */
    public long insertUser(int ID, String userName, String password,
               String securityQuestion, String securityAnswer) {
        SQLiteDatabase db = mDBHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(UserTable.COLUMN_ID, ID);
        values.put(UserTable.COLUMN_ADDRESS, userName);
        values.put(UserTable.COLUMN_PASSWORD, password);
        values.put(UserTable.COLUMN_SECURITY_QUESTION, securityQuestion);
        values.put(UserTable.COLUMN_SECURITY_ANSWER, securityAnswer);

        //long newRowId;

        return db.insert(UserTable.Table_USER,
                null,
                values);

    }

    /**
     * Method to used to change user info.
     * @param userID user id
     * @param tag column to search for.
     * @param newValue new value to update the user information to.
     * @return
     */
    public boolean updateUserInfo(int userID, String tag, String newValue) {
        SQLiteDatabase db = mDBHelper.getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put(tag, newValue);

        //which row to update
        String selection = UserTable.COLUMN_ID + " LIKE ?";
        //^ essentially telling query to find rows with an ID 'LIKE ?'
        String[] selectionArgs = { String.valueOf(userID)};
        // where the '?' will be replaced by selection args, which contains a string
        // specifying the specific user id to look for.

        int count = db.update(UserTable.Table_USER,
                values,
                selection,
                selectionArgs);
        if (count > 0) {
            return true; //success
        }
        return false;

    }

    /**
     * Gets a specific piece of information about a given user.
     * @param ID user id to search for
     * @param tag constant from UserTable specifying the column to grab
     * @return Cursor holding the requested information.
     */
        public Cursor getUserInfo(int ID, String tag) {
            SQLiteDatabase db = mDBHelper.getReadableDatabase();

            String[] projection = {
                   // Integer.toString(ID),
                    tag
            };

            String[] selectionArgs = {
                    Long.toString(ID)
            };

            Cursor c = db.query(UserTable.Table_USER,
                    projection,
                    UserTable.COLUMN_ID + "=?",
                    selectionArgs,
                    null,
                    null,
                    null);
            return c;
        }





}
