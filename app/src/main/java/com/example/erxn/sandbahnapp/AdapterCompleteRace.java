package com.example.erxn.sandbahnapp;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

public class AdapterCompleteRace extends BaseAdapter {

    Race race;
    Context context;

    TextView tvDriverName;

    private static LayoutInflater inflater = null;
    public AdapterCompleteRace(Context someActivity, Race race) {
        // TODO Auto-generated constructor stub
        this.race = race;
        context = someActivity;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return 3;
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
        rowView = inflater.inflate(R.layout.complete_race, null);
        //tvDriverName = (TextView) rowView.findViewById(R.id.TV_DRIVER_IN_RACE);

        //tvDriverName.setText(race.get(position).getName());

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
            }
        });
        return rowView;
    }
}
