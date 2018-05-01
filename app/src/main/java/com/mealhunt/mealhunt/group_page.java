package com.mealhunt.mealhunt;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

public class group_page extends LpActivity implements MyRecyclerViewAdapter.ItemClickListener, NavigationView.OnNavigationItemSelectedListener {

    MyRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing3);

        // data to populate the RecyclerView with
        ArrayList<String> groupName = new ArrayList<>();
        groupName.add("Group 1");
        groupName.add("Group 2");
        groupName.add("Group 3");
        groupName.add("Group 4");
        groupName.add("Group 5");

        // set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.groupNa);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyRecyclerViewAdapter(this, groupName);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(107));
        recyclerView.addItemDecoration(new HorizontalSpaceItemDecoration(100));

        NavigationView lNavigationView = (NavigationView) findViewById(R.id.nav_view3);

        if (lNavigationView != null) {
            lNavigationView.setNavigationItemSelectedListener(this);
        }

    }

    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Intent myIntent = new Intent(group_page.this, LpActivity.class);
            group_page.this.startActivity(myIntent);
        }
        if (id == R.id.nav_invite) {
            Intent myIntent = new Intent(group_page.this, invite_activity.class);
            group_page.this.startActivity(myIntent);
        }
        if (id == R.id.nav_groups) {
            Intent myIntent = new Intent(group_page.this, group_page.class);
            group_page.this.startActivity(myIntent);


        }
        if (id == R.id.nav_about) {
            Intent myIntent = new Intent(group_page.this, about_page.class);
            group_page.this.startActivity(myIntent);
        }
        return false;
    }

        @Override
        public void onItemClick (View view,int position){
            Toast.makeText(this, "You clicked " + adapter.getItem(position) + " on row number " + (position + 1), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPointerCaptureChanged ( boolean hasCapture){

        }
    }