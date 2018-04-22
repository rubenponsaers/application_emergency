package com.example.android.emergency.data;

import android.provider.BaseColumns;

/**
 * Created by Ruben on 2/04/2018.
 */

public class PriorityContactsContract {

    public static final class ContactsEntry implements BaseColumns{
        public static final String TABLE_NAME = "emergencyContacts";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_NUMBER = "number";
    }
}
