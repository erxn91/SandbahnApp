package com.example.erxn.sandbahnapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;

public class EventDetailActivity extends AppCompatActivity {

    int eventID;
    TextView tvEventOrt;
    TextView tvAnzahlFahrer;
    TextView tvEventDate;
    Event event;
    ArrayList<Driver> drivers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        initControll();
    }

    private void initControll() {
        tvEventOrt = (TextView)findViewById(R.id.TV_DETAIL_ORT);
        tvAnzahlFahrer = (TextView)findViewById(R.id.TV_DETAIL_ANZAHL_FAHRER);
        tvEventDate = (TextView)findViewById(R.id.TV_EVENT_DATE);

        getEventFromDB();
        if(event.getEventOrt().equals("")) tvEventOrt.setText("Kein Ort angegeben");
        else tvEventOrt.setText(event.getEventOrt());
        tvAnzahlFahrer.setText(Integer.toString(event.getAnzahlDrivers()) + " Fahrer");
        tvEventDate.setText(event.getEventDate());
    }

    private void getEventFromDB() {
        MyDBManager db = new MyDBManager(this);
        Intent myIntent = getIntent();

        // Event
        eventID = myIntent.getIntExtra("EVENT_ID", 0);
        event = db.getOneEvent(eventID);

        // Drivers of Event
        drivers = new ArrayList<>();
        drivers = db.getDriversOfEvent(eventID);
    }

    private AlertDialog.Builder giveMeDialogBuilder(ListView myList) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // nichts unternehmen
            }
        });
        builder.setView(myList);
        return builder;
    }

    public void clicked(View v) {
        if(v.getId() == R.id.BUTTON_LIST_DRIVERS_DETAIL) {
            AdapterDriverList adapter = new AdapterDriverList(this, drivers, false);
            ListView listOFDrivers = new ListView(this);
            listOFDrivers.setAdapter(adapter);

            final Dialog dialog = giveMeDialogBuilder(listOFDrivers).create();
            dialog.setCanceledOnTouchOutside(true);
            dialog.show();
        }
    }
}
