package com.tcss450.moneyteam.geotracker.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * User database helper
 * Created by Brandon on 4/21/2015.
 */
public class UserDBHelper extends SQLiteOpenHelper {

    //Static constants
    private static final String DATABASE_NAME = "usertable.db";
    private static final int DATABASE_VERSION = 1;
    //private static final String USER_TABLE = "userTable";


    public UserDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /*private Context context;
    private SQLiteDatabase db;*/



    @Override
    public void onCreate(SQLiteDatabase db) {
        UserTable.onCreate(db);

    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        UserTable.onUpgrade(db, oldVersion, newVersion);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        UserTable.onUpgrade(db, oldVersion, newVersion);
    }

}
