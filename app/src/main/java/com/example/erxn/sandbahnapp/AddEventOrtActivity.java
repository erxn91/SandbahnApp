package com.example.erxn.sandbahnapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

public class AddEventOrtActivity extends AppCompatActivity {

    EditText etEventOrt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        initControlls();
    }

    private Intent whereDidIComeFrom() {
        Intent myIntent = getIntent();
        if(getIntent().getStringExtra("WHICH_EVENT").equals("FAST_EVENT")) {
            myIntent = new Intent(this, AddSomeDriversActivity.class);
            myIntent.putExtra("EVENTORT", etEventOrt.getText().toString());
        }
        if(getIntent().getStringExtra("WHICH_EVENT").equals("CUSTOM_EVENT")) {
            myIntent = new Intent(this, AddDriversActivity.class);
            myIntent.putExtra("EVENTORT", etEventOrt.getText().toString());
        }
        return myIntent;
    }

    private void initControlls() {
        etEventOrt = (EditText)findViewById(R.id.EDIT_EVENT_ORT);
    }

    public void clicked(View v) {
        if(v.getId() == R.id.BUTTON_NEXT_TO_ADD_DRIVERS) {
                startActivity(whereDidIComeFrom());
        }
    }
}
