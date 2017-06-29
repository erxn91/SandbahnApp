/* In dieser Activity werden die Punkte der Fahrer
nach den jeweiligen Rennpaarungen ermittelt.
Sobald alle Punkte vergeben wurden, erhalten wir eine
Liste der Punkteverteilung. Geht man dann zurück,
wird das Event abgeschlossen, die Punkte in der DB gespeichert
und man wird wieder zur MainActiviry geschickt
 */

package com.example.erxn.sandbahnapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import java.util.ArrayList;
import java.util.Iterator;

public class NotFinishedRaceActivity extends AppCompatActivity {

    int eventID;
    ArrayList<Driver> initDrivers;
    Driver[] drivers;
    ListView raceList;
    int[] places;
    int driver1SelectedID;
    int driver2SelectedID;
    int driver3SelectedID;

    // befinden wir uns im letzten Screen?
    boolean onLastScreen = false;

    TextView tvDriverName1;
    TextView tvDriverName2;
    TextView tvDriverName3;
    RadioGroup rgPlaces1;
    RadioGroup rgPlaces2;
    RadioGroup rgPlaces3;
    RadioButton rbPlaces1;
    RadioButton rbPlaces2;
    RadioButton rbPlaces3;
    LinearLayout llCompleteRaces;

    // Rennverwaltung
    Iterator<Race> renneniter;
    Rennverwaltung rennen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.complete_race);

        initControlls();
    }

    private void initControlls() {
        Intent myIntent = getIntent();
        eventID = myIntent.getIntExtra("EVENT_ID", 0);
        initDrivers = getDriversFromDB(eventID);
        tvDriverName1 = (TextView) findViewById(R.id.TV_ONE_OF_THREE);
        tvDriverName2 = (TextView) findViewById(R.id.TV_ONE_OF_THREE2);
        tvDriverName3 = (TextView) findViewById(R.id.TV_ONE_OF_THREE3);
        llCompleteRaces = (LinearLayout) findViewById(R.id.LL_COMPLETE_RACES);

        // RadioGroups and RadioButtons
        initRadioGroups();

        drivers = new Driver[initDrivers.size()];
        initDrivers.toArray(drivers);

        // Ein neuer Renntag wird gestartet
        rennen = new Rennverwaltung(drivers, this);

        renneniter = rennen.iterator();
        nextRace();
    }

    private void initRadioGroups() {
        rgPlaces1 = (RadioGroup) findViewById(R.id.RG_FAHRER_PLATZ);
        rgPlaces2 = (RadioGroup) findViewById(R.id.RG_FAHRER_PLATZ2);
        rgPlaces3 = (RadioGroup) findViewById(R.id.RG_FAHRER_PLATZ3);

        driver1SelectedID = rgPlaces1.getCheckedRadioButtonId();
        driver2SelectedID = rgPlaces2.getCheckedRadioButtonId();
        driver3SelectedID = rgPlaces3.getCheckedRadioButtonId();

        rbPlaces1 = (RadioButton) findViewById(driver1SelectedID);
        rbPlaces2 = (RadioButton) findViewById(driver2SelectedID);
        rbPlaces3 = (RadioButton) findViewById(driver3SelectedID);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // befinden wir uns auf dem letzten Screen und drücken auf zurück,
        // werden Punkte gespeichert, das Event wird abgeschlossen und die Punkte
        // und der Activity-Verlauf wird vom Stack genommen
        if(onLastScreen) {
            savePointsInDB();
            Intent myIntent = new Intent(this, MainActivity.class);
            myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(myIntent);
        }
    }

    // solange es noch eine Rennpaarung gibt, wird durchiteriert
    private void nextRace(){
        if (renneniter.hasNext()){
            Race r = renneniter.next();
            tvDriverName1.setText(r.get(0).getName());
            tvDriverName2.setText(r.get(1).getName());
            tvDriverName3.setText(r.get(2).getName());

        } else {
            //Ergebnisse in einer Liste anzeigen
            drivers = rennen.getDrivers();
            raceList = (ListView) findViewById(R.id.LIST_COMPLETE_RACES);
            raceList.setVisibility(View.VISIBLE);
            llCompleteRaces.setVisibility(View.GONE);

            AdapterRaceDriverList adapter = new AdapterRaceDriverList(this, drivers);
            raceList.setAdapter(adapter);
            onLastScreen = true;
        }
    }
    private void savePointsInDB() {
        MyDBManager db = new MyDBManager(this);
        db.addDriverRacePoints(drivers);
        db.finishEvents(eventID);
    }

    private ArrayList<Driver> getDriversFromDB(int eventID) {
        MyDBManager db = new MyDBManager(this);
        ArrayList<Driver> drivers = db.getDriversOfEvent(eventID);
        return drivers;
    }

    // RadioButtons werden ausgewertet
    private void radioButtonsAuswerten() {
        places = new int[3];

        // RadioButtons
        driver1SelectedID = rgPlaces1.getCheckedRadioButtonId();
        driver2SelectedID = rgPlaces2.getCheckedRadioButtonId();
        driver3SelectedID = rgPlaces3.getCheckedRadioButtonId();
        switch(driver1SelectedID) {
            case R.id.RB_ERSTER:
                places[0] = 0;
                break;
            case R.id.RB_ZWEITER:
                places[1] = 0;
                break;
            case R.id.RB_DRITTER:
                places[2] = 0;
                break;
        }
        switch(driver2SelectedID) {
            case R.id.RB_ERSTER2:
                places[0] = 1;
                break;
            case R.id.RB_ZWEITER2:
                places[1] = 1;
                break;
            case R.id.RB_DRITTER2:
                places[2] = 1;
                break;
        }
        switch(driver3SelectedID) {
            case R.id.RB_ERSTER3:
                places[0] = 2;
                break;
            case R.id.RB_ZWEITER3:
                places[1] = 2;
                break;
            case R.id.RB_DRITTER3:
                places[2] = 2;
                break;
        }
    }

    public void clicked(View v) {
        if(v.getId() == R.id.BUTTON_NEXT_RACES) {
            radioButtonsAuswerten();
            // Punkte verteilen
            rennen.evaluate(places[0],
                            places[1],
                            places[2],
                    false,
                    false,
                    false);
            // RadioGroups unselecten
            rgPlaces1.clearCheck();
            rgPlaces2.clearCheck();
            rgPlaces3.clearCheck();
            // danach nächste Rennpaarung
            nextRace();
        }
    }
}
