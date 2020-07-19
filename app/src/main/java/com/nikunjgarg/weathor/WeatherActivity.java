package com.nikunjgarg.weathor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ybq.android.spinkit.style.Circle;
import com.github.ybq.android.spinkit.style.Wave;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.Locale;

public class WeatherActivity extends AppCompatActivity {

    public static final int MY_PERMISSION_REQUEST_LOCATION = 1;

    EditText cityEditText;
    String link = "";
    Intent intent1;
    ProgressBar progressBar;
    Wave wave;
    SharedPreferences sharedPreferences;
    String sharedPreferencesCity = "";
    Button button;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        setTitle("Weather");

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        cityEditText = (EditText) findViewById(R.id.cityEditText);
        intent1 = new Intent(getApplicationContext(), WeatherShowActivity.class);
        progressBar = (ProgressBar) findViewById(R.id.spinKit);
        wave = new Wave();
        progressBar.setIndeterminateDrawable(wave);
        progressBar.setVisibility(View.INVISIBLE);
        button = (Button) findViewById(R.id.button);
        button.setEnabled(true);
        textView = (TextView) findViewById(R.id.textView);

        sharedPreferences = this.getSharedPreferences("com.example.myfirstapplication",MODE_PRIVATE);

        sharedPreferencesCity = sharedPreferences.getString("city", "");
        cityEditText.setText(sharedPreferencesCity);
        cityEditText.requestFocus();
        cityEditText.setSelection(cityEditText.length());

        cityEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                button.setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    //Get closest city name
    public String hereLocation(double lat, double lon) {
        String curCity = "";

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addressList;
        try {
            addressList = geocoder.getFromLocation(lat, lon, 1);
            if (addressList.size() > 0) {
                curCity = addressList.get(0).getLocality();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return curCity;
    }

    public void getLocation(View view) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSION_REQUEST_LOCATION);
        } else {
            setTitle("Loading...");
            progressBar.setVisibility(View.VISIBLE);
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            final Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            try {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setTitle("Weather");
                        progressBar.setVisibility(View.INVISIBLE);
                        cityEditText.setText(hereLocation(location.getLatitude(), location.getLongitude()));
                        cityEditText.requestFocus();
                        cityEditText.setSelection(cityEditText.length());
                    }
                },3000);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Location Not Found! \nPlease Enter:(", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_REQUEST_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        progressBar.setVisibility(View.VISIBLE);
                        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                        final Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        try {new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                setTitle("Weather");
                                progressBar.setVisibility(View.INVISIBLE);
                                cityEditText.setText(hereLocation(location.getLatitude(), location.getLongitude()));
                                cityEditText.requestFocus();
                                cityEditText.setSelection(cityEditText.length());
                            }
                        },3000);
                        } catch (Exception e) {
                            setTitle("Weather");
                            progressBar.setVisibility(View.INVISIBLE);
                            e.printStackTrace();
                            Toast.makeText(this, "Location Not Found! \nPlease Enter:(", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "No permission granted! \nSo, Please Enter city:(", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        startActivity(new Intent(getApplicationContext(),HomeActivity.class));
        finish();
        return super.onOptionsItemSelected(item);

    }

    public void getWeather(View view) {
        try {
            DownloadTask task = new DownloadTask();
            //Different API keys to use or create a new from accuweather free API
            //aKNtQBNl3L2UAvvkzGRtrDUBJbAQmup8
            //K0keFsUNiSdOVM4CTZvMuwqAbM46q3bC
            //yxGAXzwNzsLas0zmOnviOkH32gzP1b6a

            String encodedCityName = URLEncoder.encode(cityEditText.getText().toString(), "UTF-8");
            task.execute("https://dataservice.accuweather.com/locations/v1/cities/search?apikey=aKNtQBNl3L2UAvvkzGRtrDUBJbAQmup8&q=" + encodedCityName);
            progressBar.setVisibility(View.VISIBLE);
            setTitle("Loading...");

            InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            mgr.hideSoftInputFromWindow(cityEditText.getWindowToken(), 0);

            sharedPreferences.edit().putString("city", cityEditText.getText().toString()).apply();

        } catch (Exception e) {
            setTitle("Weather");
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Sorry, API's max limit reached:(", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.INVISIBLE);
        }

        button.setEnabled(false);
    }

    public class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {

            String cityInfoArray = "";
            URL url;
            HttpURLConnection urlConnection = null;

            try {
                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();


                while (data != -1) {
                    char current = (char) data;
                    cityInfoArray += current;
                    data = reader.read();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return cityInfoArray;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            URL url;
            HttpURLConnection urlConnection = null;
            JSONArray jsonArray;
            JSONObject jsonObject;
            InputStream in;
            InputStreamReader reader;

            if (s.equals("[]")) {
                Toast.makeText(WeatherActivity.this, "Please enter a valid city name:(", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
                setTitle("Weather");

            } else {

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);

                try {
                    jsonArray = new JSONArray(s);

                    String cityInfo = jsonArray.getString(0);
                    jsonObject = new JSONObject(cityInfo);
                    String key = jsonObject.getString("Key");

                    url = new URL("https://dataservice.accuweather.com/currentconditions/v1/" + key + "?apikey=aKNtQBNl3L2UAvvkzGRtrDUBJbAQmup8");

                    urlConnection = (HttpURLConnection) url.openConnection();
                    in = urlConnection.getInputStream();
                    reader = new InputStreamReader(in);
                    int data = reader.read();

                    String weatherInfoArray = "";
                    while (data != -1) {
                        char current = (char) data;
                        weatherInfoArray += current;
                        data = reader.read();
                    }

                    jsonArray = new JSONArray(weatherInfoArray);
                    String weatherInfo = jsonArray.getString(0);
                    jsonObject = new JSONObject(weatherInfo);

                    String weatherText = jsonObject.getString("WeatherText");
                    int weatherIconPic = -1;
                    weatherIconPic = jsonObject.getInt("WeatherIcon");
                    if (weatherIconPic == -1) {
                        weatherIconPic = 0;
                    }
                    String linkhttp = jsonObject.getString("MobileLink");

                    String temperature = jsonObject.getString("Temperature");
                    jsonObject = new JSONObject(temperature);

                    String metric = jsonObject.getString("Metric");
                    jsonObject = new JSONObject(metric);
                    String celciusTemp = jsonObject.getString("Value");

                    jsonObject = new JSONObject(temperature);
                    String imperial = jsonObject.getString("Imperial");
                    jsonObject = new JSONObject(imperial);
                    String fahrenheitTemp = jsonObject.getString("Value");

                    if (!weatherText.equals("") || !weatherText.equals(null)) {
                        intent1.putExtra("WeatherText", weatherText);
                    }
                    if (!celciusTemp.equals("") || !celciusTemp.equals(null)) {
                        intent1.putExtra("CelciusTemp", celciusTemp);
                    }
                    if (!fahrenheitTemp.equals("") || !fahrenheitTemp.equals(null)) {
                        intent1.putExtra("FahrenheitTemp", fahrenheitTemp);
                    }

                    String weatherIcon = "weathericon" + weatherIconPic;
                    intent1.putExtra("WeatherIcon", weatherIcon);

                    String linkhttps = "";
                    for (int i = 0; i < linkhttp.length(); i++) {
                        linkhttps += linkhttp.charAt(i);
                        if (i == 3) {
                            linkhttps += "s";
                        }
                    }
                    link = linkhttps;
                    intent1.putExtra("link", link);
                    startActivity(intent1);
                    finish();
                    progressBar.setVisibility(View.INVISIBLE);
                    setTitle("Weather");

                } catch (Exception e) {
                    Toast.makeText(WeatherActivity.this, "Could not find Weather:( \nPlease try again!", Toast.LENGTH_SHORT).show();
                    button.setEnabled(true);
                    progressBar.setVisibility(View.INVISIBLE);
                    setTitle("Weather");
                    e.printStackTrace();
                }
            }
        }
    }
}
