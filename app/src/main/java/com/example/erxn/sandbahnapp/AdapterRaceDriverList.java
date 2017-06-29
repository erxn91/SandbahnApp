package com.example.erxn.sandbahnapp;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

public class AdapterRaceDriverList extends BaseAdapter {

    Driver[] drivers;
    Context context;

    TextView tvDriverName;
    TextView tvPunkte;

    private static LayoutInflater inflater = null;
    public AdapterRaceDriverList(Activity someActivity,
                                 Driver[] drivers) {
        // TODO Auto-generated constructor stub
        this.drivers = drivers;
        context = someActivity;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return drivers.length;
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
        final View rowView;
        rowView = inflater.inflate(R.layout.list_driver_places, null);
        tvDriverName = (TextView) rowView.findViewById(R.id.TV_DRIVER_NAME);
        tvPunkte = (TextView) rowView.findViewById(R.id.TV_DRIVER_PLACE);

        tvDriverName.setText(drivers[position].getName());
        tvPunkte.setText(drivers[position].getPunkte() + " Punkte");

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
            }
        });
        return rowView;
    }
}