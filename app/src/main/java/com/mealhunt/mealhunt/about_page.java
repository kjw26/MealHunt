package com.mealhunt.mealhunt;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class about_page extends LpActivity implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing2);
        NavigationView mNavigationView = (NavigationView) findViewById(R.id.nav_view2);
        Button button = (Button) findViewById(R.id.button13);
        if (mNavigationView != null) {
            mNavigationView.setNavigationItemSelectedListener(this);
        }


    }

    public void faceIn(View view) {
        Intent myIntent = new Intent(about_page.this, MainActivity.class);
        about_page.this.startActivity(myIntent);
    }

    public void homeGo(View view) {
        setContentView(R.layout.activity_landing2);
        NavigationView mNavigationView = (NavigationView) findViewById(R.id.nav_view2);

        if (mNavigationView != null) {
            mNavigationView.setNavigationItemSelectedListener(this);
        }
    }

    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            setContentView(R.layout.activity_landing);
            NavigationView mNavigationView = (NavigationView) findViewById(R.id.nav_view);

            if (mNavigationView != null) {
                mNavigationView.setNavigationItemSelectedListener(this);
            }
        }
        if (id == R.id.nav_invite) {
            Intent myIntent = new Intent(about_page.this, invite_activity.class);
            about_page.this.startActivity(myIntent);
        }
        if (id == R.id.nav_groups) {
            Intent myIntent = new Intent(about_page.this, firebase_groups.class);
            about_page.this.startActivity(myIntent);


        }
        if (id == R.id.nav_about) {
            Intent myIntent = new Intent(about_page.this, about_page.class);
            about_page.this.startActivity(myIntent);
        }
        return false;
    }
}