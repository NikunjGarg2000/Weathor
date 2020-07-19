package com.nikunjgarg.weathor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class HoroscopeShowActivity extends AppCompatActivity {

    ImageView imageView;
    TextView horoscopeTextView;
    TextView horoscopeNameTextView;
    String tag;
    Intent intent2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horoscope_show);

        setTitle("Your Horoscope");

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setY(-1000);
        horoscopeTextView = (TextView) findViewById(R.id.horoscopeTextView);
        horoscopeTextView.setX(-1000);
        horoscopeNameTextView = (TextView) findViewById(R.id.horoscopeNameTextView);
        horoscopeNameTextView.setX(1000);
        intent2 = new Intent(getApplicationContext(), WebActivity.class);

        Intent intent = getIntent();
        tag = intent.getStringExtra("Tag");

        String horoscope = intent.getStringExtra("Horoscope");

        Resources res = getResources();
        int resourceId = res.getIdentifier(
                tag, "drawable", getPackageName() );
        imageView.setImageResource( resourceId );
        imageView.animate().translationYBy(1000).rotation(1080).setDuration(2000);

        horoscopeTextView.setText(horoscope);
        horoscopeTextView.animate().translationXBy(1000).setDuration(2000);
        horoscopeNameTextView.setText(tag.substring(0, 1).toUpperCase() + tag.substring(1));
        horoscopeNameTextView.animate().translationXBy(-1000).setDuration(1000);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        startActivity(new Intent(getApplicationContext(),HoroscopeActivity.class));
        finish();
        return super.onOptionsItemSelected(item);

    }

    public void moreInfo(View view) {
        setTitle("Loading...");
        String link = "https://www.astrology-zodiac-signs.com/zodiac-signs/" + tag + "/";
        intent2.putExtra("link", link);
        startActivity(intent2);
        setTitle("Your Horoscope");
    }

}