package com.mealhunt.mealhunt;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class LpActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        NavigationView mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        Button button = (Button) findViewById(R.id.button13);
        if (mNavigationView != null) {
            mNavigationView.setNavigationItemSelectedListener(this);
        }


}
    public void faceIn(View view)
    {
        Intent myIntent = new Intent(LpActivity.this, huntActivity.class);
        //Intent myIntent = new Intent(LpActivity.this, firebase_groups.class);
        LpActivity.this.startActivity(myIntent);
    }
    public void homeGo(View view)
    {
        setContentView(R.layout.activity_landing);
        NavigationView mNavigationView = (NavigationView) findViewById(R.id.nav_view);

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
            Intent myIntent = new Intent(LpActivity.this, invite_activity.class);
            LpActivity.this.startActivity(myIntent);
        }
        if (id == R.id.nav_groups) {
            //Intent myIntent = new Intent(LpActivity.this, group_page.class);
            Intent myIntent = new Intent(LpActivity.this, firebase_groups.class);
            LpActivity.this.startActivity(myIntent);


        }
        if (id == R.id.nav_about) {
            Intent myIntent = new Intent(LpActivity.this, about_page.class);
            LpActivity.this.startActivity(myIntent);
        }
        return false;
    }
    
}