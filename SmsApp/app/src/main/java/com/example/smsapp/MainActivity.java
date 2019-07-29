package com.example.smsapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Main Activity. Displays a list of numbers.
 *
 * @author itcuties
 *
 */
public class MainActivity extends AppCompatActivity {
    Intent service;
    Button btnStartService , btnStopService;
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Register sms listener
        textView = findViewById(R.id.smsNumberText);
        btnStartService = findViewById(R.id.btn_start_service);
        btnStartService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                service = new Intent(getApplicationContext(), MyService.class);
                getApplicationContext().startService(service);
                textView.setText("start service");
            }
        });

        btnStopService = findViewById(R.id.btn_stop_service);
        btnStopService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(service != null){
                    getApplicationContext().stopService(service);
                    textView.setText("stop service");
                }
            }
        });
    }

}
