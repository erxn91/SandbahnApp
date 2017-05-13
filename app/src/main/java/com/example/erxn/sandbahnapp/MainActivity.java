package com.example.erxn.sandbahnapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void clicked(View v) {
        if(v.getId() == R.id.LAYOUT_ADD_EVENT || v.getId() == R.id.BUTTON_ADD_EVENT) {
            Intent myIntent = new Intent(this, AddEventActivity.class);
            startActivity(myIntent);
        }
        if(v.getId() == R.id.LAYOUT_EVENT_NOT_FINISHED || v.getId() == R.id.BUTTON_ACTIVE_RACE) {
            Intent myIntent = new Intent(this, NotFinishedEvent.class);
            startActivity(myIntent);
        }
        if(v.getId() == R.id.LAYOUT_EVENT_FINISHED || v.getId() == R.id.BUTTON_FINISHED_RACE) {
            Intent myIntent = new Intent(this, FinishedEventActivity.class);
            startActivity(myIntent);
        }
    }
}
