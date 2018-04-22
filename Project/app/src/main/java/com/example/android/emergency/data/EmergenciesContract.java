package com.example.android.emergency.data;

import android.provider.BaseColumns;

/**
 * Created by Ruben on 13/04/2018.
 */

public class EmergenciesContract {

    public static final class EmergenciesEntry implements BaseColumns {
        public static final String TABLE_NAME = "emergencies";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_LAT = "latitude";
        public static final String COLUMN_LON = "longtitude";
    }
}