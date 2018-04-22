package com.example.android.emergency;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.emergency.data.EmergenciesContract;
import com.example.android.emergency.data.EmergenciesDBHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private Button emergencyHistory;
    private Button sendEmergency;
    private Button contacts;
    private TextView userName;
    private TextView userBloodType;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private SQLiteDatabase mDb;
    private final static String TAG = EmergencyHistoryActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EmergenciesDBHelper dbHelper = new EmergenciesDBHelper(this);
        mDb = dbHelper.getWritableDatabase();

        emergencyHistory = (Button) findViewById(R.id.button_emergencyHistory);
        sendEmergency = (Button) findViewById(R.id.button_sendEmergency);
        contacts = (Button) findViewById(R.id.button_contacts);
        userName = (TextView) findViewById(R.id.textView_userName);
        userBloodType = (TextView) findViewById(R.id.textView_userBloodType);

        setupSharedPreferences();
        setupLocationService();

        //Button actions
        emergencyHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = MainActivity.this;
                Class destinationActivity = EmergencyHistoryActivity.class;
                Intent startActivity = new Intent(context, destinationActivity);
                startActivity(startActivity);
            }
        });
        sendEmergency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                configureEmergency();
            }
        });
        contacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = MainActivity.this;
                Class destinationActivity = PriorityContactsActivity.class;
                Intent startActivity = new Intent(context, destinationActivity);
                startActivity(startActivity);
            }
        });
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
        locationManager.requestLocationUpdates("gps", 1000, 0, locationListener);
    }

    private void setupSharedPreferences(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
        loadInfoFromSharedPreferences(sharedPreferences);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
    }

    public long addNewEmergency(Location location){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String date = simpleDateFormat.format(calendar.getTime());

        double lat = location.getLatitude();
        double lon = location.getLongitude();

        ContentValues contentValues = new ContentValues();

        contentValues.put(EmergenciesContract.EmergenciesEntry.COLUMN_DATE,date);
        contentValues.put(EmergenciesContract.EmergenciesEntry.COLUMN_LAT, lat);
        contentValues.put(EmergenciesContract.EmergenciesEntry.COLUMN_LON, lon);
        locationManager.removeUpdates(locationListener);
        Context context = MainActivity.this;
        Toast messageToast = Toast.makeText(context, "Message send", Toast.LENGTH_SHORT);
        messageToast.show();
        return mDb.insert(EmergenciesContract.EmergenciesEntry.TABLE_NAME,null,contentValues);
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
            Intent startSettingsActivity = new Intent(this, SettingsActivity.class);
            startActivity(startSettingsActivity);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        loadInfoFromSharedPreferences(sharedPreferences);
    }

    private void loadInfoFromSharedPreferences(SharedPreferences sharedPreferences){
        userName.setText(sharedPreferences.getString(getString(R.string.pref_username_key),getString(R.string.pref_username)));
        userBloodType.setText(sharedPreferences.getString(getString(R.string.pref_bloodtype_key),getString(R.string.pref_bloodtype)));
    }
}
