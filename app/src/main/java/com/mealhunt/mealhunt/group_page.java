package com.mealhunt.mealhunt;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class group_page extends AppCompatActivity implements MyRecyclerViewAdapter.ItemClickListener {

    MyRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_page);

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
        adapter = new MyRecyclerViewAdapter (this, groupName);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(107));
        recyclerView.addItemDecoration(new HorizontalSpaceItemDecoration(100));
    }

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(this, "You clicked " + adapter.getItem(position) + " on row number " + (position + 1), Toast.LENGTH_SHORT).show();
    }
}