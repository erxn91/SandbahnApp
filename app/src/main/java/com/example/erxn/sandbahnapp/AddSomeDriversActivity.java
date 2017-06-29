/* In dieser Activity können einem schnellen Event, beliebig viele
Fahrer hinzugefügt werden. Um die Usability etwas zu verbessern,
können beide Buttons lange gedrückt werden, damit es etwas schneller geht.
Fährt man fort, wird ein Event mit dem EventOrt und der Anzahl der Fahrer
in die DB geschrieben.
 */

package com.example.erxn.sandbahnapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class AddSomeDriversActivity extends AppCompatActivity {

    TextView tvAnzahlDriver;
    int driverCount = 0;
    String eventOrt;
    Button addButton;
    Button removeButton;
    ArrayList<Driver> drivers;

    Timer T;
    boolean isButtonLongPressed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_some_drivers);

        initControlls();
    }

    private void initControlls() {
        // wird benötigt um Event zu erstellen
        eventOrt = getIntent().getStringExtra("EVENTORT");

        tvAnzahlDriver = (TextView)findViewById(R.id.TV_ANZAHL_DRIVER);
        tvAnzahlDriver.setText(driverCount + " Fahrer");
        addButton = (Button)findViewById(R.id.BUTTON_ADD_DRIVER);
        removeButton = (Button)findViewById(R.id.BUTTON_REMOVE_DRIVER);

        // EventListener fürs lange Drücken
        addButton.setOnLongClickListener(longTouchButton);
        // EventListener fürs Beenden des langen Drückens
        addButton.setOnTouchListener(releaseButton);
        // natürlich für beide Buttons
        removeButton.setOnLongClickListener(longTouchButton);
        removeButton.setOnTouchListener(releaseButton);
    }

    // Beim langen Drücken einer der Buttons, wird ein Timer ausgelöst,
    // der kein delay besitzt und in der vogegebenen Periode den
    // DriverCount erhöht
    private void runFastDriverCount(final boolean up) {
        T = new Timer();
        T.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        if(up) tvAnzahlDriver.setText(++driverCount + " Fahrer");
                        else {
                            if(driverCount < 1) {
                                // passiert nichts, da Wert sonst negativ wird!
                            }
                            else tvAnzahlDriver.setText(--driverCount + " Fahrer");
                        }
                    }
                });
            }
        }, 0, 100);
    }

    // Methode um den Timer zu stoppen
    private void stopFastDriverCount() {
        T.cancel();
    }

    public void clicked(View v) {
        if(v.getId() == R.id.BUTTON_ADD_DRIVER)
            tvAnzahlDriver.setText(++driverCount + " Fahrer");
        if(v.getId() == R.id.BUTTON_REMOVE_DRIVER) {
            if(driverCount < 1) {
                // passiert nichts, da Wert sonst negativ wird!
            }
            else tvAnzahlDriver.setText(--driverCount + " Fahrer");
        }
        if(v.getId() == R.id.BUTTON_GO_ON) {
            // erstelltes Event in der Datenbank speichern
            saveInDB(initEvent());

            Intent myIntent = new Intent(this, MainActivity.class);
            startActivity(myIntent);
        }
    }

    // Event mit allen zur Verfügung stehenden Werten erzeugen
    // Driver werden auch erzeugt und dem Event zugeordnet
    private Event initEvent() {
        Event e = new Event();
        e.setEventOrt(eventOrt);
        e.setAnzahlDrivers(driverCount);
        e.setEventDate();
        e.setEventYear();
        e.isFast();

        // Drivers
        drivers = new ArrayList<>();
        for(int i = 1; i <= driverCount; i++) {
            Driver driver = new Driver("Fahrer " + i, "keine Maschine");
            driver.setStartnummer(i);
            drivers.add(driver);
        }
        e.setDrivers(drivers);
        return e;
    }

    // Event als Parameter mitgeben um es in DB zu speichern
    private void saveInDB(Event e) {
        MyDBManager db = new MyDBManager(this);
        db.insertEvent(e);
    }

    // Listener für das lange Drücken einer der Buttons
    private View.OnLongClickListener longTouchButton = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            if(v.getId() == R.id.BUTTON_ADD_DRIVER) {
                isButtonLongPressed = true;
                runFastDriverCount(true);
            }
            if(v.getId() == R.id.BUTTON_REMOVE_DRIVER) {
                isButtonLongPressed = true;
                runFastDriverCount(false);
            }
            return true;
        }
    };

    // Listener für das Beenden des langen Drückens einer der Buttons
    private View.OnTouchListener releaseButton = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            v.onTouchEvent(event);
            if(v.getId() == R.id.BUTTON_ADD_DRIVER) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (isButtonLongPressed) {
                        stopFastDriverCount();
                        isButtonLongPressed = false;
                    }
                }
            }
            if(v.getId() == R.id.BUTTON_REMOVE_DRIVER) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (isButtonLongPressed) {
                        stopFastDriverCount();
                        isButtonLongPressed = false;
                    }
                }
            }
            return true;
        }
    };
}
