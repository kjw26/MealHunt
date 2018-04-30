package com.mealhunt.mealhunt;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class huntActivity extends AppCompatActivity {
    Button mStartButton;
    EditText mFoodType;
    EditText mStars;
    EditText mRadius;
    final String api_key = "AIzaSyBynpSv6ecFaFMNsow2Ud86cMbL9MNBQpM";
    boolean mDone = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hunt);

        mStartButton = (Button) findViewById(R.id.go_button);
        mFoodType = (EditText) findViewById(R.id.foodType);
        mStars = (EditText) findViewById(R.id.stars);
        mRadius = (EditText) findViewById(R.id.radius);
        final String latitude = "40.7423";
        final String longitude = "-74.1793";

        mStartButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        String foodType = mFoodType.getText().toString();
                        String stars = mStars.getText().toString();
                        String radius = mRadius.getText().toString();

                        Log.i("EditText", foodType);
                        Log.i("EditText", stars);
                        Log.i("EditText", radius);
                        String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + latitude
                                + "," + longitude +
                                "&radius=" + radius + "&type=restaurant&keyword=" + foodType + "&key=" + api_key;
                        Log.i("EditText", url);
                        new FileAsync().execute(url);
                    }
                });
    }
    /*
    public String getJson(String str) throws IOException {

        HttpURLConnection c = null;
        try {
            URL u = new URL(str);
            c = (HttpURLConnection) u.openConnection();
            c.setRequestMethod("GET");
            c.setRequestProperty("Content-length", "0");
            c.setUseCaches(false);
            c.setAllowUserInteraction(false);
            c.setConnectTimeout(1000);
            c.setReadTimeout(1000);
            c.connect();
            int status = c.getResponseCode();

            switch (status) {
                case 200:
                case 201:
                    BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    br.close();
                    return sb.toString();
            }

        } catch (MalformedURLException ex) {
            Log.i("EditText", ex.toString());
        } catch (IOException ex) {
            Log.i("EditText", ex.toString());
        } finally {
            if (c != null) {
                try {
                    c.disconnect();
                } catch (Exception ex) {
                    Log.i("EditText", ex.toString());
                }
            }
        }
        return null;
    }*/

    public void printResults(String str) {
        Log.i("EditText", str);
        //start up another activity here, pass the string.

        Intent activityChangeIntent = new Intent(huntActivity.this, MainTinderActivity.class);
        activityChangeIntent.putExtra("str",str);
        huntActivity.this.startActivity(activityChangeIntent);
    }


    class FileAsync extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... aurl) {

            HttpURLConnection c = null;
            try {
                Log.i("EditText", aurl[0].toString());
                URL u = new URL(aurl[0]);
                Log.i("EditText", u.toString());
                c = (HttpURLConnection) u.openConnection();
                c.setRequestMethod("GET");
                c.setRequestProperty("Content-length", "0");
                c.setUseCaches(false);
                c.setAllowUserInteraction(false);
                c.setConnectTimeout(1000);
                c.setReadTimeout(1000);
                c.connect();
                int status = c.getResponseCode();

                switch (status) {
                    case 200:
                    case 201:
                        BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));
                        StringBuilder sb = new StringBuilder();
                        String line;
                        while ((line = br.readLine()) != null) {
                            sb.append(line + "\n");
                        }
                        br.close();
                        return sb.toString();
                }

            } catch (MalformedURLException ex) {
                Log.i("EditText", ex.toString());
            } catch (IOException ex) {
                Log.i("EditText", ex.toString());
            } finally {
                if (c != null) {
                    try {
                        c.disconnect();
                    } catch (Exception ex) {
                        Log.i("EditText", ex.toString());
                    }
                }
            }
            return null;

        }

        @Override
        protected void onPostExecute(String result) {
            printResults(result);
        }
    }
}
