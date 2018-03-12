package com.example.android.emergency;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity_Startscherm extends AppCompatActivity {

    private Button berichtenGeschiedenis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        berichtenGeschiedenis = (Button) findViewById(R.id.button_berichtenGeschiedenis);
        berichtenGeschiedenis.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Context context = MainActivity_Startscherm.this;
                Class destinationActivity = ChildActivity_BerichtenGeschiedenis.class;
                Intent start_BerichtenGeschiedenis = new Intent(context,destinationActivity);
                startActivity(start_BerichtenGeschiedenis);
            }
        });
    }
}
