package com.example.erxn.sandbahnapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class NotFinishedRaceActivity extends AppCompatActivity {

    ArrayList<Driver> initDrivers;
    Driver[] drivers;
    ListView raceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_not_finished_race);

        initControlls();
    }

    private void initControlls() {
        raceList = (ListView)findViewById(R.id.LIST_RACES);

        Intent myIntent = getIntent();
        int eventID = myIntent.getIntExtra("EVENT_ID", 0);
        initDrivers = getDriversFromDB(eventID);

        drivers = new Driver[initDrivers.size()];
        initDrivers.toArray(drivers);

        Rennverwaltung_mit_ausfallen_v1 rennen = new Rennverwaltung_mit_ausfallen_v1(drivers, this);
        ArrayList<Race> races = rennen.getRennfolge();

        AdapterRaceDriverList adapter = new AdapterRaceDriverList(this, races, rennen);
        raceList.setAdapter(adapter);
    }

    private ArrayList<Driver> getDriversFromDB(int eventID) {
        MyDBManager db = new MyDBManager(this);
        ArrayList<Driver> drivers = db.getDriversOfEvent(eventID);
        return drivers;
    }

    public void clicked(View v) {
        if(v.getId() == R.id.BUTTON_ALLE_RENNEN_ABSCHL) {
        }
    }
}
