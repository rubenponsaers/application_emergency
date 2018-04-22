package com.example.android.emergency.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.emergency.data.EmergenciesContract.EmergenciesEntry;

/**
 * Created by Ruben on 13/04/2018.
 */

public class EmergenciesDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "emergencies.db";
    private static final int DATABASE_VERSION = 4;

    public EmergenciesDBHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_EMERGENCIES_TABLE = "CREATE TABLE " + EmergenciesEntry.TABLE_NAME + " (" +
                EmergenciesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                EmergenciesEntry.COLUMN_DATE + " TEXT NOT NULL, " +
                EmergenciesEntry.COLUMN_TIME + " TEXT NOT NULL," +
                EmergenciesEntry.COLUMN_LATITUDE + " DOUBLE NOT NULL," +
                EmergenciesEntry.COLUMN_LONGTITUDE + " DOUBLE NOT NULL" +
                "); ";
        sqLiteDatabase.execSQL(SQL_CREATE_EMERGENCIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + EmergenciesEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
