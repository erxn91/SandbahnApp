package com.example.erxn.sandbahnapp;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class FinishedEventActivity extends AppCompatActivity {

    ListView listFinishedEvents;
    ArrayList<Event> events;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finished_event);

        iniControlls();
    }

    private void iniControlls() {
        listFinishedEvents = (ListView)findViewById(R.id.LIST_FINISHED_RACES);
        events = getEventsFromDB();
        AdapterFinishedRace myAdapter = new AdapterFinishedRace(this, events);
        listFinishedEvents.setAdapter(myAdapter);
    }

    private ArrayList<Event> getEventsFromDB() {
        MyDBManager db = new MyDBManager(this);
        return db.getEvents(true);
    }
}
