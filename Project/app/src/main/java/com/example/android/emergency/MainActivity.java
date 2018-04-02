package com.example.android.emergency;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity{

    private Button emergencyHistory;
    private Button sendEmergency;

    private static boolean PREFERENCES_HAVE_BEEN_UPDATED = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Button actions
        emergencyHistory = (Button) findViewById(R.id.button_emergencyHistory);
        emergencyHistory.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Context context = MainActivity.this;
                Class destinationActivity = EmergencyHistoryActivity.class;
                Intent startEmergencyHistory = new Intent(context,destinationActivity);
                startActivity(startEmergencyHistory);
            }
        });

        sendEmergency = (Button) findViewById(R.id.button_sendEmergency);
        sendEmergency.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Context context = MainActivity.this;
                Toast messageToast = Toast.makeText(context, "Sending emergency message...", Toast.LENGTH_SHORT);
                messageToast.show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * When a menu item is selected, start the activity that belongs to that item
     * @param item  the item that is selected
     * @return
     */
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
}
