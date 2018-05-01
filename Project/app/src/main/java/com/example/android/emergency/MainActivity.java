package com.example.android.emergency;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.emergency.data.EmergenciesContract;
import com.example.android.emergency.data.EmergenciesDBHelper;
import com.example.android.emergency.data.NetworkUtils;
import com.example.android.emergency.data.fetchDataDoctor;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private final static String TAG = EmergenciesActivity.class.getSimpleName();
    private Button emergencies;
    private Button sendEmergency;
    private Button myContacts;
    private Button stopEmergency;
    private TextView userName;
    private TextView userBloodType;
    private TextView doctorInCity;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private String provider;
    private SQLiteDatabase sqLiteDatabaseWrite;
    private EmergenciesDBHelper dbHelperWrite;
    private Boolean emergencieStarted;
    public static String prefCity;
    public static ProgressBar loadingIndicator;
    public static TextView doctorResult;
    private int prefDelay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelperWrite = new EmergenciesDBHelper(this);
        sqLiteDatabaseWrite = dbHelperWrite.getWritableDatabase();
        emergencieStarted = false;

        emergencies = (Button) findViewById(R.id.button_emergencies);
        sendEmergency = (Button) findViewById(R.id.button_sendEmergency);
        myContacts = (Button) findViewById(R.id.button_mycontacts);
        userName = (TextView) findViewById(R.id.textView_userName);
        userBloodType = (TextView) findViewById(R.id.textView_userBloodType);
        stopEmergency = (Button) findViewById(R.id.button_stopEmergency);
        doctorInCity = (TextView) findViewById(R.id.textview_doctor_city);
        doctorResult = (TextView) findViewById(R.id.textview_doctor_result);
        loadingIndicator = (ProgressBar) findViewById(R.id.loading_indicator);


        //Button actions
        emergencies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent emergenciesActivity = new Intent(MainActivity.this, EmergenciesActivity.class);
                startActivity(emergenciesActivity);
                Log.i(TAG,"Emergencies button clicked");
            }
        });
        myContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myContactsActivity = new Intent(MainActivity.this, MyContactsActivity.class);
                startActivity(myContactsActivity);
                Log.i(TAG,"My contacts activity button clicked");
            }
        });
        sendEmergency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(emergencieStarted == false){
                    setupLocationService();
                    emergencieStarted = true;
                    Log.i(TAG,"Emergencie button clicked");
                }
                Log.e(TAG,"Emergencie is already activated");
            }
        });
        stopEmergency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(emergencieStarted == true) {
                    locationManager.removeUpdates(locationListener);
                    sendEmergency.setBackgroundColor(getResources().getColor(R.color.colorThemRedsendEmergencyVisible));
                    stopEmergency.setBackgroundColor(getResources().getColor(R.color.colorThemeRedstopEmergencyInvisible));
                    Toast messageToast = Toast.makeText(MainActivity.this, "Emergency stopted!", Toast.LENGTH_SHORT);
                    messageToast.show();
                    emergencieStarted = false;
                    Log.i(TAG,"Stop emergencie button clicked");
                }
                Log.e(TAG,"Emergencie is already deactivated");
            }
        });
        setupSharedPreferences();
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadDoctorQuery();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem settings = menu.findItem(R.id.action_settings);
        settings.setVisible(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings){
            Intent settingsActivity = new Intent(this, SettingsActivity.class);
            startActivity(settingsActivity);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupLocationService() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                double currentLatitude = location.getLatitude();
                double currentLongtitude = location.getLongitude();
                addNewEmergency(currentLongtitude, currentLatitude);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {
            }

            @Override
            public void onProviderEnabled(String s) {
            }

            @Override
            public void onProviderDisabled(String s) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                Log.e(TAG,"Provider disabled");
                startActivity(intent);
            }
        };
        configureEmergency();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    configureEmergency();
                }
                Log.e(TAG,"Permission not granted");
        }
    }

    private void configureEmergency() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.INTERNET},
                    1);
            return;
        }
    }
        locationManager.requestLocationUpdates(provider, prefDelay, 0, locationListener);
        sendEmergency.setBackgroundColor(getResources().getColor(R.color.colorThemRedsendEmergencyInvisible));
        stopEmergency.setBackgroundColor(getResources().getColor(R.color.colorThemeRedstopEmergencyVisible));
    }

    private void setupSharedPreferences(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
        loadInfoFromSharedPreferences(sharedPreferences);
    }

    private void loadDoctorQuery(){
        String city = prefCity;
        URL githubSearchUrl = NetworkUtils.buildUrlDoctor();
        doctorInCity.setText("Doctor on duty in " + city + ":");
        Log.i(TAG,"Loading doctor on duty information");
        new fetchDataDoctor().execute(githubSearchUrl);
    }

    public long addNewEmergency(double longidtude, double latitude){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDate = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat simpleTime = new SimpleDateFormat("HH:mm");
        String date = simpleDate.format(calendar.getTime());
        String time = simpleTime.format(calendar.getTime());

        ContentValues contentValues = new ContentValues();
            contentValues.put(EmergenciesContract.EmergenciesEntry.COLUMN_DATE,date);
            contentValues.put(EmergenciesContract.EmergenciesEntry.COLUMN_TIME, time);
            contentValues.put(EmergenciesContract.EmergenciesEntry.COLUMN_LATITUDE, latitude);
            contentValues.put(EmergenciesContract.EmergenciesEntry.COLUMN_LONGITUDE, longidtude);

        Toast messageToast = Toast.makeText(MainActivity.this, "Sending emergency!", Toast.LENGTH_SHORT);
        messageToast.show();

        Log.i(TAG,"New emergency added to the database of emergencies");

        return sqLiteDatabaseWrite.insert(EmergenciesContract.EmergenciesEntry.TABLE_NAME,null,contentValues);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        loadInfoFromSharedPreferences(sharedPreferences);
    }

    private void loadInfoFromSharedPreferences(SharedPreferences sharedPreferences){
        userName.setText(sharedPreferences.getString(getString(R.string.pref_username_key),getString(R.string.pref_username)));
        userBloodType.setText("Bloodtype: " + sharedPreferences.getString(getString(R.string.pref_bloodtype_key),getString(R.string.pref_bloodtype)));
        prefCity = sharedPreferences.getString(getString(R.string.pref_city_key), getString(R.string.pref_city));
        if(sharedPreferences.getString(getString(R.string.pref_delay_key),getString(R.string.pref_delay)).equals(getString(R.string.pref_delay_5s_key))){
            prefDelay = 5000;
        }
        else if(sharedPreferences.getString(getString(R.string.pref_delay_key),getString(R.string.pref_delay)).equals(getString(R.string.pref_delay_20s_key))){
            prefDelay = 20000;
        }
        else if(sharedPreferences.getString(getString(R.string.pref_delay_key),getString(R.string.pref_delay)).equals(getString(R.string.pref_delay_1m_key))){
            prefDelay = 60000;
        }
        Log.i(TAG,"Sharedprefernces have been setup");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
    }

}
