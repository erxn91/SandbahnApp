package com.example.erxn.sandbahnapp;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class NotFinishedEvent extends AppCompatActivity {

    ListView listNotFinishedEvents;
    ArrayList<Event> events;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_not_finished_event);

        initControlls();
    }

    private void initControlls() {
        listNotFinishedEvents = (ListView)findViewById(R.id.LIST_NOT_FINISHED_RACES);
        events = eventsFromDB();
        AdapterFinishedRace myAdapter = new AdapterFinishedRace(this, events);
        listNotFinishedEvents.setAdapter(myAdapter);
    }

    private ArrayList<Event> eventsFromDB() {
        MyDBManager db = new MyDBManager(this);
        return db.getEvents(false);
    }
}
