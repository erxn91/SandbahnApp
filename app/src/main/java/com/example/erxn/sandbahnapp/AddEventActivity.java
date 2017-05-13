package com.example.erxn.sandbahnapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class AddEventActivity extends AppCompatActivity {

    EditText etEventOrt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        initControlls();
    }

    private void initControlls() {
        etEventOrt = (EditText)findViewById(R.id.EDIT_EVENT_ORT);
    }

    public void clicked(View v) {
        if(v.getId() == R.id.BUTTON_NEXT_TO_ADD_DRIVERS) {
            Intent myIntent =  new Intent(this, AddEventActivity_02.class);
            myIntent.putExtra("EVENTORT", etEventOrt.getText().toString());
            startActivity(myIntent);
        }
    }
}
