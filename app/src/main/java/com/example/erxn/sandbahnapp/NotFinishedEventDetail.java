package com.example.erxn.sandbahnapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class NotFinishedEventDetail extends AppCompatActivity {

    int eventID;
    TextView tvEventOrt;
    TextView tvAnzahlFahrer;
    Event event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_not_finished_event_detail);

        initControll();
    }

    private void initControll() {
        tvEventOrt = (TextView)findViewById(R.id.TV_DETAIL_ORT);
        tvAnzahlFahrer = (TextView)findViewById(R.id.TV_DETAIL_ANZAHL_FAHRER);

        getEventFromDB();
        tvEventOrt.setText(event.getEventOrt());
        tvAnzahlFahrer.setText(Integer.toString(event.getAnzahlDrivers()) + " Fahrer");
    }

    private void getEventFromDB() {
        MyDBManager db = new MyDBManager(this);
        Intent myIntent = getIntent();
        eventID = myIntent.getIntExtra("EVENT_ID", 0);
        event = db.getOneEvent(eventID);
    }

    public void clicked(View v) {
        if(v.getId() == R.id.BUTTON_LIST_DRIVERS_DETAIL) {
            Intent myIntent = new Intent(this, ListDriverDetailActivity.class);
            myIntent.putExtra("EVENT_ID_DETAIL", eventID);
            startActivity(myIntent);
        }
    }
}
