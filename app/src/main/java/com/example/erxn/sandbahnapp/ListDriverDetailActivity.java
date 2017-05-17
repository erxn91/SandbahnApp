package com.example.erxn.sandbahnapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class ListDriverDetailActivity extends AppCompatActivity {

    ListView listDriverDetail;
    ArrayList<Driver> drivers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_driver_detail);

        initControlls();
    }

    private void initControlls() {
        listDriverDetail = (ListView)findViewById(R.id.LIST_DRIVER_DETAIL);
        Intent myIntent = getIntent();
        int eventID = myIntent.getIntExtra("EVENT_ID_DETAIL", 0);
        getDriversFromDB(eventID);

        AdapterAddDrivers adapter = new AdapterAddDrivers(this, drivers);
        listDriverDetail.setAdapter(adapter);
    }

    private void getDriversFromDB(int eventID) {
        MyDBManager db = new MyDBManager(this);
        drivers = db.getDriversOfEvent(eventID);
    }
}
