// Created by erxn on 26.04.2017.

package com.example.erxn.sandbahnapp;

import android.app.Activity;
import android.content.Context;
import android.provider.CalendarContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class AdapterFinishedRace extends BaseAdapter {
    Event[] events;
    Context context;
    private static LayoutInflater inflater = null;
    public AdapterFinishedRace(Activity someActivity, ArrayList<Event> eventsFinished) {
        // TODO Auto-generated constructor stub
        events = new Event[eventsFinished.size()];      // Array in Größe der ArrayList
        eventsFinished.toArray(events);                 // ArrayList in Array casten
        context = someActivity;
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

    public class Holder
    {
        TextView tvDate;
        TextView tvYearOfEvent;
        TextView tvOrt;
        TextView tvAnzahlDrivers;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder = new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.list_item_finished_race, null);
        holder.tvDate=(TextView) rowView.findViewById(R.id.DATE_OF_EVENT);
        holder.tvOrt=(TextView) rowView.findViewById(R.id.ORT_EVENT);
        holder.tvYearOfEvent=(TextView) rowView.findViewById(R.id.YEAR_OF_EVENT);
        holder.tvAnzahlDrivers=(TextView) rowView.findViewById(R.id.ANZAHL_DRIVERS);
        holder.tvDate.setText(events[position].getFormatEventDate());
        holder.tvYearOfEvent.setText(events[position].getFormatEventYear());
        holder.tvOrt.setText(events[position].getEventOrt());
        holder.tvAnzahlDrivers.setText(Integer.toString(events[position].getAnzahlDrivers()) + " Teilnehmer");
        rowView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Toast.makeText(context, "You Clicked "+ events[position].getEventID(), Toast.LENGTH_SHORT).show();
            }
        });
        return rowView;
    }

}
