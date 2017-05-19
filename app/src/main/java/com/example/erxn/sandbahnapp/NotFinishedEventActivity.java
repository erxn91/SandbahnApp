package com.example.erxn.sandbahnapp;


import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class NotFinishedEventActivity extends AppCompatActivity {

    ListView listNotFinishedEvents;
    Button btFinishEverything;
    ArrayList<Event> events;
    TextView tvDefaultMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_not_finished_event);

        initControlls();
    }

    private void initControlls() {
        listNotFinishedEvents = (ListView)findViewById(R.id.LIST_NOT_FINISHED_RACES);
        btFinishEverything = (Button)findViewById(R.id.FINISHEVERYTHING);
        tvDefaultMessage = (TextView)findViewById(R.id.TV_DEFAULT_MSSG);
        events = getEventsFromDB();

        if(!events.isEmpty()) {
            AdapterRace myAdapter = new AdapterRace(this, events, false);
            listNotFinishedEvents.setAdapter(myAdapter);
            tvDefaultMessage.setVisibility(View.GONE);
        }
        else {
            btFinishEverything.setVisibility(View.GONE);
            listNotFinishedEvents.setVisibility(View.GONE);
        }
    }

    private void finishEvent() {
        MyDBManager db = new MyDBManager(this);
        db.finishEvents();
    }

    private ArrayList<Event> getEventsFromDB() {
        MyDBManager db = new MyDBManager(this);
        return db.getEvents(false);
    }

    private AlertDialog.Builder getBuilder() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Alle Events abschließen")
            .setMessage("Möchtest du wirklich alle Events abschließen?")
            .setPositiveButton("Ja", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finishEvent();
                    finish();
                    startActivity(getIntent());
                }
            })
            .setNegativeButton("Nein", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // nichts unternehmen
                }
            });
        return builder;
    }

    public void clicked(View v) {
        if(v.getId() == R.id.FINISHEVERYTHING) {
            getBuilder().show();
        }
    }
}
