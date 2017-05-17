package com.example.erxn.sandbahnapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class AddDriversActivity extends AppCompatActivity {

    ListView listOfDrivers;
    ArrayList<Driver> drivers;
    Event event;
    String eventOrt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_drivers);

        initControlls();
    }

    private void initControlls() {
        listOfDrivers = (ListView)findViewById(R.id.LIST_ADD_DRIVERS);
        event = new Event();
        drivers = event.getDrivers();

        Intent myIntent = getIntent();
        eventOrt = myIntent.getStringExtra("EVENTORT");

        AdapterAddDrivers adapter = new AdapterAddDrivers(this, drivers);
        listOfDrivers.setAdapter(adapter);
    }

    private Event initEvent() {
            Event e = event;
            e.setEventOrt(eventOrt);
            e.setAnzahlDrivers(listOfDrivers.getCount());
            return e;
    }

    private void saveInDB(Event e) {
        MyDBManager db = new MyDBManager(this);
        db.insertEvent(e);
    }

    public void clicked(View v) {
        if(v.getId() == R.id.BUTTON_ADD_DRIVER_TO_LIST) {
            EditText driverName = (EditText)findViewById(R.id.ET_DRIVER_NAME);
            EditText driverMachine = (EditText)findViewById(R.id.ET_DRIVER_MACHINE);
            // wenn EditText für Name leer
            if(driverName.getText().toString().length() == 0) driverName.setError("Bitte Namen des Fahrers eingeben");
            else {
                Driver driver = new Driver(driverName.getText().toString(), driverMachine.getText().toString());
                event.addDriver(driver);

                driverName.setText("");
                driverMachine.setText("");

                Toast.makeText(this, "Fahrer hinzugefügt", Toast.LENGTH_SHORT);

                AdapterAddDrivers adapter = new AdapterAddDrivers(this, drivers);
                listOfDrivers.setAdapter(adapter);
            }
        }
        if(v.getId() == R.id.BUTTON_DELETE_DRIVER) {
            final int position = listOfDrivers.getPositionForView((View) v.getParent());
            drivers.remove(position);

            Toast.makeText(this, "Fahrer gelöscht", Toast.LENGTH_SHORT).show();

            AdapterAddDrivers adapter = new AdapterAddDrivers(this, drivers);
            listOfDrivers.setAdapter(adapter);
        }
        if(v.getId() == R.id.BUTTON_EVENT_ERSTELLEN) {
            saveInDB(initEvent());
            Intent myIntent = new Intent(this, MainActivity.class);
            startActivity(myIntent);
        }
    }
}
