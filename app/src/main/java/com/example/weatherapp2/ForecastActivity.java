package com.example.weatherapp2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class ForecastActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);

        // We're opening screen here
        Intent intent = getIntent();
        String cityName = intent.getStringExtra("CITY_NAME");
        int howManyDays = intent.getIntExtra("HOW_MANY_DAYS", 6);
        TextView ForecastHeaderTextView = findViewById(R.id.ForecastHeaderTextView);

        if(cityName != null){
            ForecastHeaderTextView.setText( cityName );
        }
        else{
            ForecastHeaderTextView.setText(R.string.LOCATION_NOT_KNOWN);
        }
    }
}