package com.example.android.emergency;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.emergency.data.EmergencyHistoryContract;
import com.example.android.emergency.data.EmergencyHistoryDBHelper;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private Button emergencyHistory;
    private Button sendEmergency;
    private Button contacts;
    private TextView userName;
    private TextView userBloodType;

    private SQLiteDatabase mDb;

    private final static String TAG = EmergencyHistoryActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EmergencyHistoryDBHelper dbHelper = new EmergencyHistoryDBHelper(this);
        mDb = dbHelper.getWritableDatabase();

        emergencyHistory = (Button) findViewById(R.id.button_emergencyHistory);
        sendEmergency = (Button) findViewById(R.id.button_sendEmergency);
        contacts = (Button) findViewById(R.id.button_contacts);
        userName = (TextView) findViewById(R.id.textView_userName);
        userBloodType = (TextView) findViewById(R.id.textView_userBloodType);

        setupSharedPreferences();
        //Button actions
        emergencyHistory.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Context context = MainActivity.this;
                Class destinationActivity = EmergencyHistoryActivity.class;
                Intent startActivity = new Intent(context,destinationActivity);
                startActivity(startActivity);
            }
        });
        sendEmergency.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Context context = MainActivity.this;
                Toast messageToast = Toast.makeText(context, "Sending emergency message...", Toast.LENGTH_SHORT);
                messageToast.show();
                addNewEmergency();
            }
        });
        contacts.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Context context = MainActivity.this;
                Class destinationActivity = ContactsActivity.class;
                Intent startActivity = new Intent(context,destinationActivity);
                startActivity(startActivity);
            }
        });
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

    public long addNewEmergency(){
        String date = "16/04/2018";
        String location = "Tongeren";

        ContentValues contentValues = new ContentValues();

        contentValues.put(EmergencyHistoryContract.EmergencyHistoryEntry.COLUMN_DATE,date);
        contentValues.put(EmergencyHistoryContract.EmergencyHistoryEntry.COLUMN_LOCATION, location);

        return mDb.insert(EmergencyHistoryContract.EmergencyHistoryEntry.TABLE_NAME,null,contentValues);
        //TODO(3) Vraagt locatie en datum op en voegt dit toe aan de database
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main, menu);
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
