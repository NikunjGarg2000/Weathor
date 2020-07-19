package com.nikunjgarg.weathor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class WeatherShowActivity extends AppCompatActivity {

    Button moreInfoButton;
    TextView weatherText;
    TextView temperature;
    Intent intent;
    String weatherStatus;
    String celciusTemp;
    String fahrenheitTemp;
    String weatherIcon;
    ImageView imageView;
    String link = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_show);

        setTitle("Your Weather");

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        weatherText = (TextView) findViewById(R.id.weatherTextTextView);
        temperature = (TextView) findViewById(R.id.temperatureTextView);
        moreInfoButton = (Button) findViewById(R.id.moreInfoButton);
        imageView = (ImageView) findViewById(R.id.imageView);
        Intent intent1 = getIntent();
        link = intent1.getStringExtra("link");

        intent = getIntent();
        weatherStatus = intent.getStringExtra("WeatherText");
        celciusTemp = intent.getStringExtra("CelciusTemp");
        fahrenheitTemp = intent.getStringExtra("FahrenheitTemp");
        weatherIcon = intent.getStringExtra("WeatherIcon");

        weatherText.setX(1000);
        temperature.setY(-1000);
        weatherText.setText(weatherStatus);
        temperature.setText(celciusTemp + "°C/" + fahrenheitTemp + "°F");
        TextView textView1 = (TextView) findViewById(R.id.textView1);
        textView1.setY(1000);
        TextView textView2 = (TextView) findViewById(R.id.textView2);
        textView2.setX(-1000);

        textView1.animate().translationYBy(-1000).setDuration(1000);
        textView2.animate().translationXBy(1000).setDuration(2000);
        weatherText.animate().translationXBy(-1000).setDuration(2000);
        temperature.animate().translationYBy(+1000).setDuration(1000);

        imageView.setY(-1000);

        Resources res = getResources();
        int resourceId = res.getIdentifier(
                weatherIcon, "drawable", getPackageName() );
        imageView.setImageResource( resourceId );

        imageView.animate().translationYBy(1000).rotation(720).setDuration(3000);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        startActivity(new Intent(getApplicationContext(),WeatherActivity.class));
        setTitle("Your Weather");
        finish();
        return super.onOptionsItemSelected(item);

    }

    public void moreInfo(View view){
        setTitle("Loading...");
        Intent intent2 = new Intent(getApplicationContext(), WebActivity.class);
        intent2.putExtra("link", link);
        startActivity(intent2);
        setTitle("Your Weather");
    }
}