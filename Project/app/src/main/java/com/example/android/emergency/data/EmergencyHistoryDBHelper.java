package com.example.android.emergency.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.emergency.data.EmergencyHistoryContract.EmergencyHistoryEntry;

/**
 * Created by Ruben on 13/04/2018.
 */

public class EmergencyHistoryDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "emergencyHistory.db";
    private static final int DATABASE_VERSION = 1;

    public EmergencyHistoryDBHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_EMERGENCYHISTORY_TABLE = "CREATE TABLE " + EmergencyHistoryEntry.TABLE_NAME + " (" +
                EmergencyHistoryEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                EmergencyHistoryEntry.COLUMN_DATE + " TEXT NOT NULL, " +
                EmergencyHistoryEntry.COLUMN_LOCATION + " TEXT NOT NULL" +
                "); ";
        sqLiteDatabase.execSQL(SQL_CREATE_EMERGENCYHISTORY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + EmergencyHistoryEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
