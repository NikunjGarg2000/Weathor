package com.nikunjgarg.weathor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HoroscopeActivity extends AppCompatActivity implements View.OnClickListener {

    String clickedTag;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horoscope);

        setTitle("Horoscope");

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        intent = new Intent(getApplicationContext(), HoroscopeShowActivity.class);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        startActivity(new Intent(getApplicationContext(),HomeActivity.class));
        finish();
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onClick(View view) {

        DownloadTask task = new DownloadTask();
        clickedTag = view.getTag().toString();
        task.execute("https://horoscope-api.herokuapp.com/horoscope/today/" + clickedTag);
        setTitle("Loading...");
        intent.putExtra("Tag", clickedTag);
    }

    public class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {

            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;
            JSONObject jsonObject;

            try {
                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();

                while (data != -1) {
                    char current = (char) data;
                    result += current;
                    data = reader.read();
                }

                jsonObject = new JSONObject(result);
                result = jsonObject.getString("horoscope");
                intent.putExtra("Horoscope", result);
                startActivity(intent);
                finish();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }
    }
}