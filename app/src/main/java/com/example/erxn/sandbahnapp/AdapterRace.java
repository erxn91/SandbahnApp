package com.example.erxn.sandbahnapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterRace extends BaseAdapter {
    Event[] events;
    Context context;

    TextView tvDate;
    TextView tvYearOfEvent;
    TextView tvOrt;
    TextView tvAnzahlDrivers;
    boolean eventIsFinished;

    private static LayoutInflater inflater = null;
    public AdapterRace(Activity someActivity, ArrayList<Event> eventsFinished, boolean eventIsFinished) {
        // TODO Auto-generated constructor stub
        events = new Event[eventsFinished.size()];      // Array in Größe der ArrayList
        eventsFinished.toArray(events);                 // ArrayList in Array casten
        context = someActivity;
        this.eventIsFinished = eventIsFinished;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return events.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View rowView;
        rowView = inflater.inflate(R.layout.list_item_finished_race, null);
        tvDate=(TextView) rowView.findViewById(R.id.DATE_OF_EVENT);
        tvOrt=(TextView) rowView.findViewById(R.id.ORT_EVENT);
        tvYearOfEvent=(TextView) rowView.findViewById(R.id.YEAR_OF_EVENT);
        tvAnzahlDrivers=(TextView) rowView.findViewById(R.id.ANZAHL_DRIVERS);
        tvDate.setText(events[position].getEventDate());
        tvYearOfEvent.setText(events[position].getEventYear());
        tvOrt.setText(events[position].getEventOrt());
        tvAnzahlDrivers.setText(Integer.toString(events[position].getAnzahlDrivers()) + " Teilnehmer");
        rowView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent myIntent;
                if(eventIsFinished) myIntent = new Intent(context, FinishedEventDetailActivity.class);
                else myIntent = new Intent(context, NotFinishedEventDetailActivity.class);
                myIntent.putExtra("EVENT_ID", events[position].getEventID());
                context.startActivity(myIntent);
            }
        });
        return rowView;
    }

}
