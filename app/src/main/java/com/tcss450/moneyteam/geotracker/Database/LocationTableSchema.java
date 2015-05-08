package com.tcss450.moneyteam.geotracker.Database;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by Brandon on 5/3/2015.
 */
public class LocationTableSchema {

    /*Defining table and column names.*/
    public static final String TABLE_NAME = "tablelocation";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_LATITUDE = "Latitude";
    public static final String COLUMN_LONGITUDE = "Longitude";
    public static final String COLUMN_SPEED = "Speed";
    public static final String COLUMN_HEADING = "Heading";
    public static final String COLUMN_SOURCE = "Source";
    public static final String COLUMN_TIMESTAMP = "Timestamp";

    public static final String[] FIELDS = new String[] {
       COLUMN_ID, COLUMN_LATITUDE, COLUMN_LONGITUDE, COLUMN_SPEED,
            COLUMN_HEADING, COLUMN_SOURCE, COLUMN_TIMESTAMP
    } ;

    /*Create table statement*/

    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
            + COLUMN_ID + "INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_LATITUDE + " DOUBLE NOT NULL, "
            + COLUMN_LONGITUDE + "DOUBLE NOT NULL, " + COLUMN_SPEED + "INTEGER NOT NULL, "
            + COLUMN_HEADING + "DOUBLE NOT NULL, " + COLUMN_SOURCE + "TEXT UNIQUE NOT NULL, "
            + COLUMN_TIMESTAMP + "INTEGER NOT NULL);";

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(CREATE_TABLE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {
        Log.w(UserTable.class.getName(), "Upgrading database from version " +
                oldVersion + "to " + newVersion +
                ", which will destroy all old data");

        database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(database);
    }
}
