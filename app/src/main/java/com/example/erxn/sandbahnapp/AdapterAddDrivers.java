// Created by erxn on 16.05.2017.

package com.example.erxn.sandbahnapp;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.util.ArrayList;

public class AdapterAddDrivers extends BaseAdapter {

    Driver[] drivers;
    Context context;

    TextView tvDriverName;
    TextView tvDriverMachine;
    Button btRemoveDriver;

    private static LayoutInflater inflater = null;
    public AdapterAddDrivers(Activity someActivity, ArrayList<Driver> drivers) {
        // TODO Auto-generated constructor stub
        this.drivers = new Driver[drivers.size()];      // Array in Größe der ArrayList
        drivers.toArray(this.drivers);                  // ArrayList in Array casten
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
        View rowView;
        rowView = inflater.inflate(R.layout.list_item_add_drivers, null);
        tvDriverName = (TextView) rowView.findViewById(R.id.TV_NAME);
        tvDriverMachine = (TextView) rowView.findViewById(R.id.TV_MACHINE);
        btRemoveDriver = (Button) rowView.findViewById(R.id.BUTTON_REMOVE_DRIVER);

        tvDriverName.setText(drivers[position].getName());
        tvDriverMachine.setText(drivers[position].getMachine());
        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
            }
        });
        return rowView;
    }
}
