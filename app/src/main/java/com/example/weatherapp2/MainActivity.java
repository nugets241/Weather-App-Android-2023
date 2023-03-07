package com.example.weatherapp2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private RequestQueue queue;
    private String description = "Click refresh";
    private double temperature = 0;
    private double windSpeed = 0;
    private String icon = "02d";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Create a new web request queue
        queue = Volley.newRequestQueue(this);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("WEATHER_DESCRIPTION", description);
        outState.putDouble("TEMPERATURE", temperature);
        outState.putDouble("WIND_SPEED", windSpeed);
        outState.putString("ICON", icon);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        //Check if there is a bundle and activity ui data saved
        //If so, read the values from there

        description = savedInstanceState.getString("WEATHER_DESCRIPTION");
        if (description == null) {
            description = "Click refresh";
        }
        icon = savedInstanceState.getString("ICON", "02d");
        temperature = savedInstanceState.getDouble("TEMPERATURE", 0);
        windSpeed = savedInstanceState.getDouble("WIND_SPEED", 0);

        TextView descriptionTextView = findViewById(R.id.descriptionTextView);
        descriptionTextView.setText(description);
        TextView temperatureTextView = findViewById(R.id.temperatureTextView);
        temperatureTextView.setText("" + temperature + " °C");
        TextView windTextView = findViewById(R.id.windTextView);
        windTextView.setText("" + windSpeed + " m/s");

        String iconUrl = "http://openweathermap.org/img/w/" + icon + ".png";
        ImageView iconImageView = findViewById(R.id.imageView);
        Picasso.get().load(iconUrl).into(iconImageView);
    }

    public void fetchWeatherData(View view) {
        // check locale
        Locale currentLocale = Locale.getDefault();
        String languageCode = currentLocale.getLanguage();
        if (!languageCode.equals("fi")){
            languageCode = "en";
        }
        String languageParam = "&lang=" + languageCode;
        // Make the request and put in the queue to get the response
        String url = "https://api.openweathermap.org/data/2.5/weather?q=tampere&units=metric&appid=3db444a60b76cca61fdbc4a5a15a276b" + languageParam;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    Log.d("WEATHER_APP", response);
                    parseJsonAndUpdateUI(response);
                }, error -> Log.d("WEATHER_APP", error.toString()));

        // Add the request to the queue to actually send it
        queue.add(stringRequest);
    }

    @SuppressLint("SetTextI18n")
    private void parseJsonAndUpdateUI(String response) {
        try {
            JSONObject weatherResponse = new JSONObject(response);

            description = weatherResponse.getJSONArray("weather").getJSONObject(0).getString("description");
            temperature = weatherResponse.getJSONObject("main").getDouble("temp");
            windSpeed = weatherResponse.getJSONObject("wind").getDouble("speed");
            icon = weatherResponse.getJSONArray("weather").getJSONObject(0).getString("icon");

            TextView descriptionTextView = findViewById(R.id.descriptionTextView);
            descriptionTextView.setText(description);
            TextView temperatureTextView = findViewById(R.id.temperatureTextView);
            temperatureTextView.setText("" + temperature + " °C");
            TextView windTextView = findViewById(R.id.windTextView);
            windTextView.setText("" + windSpeed + " m/s");

            String iconUrl = "http://openweathermap.org/img/w/" + icon + ".png";
            ImageView iconImageView = findViewById(R.id.imageView);
            Picasso.get().load(iconUrl).into(iconImageView);

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public void openForecastActivity(View view) {
        // Switch to the other screen and send a message to that screen
        Intent openForecast = new Intent(this, ForecastActivity.class);
        openForecast.putExtra("CITY_NAME", "Tampere");
        openForecast.putExtra("HOW_MANY_DAYS", 5);
        startActivity(openForecast);
    }


    public void openWebPage(View view) {
        // Open tuni webpage
        String urlString = "https://www.tuni.fi/";
        Uri uri = Uri.parse(urlString);
        // Create the implicit intent
        Intent openWebpage = new Intent(Intent.ACTION_VIEW, uri);

        try {
            startActivity(openWebpage);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void setAlarm(View view) {
        Intent intent = new Intent(AlarmClock.ACTION_SET_TIMER)
                .putExtra(AlarmClock.EXTRA_MESSAGE, "Timeout!")
                .putExtra(AlarmClock.EXTRA_LENGTH, 20)
                .putExtra(AlarmClock.EXTRA_SKIP_UI, true);
        try {
            startActivity(intent);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void showMap(View view) {
        //open map
        String locationString = "geo:61.50386933832975,23.80875046953705";
        Uri geoLocation = Uri.parse(locationString);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(geoLocation);
        try {
            startActivity(intent);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}