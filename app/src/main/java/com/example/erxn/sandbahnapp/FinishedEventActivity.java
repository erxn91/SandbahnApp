/* Diese Activity zeigt eine Auflistung aller
abgeschlossenen Activities.
 */

package com.example.erxn.sandbahnapp;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import java.util.ArrayList;

public class FinishedEventActivity extends AppCompatActivity {

    ListView listFinishedEvents;
    TextView tvDefaultMessage;
    ArrayList<Event> events;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finished_event);

        iniControlls();
    }

    private void iniControlls() {
        listFinishedEvents = (ListView)findViewById(R.id.LIST_FINISHED_RACES);
        listFinishedEvents.setOnItemLongClickListener(listener);
        tvDefaultMessage = (TextView)findViewById(R.id.TV_DEFAULT);
        listFinishedEvents.setLongClickable(true);
        // ArrayList mit allen abgeschlossenen Events
        // aus DB holen
        events = getEventsFromDB();

        if(!events.isEmpty()) {
            AdapterRace myAdapter = new AdapterRace(this, events, true);
            listFinishedEvents.setAdapter(myAdapter);
            tvDefaultMessage.setVisibility(View.GONE);
        }
        else listFinishedEvents.setVisibility(View.GONE);
    }

    // alle bereits abgeschlossenen Events in einer ArrayList speichern
    private ArrayList<Event> getEventsFromDB() {
        MyDBManager db = new MyDBManager(this);
        return db.getEvents(true);
    }

    // Event aus der DB löschen
    private ArrayList<Event> deleteEventFromDB(int eventID) {
        MyDBManager db = new MyDBManager(this);
        db.deleteEvent(eventID);
        return db.getEvents(true);
    }

    // Damit ein Löschen Icon bei langem Drücken des ListItems auftaucht
    AdapterView.OnItemLongClickListener listener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            view.findViewById(R.id.BUTTON_DELETE_EVENT).setVisibility(View.VISIBLE);
            return false;
        }
    };

    public void clicked(View v) {
        if(v.getId() == R.id.BUTTON_DELETE_EVENT) {
            final int position = listFinishedEvents.getPositionForView((View) v.getParent());
            events = deleteEventFromDB(events.get(position).getEventID());

            Toast.makeText(this, "Event gelöscht", Toast.LENGTH_SHORT).show();

            AdapterRace myAdapter = new AdapterRace(this, events, true);
            listFinishedEvents.setAdapter(myAdapter);
        }
    }
}
