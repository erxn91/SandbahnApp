package com.example.erxn.sandbahnapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class AddEventActivity_03 extends AppCompatActivity {

    String eventOrt;
    int anzahlFahrer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event_03);

        initControlls();
    }

    private void initControlls() {
        eventOrt = getIntent().getStringExtra("EVENTORT");
        anzahlFahrer = getIntent().getIntExtra("ANZAHL_FAHRER", 0);
    }
}
