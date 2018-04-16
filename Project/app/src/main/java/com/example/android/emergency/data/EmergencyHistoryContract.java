package com.example.android.emergency.data;

import android.provider.BaseColumns;

/**
 * Created by Ruben on 13/04/2018.
 */

public class EmergencyHistoryContract {

    public static final class EmergencyHistoryEntry implements BaseColumns {
        public static final String TABLE_NAME = "emergencyHistory";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_LOCATION = "location";
    }
}