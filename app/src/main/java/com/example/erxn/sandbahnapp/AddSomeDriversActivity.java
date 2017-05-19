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
        setContentView(R.layout.activity_add_event_02);

        initControlls();
    }

    private void initControlls() {
        eventOrt = getIntent().getStringExtra("EVENTORT");

        tvAnzahlDriver = (TextView)findViewById(R.id.TV_ANZAHL_DRIVER);
        tvAnzahlDriver.setText(driverCount + " Fahrer");
        addButton = (Button)findViewById(R.id.BUTTON_ADD_DRIVER);
        removeButton = (Button)findViewById(R.id.BUTTON_REMOVE_DRIVER);

        addButton.setOnLongClickListener(longTouchButton);
        addButton.setOnTouchListener(releaseButton);
        removeButton.setOnLongClickListener(longTouchButton);
        removeButton.setOnTouchListener(releaseButton);
    }

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
            saveInDB(initEvent());

            Intent myIntent = new Intent(this, MainActivity.class);
            startActivity(myIntent);
        }
    }

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
            drivers.add(driver);
        }
        e.setDrivers(drivers);
        return e;
    }

    private void saveInDB(Event e) {
        MyDBManager db = new MyDBManager(this);
        db.insertEvent(e);
    }

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
