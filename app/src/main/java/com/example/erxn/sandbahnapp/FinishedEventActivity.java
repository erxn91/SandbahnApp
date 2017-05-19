package com.example.erxn.sandbahnapp;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class FinishedEventActivity extends AppCompatActivity {

    ListView listFinishedEvents;
    TextView tvDefaultMessage;
    ArrayList<Event> events;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finished_event);

        iniControlls();
    }

    private void iniControlls() {
        listFinishedEvents = (ListView)findViewById(R.id.LIST_FINISHED_RACES);
        tvDefaultMessage = (TextView)findViewById(R.id.TV_DEFAULT);
        events = getEventsFromDB();

        if(!events.isEmpty()) {
            AdapterRace myAdapter = new AdapterRace(this, events, true);
            listFinishedEvents.setAdapter(myAdapter);
            tvDefaultMessage.setVisibility(View.GONE);
        }
        else listFinishedEvents.setVisibility(View.GONE);
    }

    private ArrayList<Event> getEventsFromDB() {
        MyDBManager db = new MyDBManager(this);
        return db.getEvents(true);
    }
}
