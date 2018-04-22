package com.example.android.emergency;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.example.android.emergency.data.EmergenciesContract;
import com.example.android.emergency.data.EmergenciesDBHelper;

public class EmergencyHistoryActivity extends AppCompatActivity {

    private EmergencyHistoryAdapter mAdapter;
    private RecyclerView mEmergencyList;
    private SQLiteDatabase mDb;
    private final static String TAG = EmergencyHistoryActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergencyhistory);

        ActionBar actionBar = this.getSupportActionBar();

        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        initDatabase();
    }

    private void initDatabase(){
        mEmergencyList = (RecyclerView) findViewById(R.id.recyclerView_emergencies);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mEmergencyList.setLayoutManager(layoutManager);
        mEmergencyList.setHasFixedSize(true);

        EmergenciesDBHelper dbHelper = new EmergenciesDBHelper(this);
        mDb = dbHelper.getWritableDatabase();
        Cursor cursor = getAllEmergencies();
        mAdapter = new EmergencyHistoryAdapter(this,cursor);
        mEmergencyList.setAdapter(mAdapter);
    }

    private Cursor getAllEmergencies() {
        return mDb.query(
                EmergenciesContract.EmergenciesEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                EmergenciesContract.EmergenciesEntry._ID
        );
    }

    //MENU
    //----------------------------------------------------------------------------------------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem settings = menu.findItem(R.id.action_settings);
        MenuItem clear = menu.findItem(R.id.action_clearHistory);
        settings.setVisible(true);
        clear.setVisible(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            NavUtils.navigateUpFromSameTask(this);
        }
        if(id==R.id.action_settings){
            Intent startSettingsAvtivity = new Intent(this,SettingsActivity.class);
            startActivity(startSettingsAvtivity);
            return true;
        }
        if (id==R.id.action_clearHistory){
            //TODO(1) remove all items from database
        }
        return super.onOptionsItemSelected(item);
    }
}
