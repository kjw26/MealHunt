package com.mealhunt.mealhunt;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;

public class firebase_groups extends AppCompatActivity{
    Button mJoinButton;
    EditText mGroupName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_groups);

        mJoinButton = (Button) findViewById(R.id.join_button);
        mGroupName = (EditText) findViewById(R.id.group_name);

        mJoinButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        String groupName = mGroupName.getText().toString();
                        Log.i("edittext: subscribed to", groupName);
                        FirebaseMessaging.getInstance().subscribeToTopic(groupName);
                        FirebaseMessaging.getInstance().subscribeToTopic("eating_group");
                        Toast.makeText(firebase_groups.this, "Successfully joined the group.",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
}
