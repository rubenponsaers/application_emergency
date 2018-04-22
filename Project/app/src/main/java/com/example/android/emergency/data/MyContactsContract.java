package com.example.android.emergency.data;

import android.provider.BaseColumns;

/**
 * Created by Ruben on 2/04/2018.
 */

public class MyContactsContract {

    public static final class MyContactsEntry implements BaseColumns{
        public static final String TABLE_NAME = "mycontacts";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_NUMBER = "number";
    }
}
