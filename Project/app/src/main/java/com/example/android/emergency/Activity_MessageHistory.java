package com.example.android.emergency;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public class Activity_MessageHistory extends AppCompatActivity {

    private static final int NUM_LIST_ITEMS = 100;
    private MessageHistoryAdapter mAdapter;
    private RecyclerView mNumbersList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messagehistory);

        mNumbersList = (RecyclerView) findViewById(R.id.recyclerView_messages);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mNumbersList.setLayoutManager(layoutManager);
        mNumbersList.setHasFixedSize(true);

        mAdapter = new MessageHistoryAdapter(NUM_LIST_ITEMS);
        mNumbersList.setAdapter(mAdapter);
    }
}
