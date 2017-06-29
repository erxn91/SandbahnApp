/* Über 2 Images kann hier ausgewählt werden, ob man ein
schnelles Event oder ein Custom Event erstellen möchte.
Der Unterschied ist, dass beim schnellen Event nur Ort und Anzahl der Fahrer
eingegeben werden kann.
Beim Custom Event können noch Namen und Maschinen der Fahrer angegeben werden.
 */

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
            // String, dem Intent mitgeben, um zu wissen welche Anzeige
            // in der nächsten Activity erfolgt
            myIntent.putExtra("WHICH_EVENT", "FAST_EVENT");
            startActivity(myIntent);
        }
        if(v.getId() == R.id.BUTTON_CUSTOM_EVENT) {
            Intent myIntent = new Intent(this, AddEventOrtActivity.class);
            // ebenso hier
            myIntent.putExtra("WHICH_EVENT", "CUSTOM_EVENT");
            startActivity(myIntent);
        }
    }
}
