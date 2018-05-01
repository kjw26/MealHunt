package com.mealhunt.mealhunt;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.google.firebase.messaging.FirebaseMessaging;

public class firebase_groups extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_groups);

        FirebaseMessaging.getInstance().subscribeToTopic("eating_group");
        Log.i("edittext", "subscribed to topic: eating_group");
    }

}
