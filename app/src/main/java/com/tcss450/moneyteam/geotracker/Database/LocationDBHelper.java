package com.tcss450.moneyteam.geotracker.Database;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;
import android.util.Log;

import com.tcss450.moneyteam.geotracker.R;
import com.tcss450.moneyteam.geotracker.Utilities.WebServiceHelper;

/**
 * Created by Brandon on 5/3/2015.
 */
public class LocationDBHelper extends SQLiteOpenHelper {

    private static final String LOCATION_DB_NAME = "locationdb.db";
    private static final int DATABASE_VERSION = 1;
    private static final String LOG_TAG = "locationdbhelper";

    private Context mContext;




    public LocationDBHelper(Context context) {
        super(context, LOCATION_DB_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    /**
     * Callback method to create the database.
     * @param db to be opened
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        LocationTableSchema.onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        LocationTableSchema.onUpgrade(db, oldVersion, newVersion);
    }

    /**
     * Takes a location object and stores it in the database.
     * @param location to add
     * @return true if succeeded, false otherwise.
     */
    public boolean addLocation(Location location) {
    /*Double latitude, Double longitude, double speed, String direction,
                            String source, String TimeStamp) {*/
        //TODO consider using an SQLite insert statement instead of content values
        boolean success = false;

        SQLiteDatabase db = this.getWritableDatabase();

        SharedPreferences sharedPreferences = mContext.getSharedPreferences(mContext.getString(R.string.shared_pref_key),
                Context.MODE_PRIVATE);
        String uid = sharedPreferences.getString(mContext.getString(R.string.saved_user_id_key),
                mContext.getString(R.string.default_restore_key));
        ContentValues values = new ContentValues();

        //---------Bundle up all the values----------
        values.put(LocationTableSchema.COLUMN_LATITUDE, location.getLatitude());
        values.put(LocationTableSchema.COLUMN_LONGITUDE, location.getLongitude());
        values.put(LocationTableSchema.COLUMN_SPEED, location.getSpeed());
        values.put(LocationTableSchema.COLUMN_HEADING, location.getBearing());
        values.put(LocationTableSchema.COLUMN_SOURCE, uid);
        values.put(LocationTableSchema.COLUMN_TIMESTAMP, location.getTime());

        //insert them
        final long id = db.insert(LocationTableSchema.TABLE_NAME, null, values);//test

        if (id > -1) {
            success = true;
            Log.d(LOG_TAG, "Insert successful");
        }
        db.close();
        return success;


    }

    /**
     * Gets a cursor containing a reference to all of the location data.
     * @return a cursor
     */
    public Cursor selectAllLocations() {
        //TODO return a cursor pointing to the whole table, minus the id (which may or may not even be needed)
        //Use this
        Cursor result = null;
        SQLiteDatabase db = this.getReadableDatabase();

        result = db.query(LocationTableSchema.TABLE_NAME, LocationTableSchema.FIELDS,
                null, null, null, null, null);
        Log.d(LOG_TAG, "Cursor created");
        if (result != null && result.moveToFirst()) {
            return  result;
        }
        return result;
    }

    public boolean pushPointsToServer() {
        boolean success = false;
        WebServiceHelper webServiceHelper = new WebServiceHelper(mContext);

        Cursor cursor = selectAllLocations();

        if (cursor.moveToFirst()) {

            do {
                webServiceHelper.logPoint(cursor);

            } while (cursor.moveToNext());

        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        return success;
    }

    /**
     * Converts degrees to a compass direction
     *
     * Thanks to stack overflow for this elegant solution.
     * @param degrees
     * @return
     */
    private String convDegToDir(double degrees) {
        String[] dirTags = {"N","NNE","NE","ENE","E","ESE", "SE", "SSE","S","SSW","SW","WSW","W","WNW","NW","NNW"};
        int value = (int) ((degrees /22.5) + 5);
        return dirTags[(value % 16)];
    }

}
