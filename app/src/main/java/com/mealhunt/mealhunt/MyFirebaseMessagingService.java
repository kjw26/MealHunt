package com.mealhunt.mealhunt;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.zip.GZIPInputStream;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "edittext";

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // stick a toast if the app is open

        // notification otherwise

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.i(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.

        if (remoteMessage.getData().size() > 0) {

            if (remoteMessage.getData().get("message") != null) { // app is receiving json data for restaurants
                Log.i("edittext", "received json data payload.");
                String url = remoteMessage.getData().get("message");
                printResults(sendGetRequest(url));

                Log.i(TAG, "Short lived task is done.");
            }
            else { // app is receiving final votes
                Log.i("edittext", "received final votes payload.");
                String placeId = remoteMessage.getData().get("body");
                String title = remoteMessage.getData().get("title");
                String url = "https://www.google.com/maps/search/?api=1&query=Google&query_place_id=" + placeId;
                sendNotification("Click here to open maps.", title, url);

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(browserIntent);
            }
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.i(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            String link = "https://www.google.com/maps/search/?api=1&query=Google&query_place_id=" + remoteMessage.getNotification().getBody();
            sendNotification("Time to eat!", remoteMessage.getNotification().getTitle(),link);

            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
            startActivity(browserIntent);
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

    public void printResults(String str) {
        Intent activityChangeIntent = new Intent(MyFirebaseMessagingService.this, MainTinderActivity.class);
        activityChangeIntent.putExtra("str", str);
        MyFirebaseMessagingService.this.startActivity(activityChangeIntent);
    }
    public String sendGetRequest(String url) {
        HttpURLConnection c = null;
        try {
            Log.i("EditText", url.toString());
            URL u = new URL(url);
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
    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private void sendNotification(String messageBody, String title, String link) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(link));
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.ic_stat_ic_notification)
                        .setContentTitle(title)
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
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
