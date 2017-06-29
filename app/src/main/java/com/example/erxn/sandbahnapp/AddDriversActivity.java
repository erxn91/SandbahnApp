/* In dieser Activity können die Fahrer benutzerdefiniert
angelegt werden. Dem Fahrer kann ein Name und eine Maschine
zugeordnet werden. Dieser werden dann in einer Liste unter
den EditTexts ausgegeben, um zu sehen welche Fahrer schon
erfasst wurtden. Zusätzlich können diese auch wieder gelöscht werden.
 */

package com.example.erxn.sandbahnapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class AddDriversActivity extends AppCompatActivity {

    ListView listOfDrivers;
    ArrayList<Driver> drivers;
    Event event;
    String eventOrt;
    int startnummer = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_drivers);

        initControlls();
    }

    private void initControlls() {
        listOfDrivers = (ListView)findViewById(R.id.LIST_ADD_DRIVERS);
        drivers = new ArrayList<>();

        Intent myIntent = getIntent();
        eventOrt = myIntent.getStringExtra("EVENTORT");

        AdapterDriverList adapter = new AdapterDriverList(this, drivers, true, false);
        listOfDrivers.setAdapter(adapter);
    }

    // Event mit allen Daten erstellen und zurück geben
    private Event initEvent() {
            event = new Event();
            event.setEventDate();
            event.setEventYear();
            event.setEventOrt(eventOrt);
            event.setAnzahlDrivers(listOfDrivers.getCount());
            event.setDrivers(drivers);
            event.isCustom();
            return event;
    }

    // übergebenes Event in DB speichern
    private void saveInDB(Event e) {
        MyDBManager db = new MyDBManager(this);
        db.insertEvent(e);
    }

    // Dialog für Bestätigung, ob Event wirklich erstellt werden soll
    private AlertDialog.Builder getBuilder() {
        final Intent myIntent = new Intent(this, MainActivity.class);

        // Bestätigungsdialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Event erstellen?")
                .setMessage("Danach kannst du keine Fahrer mehr hinzufügen!")
                .setPositiveButton("Ja Event erstellen", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // wenn Event erstellt wird
                        saveInDB(initEvent());
                        startActivity(myIntent);
                    }
                })
                .setNegativeButton("Bin noch nicht fertig", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // nichts unternehmen
                    }
                });
        return builder;
    }

    public void clicked(View v) {
        // Wenn Hinzufügen-Button gedrückt wird, werden alle eingebenen
        // Daten einem Driver zugeordnet. Dieser wird vorübergehend
        // in eine ArrayList geschrieben
        if(v.getId() == R.id.BUTTON_ADD_DRIVER_TO_LIST) {
            EditText driverName = (EditText)findViewById(R.id.ET_DRIVER_NAME);
            EditText driverMachine = (EditText)findViewById(R.id.ET_DRIVER_MACHINE);
            String strDriverMachine = driverMachine.getText().toString();
            // wenn EditText für Name leer
            if(driverName.getText().toString().length() == 0) driverName.setError("Bitte Namen des Fahrers eingeben");
            else {
                // wenn keine Maschine angegeben wird
                if(driverMachine.getText().toString().length() == 0) strDriverMachine = "keine Maschine";
                Driver driver = new Driver(driverName.getText().toString(), strDriverMachine);
                driver.setStartnummer(++startnummer);
                driver.setPlace(0);
                drivers.add(driver);

                driverName.setText("");
                driverMachine.setText("");

                Toast.makeText(this, "Fahrer hinzugefügt", Toast.LENGTH_SHORT);

                AdapterDriverList adapter = new AdapterDriverList(this, drivers, true, false);
                listOfDrivers.setAdapter(adapter);
            }
        }
        // Driver an der jeweiligen Stelle löschen
        // Danach wird ListView aktualisiert
        if(v.getId() == R.id.BUTTON_DELETE_DRIVER) {
            final int position = listOfDrivers.getPositionForView((View) v.getParent());
            drivers.remove(position);

            Toast.makeText(this, "Fahrer gelöscht", Toast.LENGTH_SHORT).show();

            AdapterDriverList adapter = new AdapterDriverList(this, drivers, true, false);
            listOfDrivers.setAdapter(adapter);
        }
        // wenn Event erstellt werden soll wird ein
        // Bestätigungsdialog aufgerufen
        if(v.getId() == R.id.BUTTON_EVENT_ERSTELLEN) {
            getBuilder().show();
        }
    }
}
