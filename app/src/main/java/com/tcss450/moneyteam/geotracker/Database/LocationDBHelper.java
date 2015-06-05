package com.tcss450.moneyteam.geotracker.Database;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.location.Location;
import android.util.Log;

import com.tcss450.moneyteam.geotracker.R;
import com.tcss450.moneyteam.geotracker.Utilities.WebServiceHelper;

/**
 * Class for adding location data to the database
 * @author Brandon Bell
 * @author Alexander Cherry
 * @author Joshua Rueschenberg
 */
public class LocationDBHelper extends SQLiteOpenHelper {

    private static final String LOCATION_DB_NAME = "locationdb.db";
    private static final int DATABASE_VERSION = 2;
    private static final String LOG_TAG = "locationdbhelper";

    private static final String INSERT = LocationTableSchema.INSERT
            + "values (?, ?, ?, ?, ?, ?);";

    private Context mContext;


    /**
     * Constructor for getting context and instantiating the helper
     * @param context
     */
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

    /**
     * Used for upgrading the Database
     * @param db the database to upgrade
     * @param oldVersion old version number
     * @param newVersion new version number
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        LocationTableSchema.onUpgrade(db, oldVersion, newVersion);
    }

    /**
     * Takes a location object and stores it in the database.
     * @param location to add
     * @return true if succeeded, false otherwise.
     */
    public boolean addLocation(Location location, long timestamp) {
    /*Double latitude, Double longitude, double speed, String direction,
                            String source, String TimeStamp) {*/
        //TODO consider using an SQLite insert statement instead of content values
        boolean success = true;
        Log.i("add location", location.toString());
        SQLiteDatabase db = this.getWritableDatabase();

        //trying insert statement instead
        SQLiteStatement insertStatement = db.compileStatement(INSERT);


        SharedPreferences sharedPreferences = mContext.getSharedPreferences(mContext.getString(R.string.shared_pref_key),
                Context.MODE_PRIVATE);
        String uid = sharedPreferences.getString(mContext.getString(R.string.saved_user_id_key),
                mContext.getString(R.string.default_restore_key));

        //CHECKS IF UID IS EMPTY
        if(uid.isEmpty()) {
            Log.d("addLocation Error", "addLocation Error: UID Empty");
            return false;
        }


        insertStatement.bindDouble(1, location.getLatitude());
        insertStatement.bindDouble(2, location.getLongitude());
        insertStatement.bindDouble(3, location.getSpeed());
        insertStatement.bindDouble(4, location.getBearing());
        insertStatement.bindString(5, uid);
        insertStatement.bindLong(6, timestamp);


        final long newRowID = insertStatement.executeInsert();
        if (newRowID == -1) {
            success = false;
            Log.d("INSERTTODB", "Insert failed");
        }

        db.close();
        Log.d("Insert success", "insert into db successful");
        return success;


    }

    /**
     * Gets a cursor containing a reference to all of the location data.
     * @return a cursor
     */
    public Cursor selectAllLocations() {
        //TODO return a cursor pointing to the whole table, minus the id (which may or may not even be needed)

        Cursor result = null;
        SQLiteDatabase db = this.getReadableDatabase();

        result = db.query(LocationTableSchema.TABLE_NAME, LocationTableSchema.FIELDS,
                null, null, null, null, LocationTableSchema.COLUMN_TIMESTAMP + " ASC");
        Log.d(LOG_TAG, "Cursor created");
        if (result != null && result.moveToFirst()) {
            return  result;
        }
        return result;
    }

    /**
     * Pushes all the points to the webservice
     * @return boolean based on the success of the push
     */
    public boolean pushPointsToServer() {
        boolean success = false;
        Cursor cursor = selectAllLocations();
        int i = 1;
        if (cursor.moveToFirst()) {

            do {
                WebServiceHelper webServiceHelper = new WebServiceHelper(mContext);
                Log.d("Logging Loop", " Pushed location #" + i++
                        + " with UID: " + cursor.getString(4));
                webServiceHelper.logPoint(cursor);

            } while (cursor.moveToNext());
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        //Log.d("DELETING ENTRIES", "Deleting entries now...");
        SQLiteDatabase db = getWritableDatabase();
        db.delete(LocationTableSchema.TABLE_NAME, null, null);
         Log.d("DBDELETE", "Database deleted. Points pushed to server.");//remove entries from local database
        db.close();

        return success;
    }

    /**
     * Deprecated!
     * Gets the locations in the user specified range using a cursor
     * @param startTime the start date in the range
     * @param endTime the end date in the range
     * @return the Cursor
     */
    public Cursor getLocationsInRange(long startTime, long endTime) {
        Cursor cursor;
        SQLiteDatabase db = getReadableDatabase();

        cursor = db.query(LocationTableSchema.TABLE_NAME, LocationTableSchema.FIELDS,
                LocationTableSchema.COLUMN_TIMESTAMP + " BETWEEN ? AND ?",
                new String[] {""+startTime,""+ endTime}, null,
                null, null, null);

        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        return cursor;
    }

    /**
     * Converts degrees to a compass direction
     * Wasn't needed QQ
     *
     * Thanks to stack overflow for this elegant solution.
     * @param degrees used to determine direction
     * @return string representing a direction on a compass
     */
    private String convDegToDir(double degrees) {
        String[] dirTags = {"N","NNE","NE","ENE","E","ESE", "SE", "SSE","S","SSW","SW","WSW","W","WNW","NW","NNW"};
        int value = (int) ((degrees /22.5) + 5);
        return dirTags[(value % 16)];
    }

}
