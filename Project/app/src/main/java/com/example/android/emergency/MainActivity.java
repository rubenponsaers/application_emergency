package com.example.android.emergency;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button messageHistory;
    private Button help;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        messageHistory = (Button) findViewById(R.id.button_messageHistory);
        messageHistory.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Context context = MainActivity.this;
                Class destinationActivity = Activity_MessageHistory.class;
                Intent start_MessageHistory = new Intent(context,destinationActivity);
                startActivity(start_MessageHistory);
            }
        });

        help = (Button) findViewById(R.id.button_help);
        help.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Context context = MainActivity.this;
                Toast messageToast = Toast.makeText(context, "Sending message...", Toast.LENGTH_SHORT);
                messageToast.show();
            }
        });
    }
}
