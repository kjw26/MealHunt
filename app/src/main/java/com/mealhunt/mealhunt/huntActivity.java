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
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.net.ssl.HttpsURLConnection;

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
        //TODO: undo hardcoding of NJIT lat/long
        //TODO: include stars, price in actual search query
        //TODO: need to type in group name, and then send this group name forward in the intent
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

                        MyAsynTask task = new MyAsynTask();
                        String[] params = new String[2];
                        params[0] = "https://web.njit.edu/~rs637/download/receive_json.php";
                        params[1] = url;
                        task.execute(params);

                    }
                });
    }


    public void printResults(String str) {
        Log.i("EditText", str);

        // 1) send this json data to a PHP file along with the topic name.
        // 2) PHP file sends data message through Firebase
        // 3) when app receives the data message, use the body message to start MainTinderActivity
        // can start MainTinderActivity for

        //prob move this code into firebasemessagingservice, since the first user will also receive the notification/payload
        /*
        Intent activityChangeIntent = new Intent(huntActivity.this, MainTinderActivity.class);
        activityChangeIntent.putExtra("str", str);
        huntActivity.this.startActivity(activityChangeIntent);*/
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


    private class MyAsynTask extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String response = "";
            BufferedReader reader = null;
            HttpURLConnection conn = null;
            try {
                URL urlObj = new URL(params[0]);

                conn = (HttpURLConnection) urlObj.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                wr.write(params[1]);
                wr.flush();

                if (conn.getResponseCode() == HttpsURLConnection.HTTP_OK) {
                    Log.i("edittext", "received HTTP OK");
                }
                else {
                    Log.i("editext", "did NOT receive HTTP OK");
                }

                reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line = null;

                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }

                response = sb.toString();
            } catch (Exception e) {
                e.printStackTrace();

            } finally {
                try {
                    reader.close();
                    if (conn != null) {
                        conn.disconnect();
                    }
                } catch (Exception ex) {
                }
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.i("edittext", "sent post data, here's result: " + result);
        }
    }
}
