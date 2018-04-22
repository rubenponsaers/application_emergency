package com.example.android.emergency;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
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
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.emergency.data.EmergenciesContract;
import com.example.android.emergency.data.EmergenciesDBHelper;
import com.example.android.emergency.data.MyContactsDBHelper;

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
    private LocationManager locationManager;
    private LocationListener locationListener;
    private SQLiteDatabase sqLiteDatabaseWrite;
    private SQLiteDatabase sqLiteDatabaseRead;
    private EmergenciesDBHelper dbHelperWrite;
    private MyContactsDBHelper dbHelperRead;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelperWrite = new EmergenciesDBHelper(this);
        sqLiteDatabaseWrite = dbHelperWrite.getWritableDatabase();
        dbHelperRead = new MyContactsDBHelper(this);
        sqLiteDatabaseRead = dbHelperWrite.getReadableDatabase();
        //TODO(3) Haal de contacten uit de lijst van contacten en verstuur een nood sms

        emergencies = (Button) findViewById(R.id.button_emergencies);
        sendEmergency = (Button) findViewById(R.id.button_sendEmergency);
        myContacts = (Button) findViewById(R.id.button_mycontacts);
        userName = (TextView) findViewById(R.id.textView_userName);
        userBloodType = (TextView) findViewById(R.id.textView_userBloodType);
        stopEmergency = (Button) findViewById(R.id.button_stopEmergency);

        setupSharedPreferences();
        setupLocationService();


        //Button actions
        emergencies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent emergenciesActivity = new Intent(MainActivity.this, EmergenciesActivity.class);
                startActivity(emergenciesActivity);
            }
        });
        myContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myContactsActivity = new Intent(MainActivity.this, MyContactsActivity.class);
                startActivity(myContactsActivity);
            }
        });
        sendEmergency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                configureEmergency();
            }
        });
        stopEmergency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                locationManager.removeUpdates(locationListener);
                Toast messageToast = Toast.makeText(MainActivity.this, "Emergency stopted!", Toast.LENGTH_SHORT);
                messageToast.show();
            }
        });
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
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                addNewEmergency(location);
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
                startActivity(intent);
            }
        };
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
        else{
            configureEmergency();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    configureEmergency();
                }
        }
    }

    private void configureEmergency() {
        locationManager.requestLocationUpdates("gps", 5000, 0, locationListener);
        //TODO(2) Andere providers implementeren
    }

    private void setupSharedPreferences(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
        loadInfoFromSharedPreferences(sharedPreferences);
    }

    public long addNewEmergency(Location location){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDate = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat simpleTime = new SimpleDateFormat("HH:mm");
        String date = simpleDate.format(calendar.getTime());
        String time = simpleTime.format(calendar.getTime());

        double lat = location.getLatitude();
        double lon = location.getLongitude();

        sendSMS(date,time,lat,lon);

        ContentValues contentValues = new ContentValues();
            contentValues.put(EmergenciesContract.EmergenciesEntry.COLUMN_DATE,date);
            contentValues.put(EmergenciesContract.EmergenciesEntry.COLUMN_TIME, time);
            contentValues.put(EmergenciesContract.EmergenciesEntry.COLUMN_LATITUDE, lat);
            contentValues.put(EmergenciesContract.EmergenciesEntry.COLUMN_LONGTITUDE, lon);

        Toast messageToast = Toast.makeText(MainActivity.this, "Sending emergency!", Toast.LENGTH_SHORT);
        messageToast.show();

        return sqLiteDatabaseWrite.insert(EmergenciesContract.EmergenciesEntry.TABLE_NAME,null,contentValues);
    }

    private void sendSMS(String date, String time, double latitude, double longtitude){

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        loadInfoFromSharedPreferences(sharedPreferences);
    }

    private void loadInfoFromSharedPreferences(SharedPreferences sharedPreferences){
        userName.setText(sharedPreferences.getString(getString(R.string.pref_username_key),getString(R.string.pref_username)));
        userBloodType.setText("Bloodtype: " + sharedPreferences.getString(getString(R.string.pref_bloodtype_key),getString(R.string.pref_bloodtype)));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
    }

}
