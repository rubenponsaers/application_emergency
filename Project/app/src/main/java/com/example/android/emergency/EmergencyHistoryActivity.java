package com.example.android.emergency;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

public class EmergencyHistoryActivity extends AppCompatActivity {

    private static final int EMERGENCY_LIST_ITEMS = 100;
    private EmergencyHistoryAdapter mAdapter;
    private RecyclerView mEmergencyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergencyhistory);

        ActionBar actionBar = this.getSupportActionBar();

        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mEmergencyList = (RecyclerView) findViewById(R.id.recyclerView_emergencies);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mEmergencyList.setLayoutManager(layoutManager);
        mEmergencyList.setHasFixedSize(true);

        mAdapter = new EmergencyHistoryAdapter(EMERGENCY_LIST_ITEMS);
        mEmergencyList.setAdapter(mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main, menu);
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
        return super.onOptionsItemSelected(item);
    }
}
