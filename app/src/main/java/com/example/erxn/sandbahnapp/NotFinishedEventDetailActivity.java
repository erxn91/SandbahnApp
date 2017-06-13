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
import android.widget.Toast;

import java.util.ArrayList;

public class NotFinishedEventDetailActivity extends AppCompatActivity {

    int eventID;
    TextView tvEventOrt;
    TextView tvAnzahlFahrer;
    TextView tvEventDate;
    Event event;
    ArrayList<Driver> drivers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_not_finished_event_detail);

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
        if(drivers.isEmpty()) drivers = giveMeDriverList();
    }

    private ArrayList<Driver> giveMeDriverList() {
        ArrayList<Driver> drivers = new ArrayList<>();
        for(int i = 1; i <= event.getAnzahlDrivers(); i++) {
            Driver driver = new Driver("Fahrer " + i, "keine Maschine");
            drivers.add(driver);
        }
        return drivers;
    }

    private AlertDialog.Builder giveMeListDialogBuilder(ListView myList) {
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

    private AlertDialog.Builder giveMeAcceptionDialogBuilder() {
        final Intent myIntent = new Intent(this, MainActivity.class);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Event abschließen")
                .setMessage("Möchtest du dieses Event wirklich abschließen?")
                .setPositiveButton("Ja", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finishThisEvent(eventID);
                        startActivity(myIntent);
                        Toast.makeText(
                                getApplicationContext(),
                                "Event erfolgreich abgeschlossen",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                })
                .setNegativeButton("Nein", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Nichts unternehmen
                    }
                });
        return builder;
    }

    private void finishThisEvent(int eventID) {
        MyDBManager db = new MyDBManager(this);
        db.finishEvents(eventID);
    }

    public void clicked(View v) {
        if(v.getId() == R.id.BUTTON_LIST_DRIVERS_DETAIL) {
            AdapterDriverList adapter = new AdapterDriverList(this, drivers, false);
            ListView listOFDrivers = new ListView(this);
            listOFDrivers.setAdapter(adapter);

            final Dialog dialog = giveMeListDialogBuilder(listOFDrivers).create();
            dialog.setCanceledOnTouchOutside(true);
            dialog.show();
        }
        if(v.getId() == R.id.BUTTON_FINISH_THIS_EVENT) {
            final Dialog dialog = giveMeAcceptionDialogBuilder().create();
            dialog.setCanceledOnTouchOutside(true);
            dialog.show();
        }
        if(v.getId() == R.id.BUTTON_ZUM_RENNEN) {
            Intent myIntent = new Intent(this, NotFinishedRaceActivity.class);
            myIntent.putExtra("EVENT_ID", eventID);
            startActivity(myIntent);
        }
    }
}
