package com.example.erxn.sandbahnapp;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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
        events = getEventsFromDB();
        AdapterFinishedRace myAdapter = new AdapterFinishedRace(this, events);
        listNotFinishedEvents.setAdapter(myAdapter);
    }

    private void finishEvent() {
        MyDBManager db = new MyDBManager(this);
        db.finishEvent();
    }

    private ArrayList<Event> getEventsFromDB() {
        MyDBManager db = new MyDBManager(this);
        return db.getEvents(false);
    }

    public void clicked(View v) {
        if(v.getId() == R.id.FINISHEVERYTHING) {
            finishEvent();
            finish();
            startActivity(getIntent());
        }
    }
}
