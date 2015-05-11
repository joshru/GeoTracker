package com.tcss450.moneyteam.geotracker.Database;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Class used to represent the user table, holding all constants needed
 * for its creation and access.
 *
 * @author Brandon Bell
 * @author Alexander Cherry
 * @author Joshua Rueschenberg
 */
public class UserTable {

    //Database table tags
    public static final String Table_USER = "user_table";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_ADDRESS = "email_address";
    public static final String COLUMN_PASSWORD = "user_password";
    public static final String COLUMN_SECURITY_QUESTION = "security_question";
    public static final String COLUMN_SECURITY_ANSWER = "security_answer";

    //Database creation statement
    private static final String DATABASE_CREATE = "create table "
            + Table_USER
            + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_ADDRESS + " text not null, "
            + COLUMN_PASSWORD + " text not null, "
            + COLUMN_SECURITY_QUESTION + " text not null, "
            + COLUMN_SECURITY_ANSWER + " text not null "
            + ");";

    /**
     * Creates the SQLite database
     * @param database
     */
    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    /**
     * Used for upgrading the database
     * @param database the database to upgrade
     * @param oldVerion the old version number
     * @param newVersion the new version number
     */
    public static void onUpgrade(SQLiteDatabase database, int oldVerion,
                                 int newVersion) {
        Log.w(UserTable.class.getName(), "Upgrading database from version " +
        oldVerion + "to " + newVersion +
        ", which will destroy all old data");

        database.execSQL("DROP TABLE IF EXISTS " + Table_USER);
        onCreate(database);
    }
}
