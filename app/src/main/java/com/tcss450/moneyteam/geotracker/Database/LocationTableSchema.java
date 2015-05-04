package com.tcss450.moneyteam.geotracker.Database;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by Brandon on 5/3/2015.
 */
public class LocationTableSchema {

    /*Defining table and column names.*/
    private static final String TABLE_NAME = "tablelocation";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_LATITUDE = "Latitude";
    private static final String COLUMN_LONGITUDE = "Longitude";
    private static final String COLUMN_SPEED = "Speed";
    private static final String COLUMN_HEADING = "Heading";
    private static final String COLUMN_SOURCE = "Source";
    private static final String COLUMN_TIMESTAMP = "Timestamp";

    /*Create table statement*/

    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
            + COLUMN_ID + "INTEGER AUTOINCREMENT, " + COLUMN_LATITUDE + " DOUBLE NOT NULL, "
            + COLUMN_LONGITUDE + "DOUBLE NOT NULL, " + COLUMN_SPEED + "INTEGER NOT NULL, "
            + COLUMN_HEADING + "TEXT NOT NULL, " + COLUMN_SOURCE + "INTEGER UNIQUE, "
            + COLUMN_TIMESTAMP + "TEXT PRIMARY KEY NOT NULL);";

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
