package com.example.erxn.sandbahnapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class ChooseEventTypeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_event_type);

        initControlls();
    }

    private void initControlls() {
    }

    public void clicked(View v) {
        if(v.getId() == R.id.BUTTON_FAST_EVENT) {
            Intent myIntent = new Intent(this, AddEventOrtActivity.class);
            myIntent.putExtra("WHICH_EVENT", "FAST_EVENT");
            startActivity(myIntent);
        }
        if(v.getId() == R.id.BUTTON_CUSTOM_EVENT) {
            Intent myIntent = new Intent(this, AddEventOrtActivity.class);
            myIntent.putExtra("WHICH_EVENT", "CUSTOM_EVENT");
            startActivity(myIntent);
        }
    }
}
