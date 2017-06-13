package com.example.erxn.sandbahnapp;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import org.w3c.dom.Text;

import java.util.ArrayList;

public class AdapterDriverList extends BaseAdapter {

    Driver[] drivers;
    Context context;

    TextView tvDriverName;
    TextView tvDriverMachine;
    TextView tvStartnummer;
    Button btRemoveDriver;
    boolean driverRemoveable;       // Sollen die Fahrer in der Liste löschbar sein?

    private static LayoutInflater inflater = null;
    public AdapterDriverList(Activity someActivity, ArrayList<Driver> drivers, boolean driverRemoveable) {
        // TODO Auto-generated constructor stub
        this.drivers = new Driver[drivers.size()];      // Array in Größe der ArrayList
        drivers.toArray(this.drivers);                  // ArrayList in Array casten
        context = someActivity;
        this.driverRemoveable = driverRemoveable;
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
        tvStartnummer = (TextView) rowView.findViewById(R.id.TV_STARTNUMMER);
        btRemoveDriver = (Button) rowView.findViewById(R.id.BUTTON_DELETE_DRIVER);

        if(!driverRemoveable) {
            btRemoveDriver.setVisibility(View.GONE);
            tvStartnummer.setVisibility(View.VISIBLE);
        }

        tvDriverName.setText(drivers[position].getName());
        tvDriverMachine.setText(drivers[position].getMachine());
        tvStartnummer.setText("SN: " + drivers[position].getStartnummer());

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
            }
        });
        return rowView;
    }
}
