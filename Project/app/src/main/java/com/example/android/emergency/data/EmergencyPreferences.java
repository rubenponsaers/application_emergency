package com.example.android.emergency.data;

/**
 * Created by Ruben on 16/04/2018.
 */

public class EmergencyPreferences {

    public static final String PREF_NAME = "Ruben";
    public static final String PREF_BLOODTYPE = "A+";

    public static String getPrefName(){
        return PREF_NAME;
    }

    public static String getPrefBloodtype() {
        return PREF_BLOODTYPE;
    }
}
