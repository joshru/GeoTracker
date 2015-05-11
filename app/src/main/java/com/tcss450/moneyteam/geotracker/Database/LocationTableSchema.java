package com.tcss450.moneyteam.geotracker.Database;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Schema class for the table of location data
 * @author Brandon Bell
 * @author Alexander Cherry
 * @author Joshua Rueschenberg
 */
public class LocationTableSchema {

    /**Defining table and column names.*/
    public static final String TABLE_NAME = "tablelocationtest";

    /** Column id*/
    public static final String COLUMN_ID = "_id";

    /** Latitude label*/
    public static final String COLUMN_LATITUDE = "Latitude";

    /** Longitude label*/
    public static final String COLUMN_LONGITUDE = "Longitude";

    /** Speed label*/
    public static final String COLUMN_SPEED = "Speed";

    /** Heading label*/
    public static final String COLUMN_HEADING = "Heading";

    /** Source label*/
    public static final String COLUMN_SOURCE = "Source";

    /** Timestamp label*/
    public static final String COLUMN_TIMESTAMP = "Timestamp";

    /** String array of field tags*/
    public static final String[] FIELDS = new String[] {
       COLUMN_LATITUDE, COLUMN_LONGITUDE, COLUMN_SPEED,
            COLUMN_HEADING, COLUMN_SOURCE, COLUMN_TIMESTAMP
    } ;

    /** Create table statement*/
    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
            + COLUMN_ID + " INTEGER PRIMARY KEY, " + COLUMN_LATITUDE + " DOUBLE NOT NULL, "
            + COLUMN_LONGITUDE + " DOUBLE NOT NULL, " + COLUMN_SPEED + " FLOAT NOT NULL, "
            + COLUMN_HEADING + " DOUBLE NOT NULL, " + COLUMN_SOURCE + " TEXT NOT NULL, "
            + COLUMN_TIMESTAMP + " INTEGER NOT NULL);";

    /** Insert string*/
    public static final String INSERT = "INSERT INTO " + TABLE_NAME + " ("
            + COLUMN_LATITUDE + ", "
            + COLUMN_LONGITUDE + ", "
            + COLUMN_SPEED + ", "
            + COLUMN_HEADING + ", "
            + COLUMN_SOURCE + ", "
            + COLUMN_TIMESTAMP + ") ";

    /**
     * Creates the SQLite database
     * @param database
     */
    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(CREATE_TABLE);
    }

    /**
     * Used for upgrading the database
     * @param database the database to upgrade
     * @param oldVersion the old version number
     * @param newVersion the new version number
     */
    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {
        Log.w(UserTable.class.getName(), "Upgrading database from version " +
                oldVersion + "to " + newVersion +
                ", which will destroy all old data");

        database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(database);
    }
}
