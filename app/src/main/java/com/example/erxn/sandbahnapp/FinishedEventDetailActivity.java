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

public class FinishedEventDetailActivity extends AppCompatActivity {

    int eventID;
    TextView tvEventOrt;
    TextView tvAnzahlFahrer;
    TextView tvEventDate;
    TextView tvFirstPlace;
    TextView tvSecondPlace;
    TextView tvThirdPlace;
    Event event;
    ArrayList<Driver> drivers;
    Driver[] winners;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finished_event_detail);

        initControll();
    }

    private void initControll() {
        tvEventOrt = (TextView)findViewById(R.id.TV_DETAIL_ORT_FINISHED);
        tvAnzahlFahrer = (TextView)findViewById(R.id.TV_DETAIL_ANZAHL_FAHRER_FINISHED);
        tvEventDate = (TextView)findViewById(R.id.TV_EVENT_DATE_FINISHED);
        tvFirstPlace = (TextView)findViewById(R.id.TV_FIRST_PLACE);
        tvSecondPlace = (TextView)findViewById(R.id.TV_SECOND_PLACE);
        tvThirdPlace = (TextView)findViewById(R.id.TV_THIRD_PLACE);

        getEventFromDB();
        if(event.getEventOrt().equals("")) tvEventOrt.setText("Kein Ort angegeben");
        else tvEventOrt.setText(event.getEventOrt());
        tvAnzahlFahrer.setText(Integer.toString(event.getAnzahlDrivers()) + " Fahrer");
        tvEventDate.setText(event.getEventDate());

        tvFirstPlace.setText("1. Platz: " + winners[0].getName());
        tvSecondPlace.setText("2. Platz: " + winners[1].getName());
        tvThirdPlace.setText("3. Platz: " + winners[2].getName());
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
        winners = getWinnersOfEvent();
    }

    private ArrayList<Driver> giveMeDriverList() {
        ArrayList<Driver> drivers = new ArrayList<>();
        for(int i = 1; i <= event.getAnzahlDrivers(); i++) {
            Driver driver = new Driver("Fahrer " + i, "keine Maschine");
            drivers.add(driver);
        }
        return drivers;
    }

    private Driver[] getWinnersOfEvent() {
        Driver[] winners = new Driver[drivers.size()];
        drivers.toArray(winners);
        return winners;
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
