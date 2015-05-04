package com.tcss450.moneyteam.geotracker.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Brandon on 5/3/2015.
 */
public class LocationDBHelper extends SQLiteOpenHelper {

    private static final String LOCATION_DB_NAME = "locationdb.db";
    private static final int DATABASE_VERSION = 1;



    public LocationDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, LOCATION_DB_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        LocationTableSchema.onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        LocationTableSchema.onUpgrade(db, oldVersion, newVersion);
    }
}
