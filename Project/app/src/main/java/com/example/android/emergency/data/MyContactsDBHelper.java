package com.example.android.emergency.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.android.emergency.data.MyContactsContract.*;

/**
 * Created by Ruben on 2/04/2018.
 */

public class MyContactsDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "mycontacts.db";
    private static final int DATABASE_VERSION = 4;

    public MyContactsDBHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_MYCONTACTS_TABLE = "CREATE TABLE " + MyContactsEntry.TABLE_NAME + " (" +
                MyContactsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                MyContactsEntry.COLUMN_NAME + " TEXT NOT NULL," +
                MyContactsEntry.COLUMN_NUMBER + " INTEGER NOT NULL" +
                "); ";
        sqLiteDatabase.execSQL(SQL_CREATE_MYCONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MyContactsEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
