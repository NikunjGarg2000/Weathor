package com.nikunjgarg.weathor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener{

    Intent weatherIntent, horoscopeIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getSupportActionBar().hide();

        weatherIntent = new Intent(getApplicationContext(), WeatherActivity.class);
        horoscopeIntent = new Intent(getApplicationContext(), HoroscopeActivity.class);

    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.weathergifImageView) {
            startActivity(weatherIntent);
        } else if (view.getId() == R.id.horoscopegifImageView) {
            startActivity(horoscopeIntent);
        }
    }
}