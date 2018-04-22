package com.example.android.emergency.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.android.emergency.data.PriorityContactsContract.*;

/**
 * Created by Ruben on 2/04/2018.
 */

public class PriorityContactsDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "contacts.db";
    private static final int DATABASE_VERSION = 3;

    public PriorityContactsDbHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_CONTACTS_TABLE = "CREATE TABLE " + ContactsEntry.TABLE_NAME + " (" +
                ContactsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                ContactsEntry.COLUMN_NAME + " TEXT NOT NULL," +
                ContactsEntry.COLUMN_NUMBER + " INTEGER NOT NULL" +
                "); ";
        sqLiteDatabase.execSQL(SQL_CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ContactsEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
