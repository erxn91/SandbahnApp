package com.example.erxn.sandbahnapp;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.util.ArrayList;

public class AdapterRaceDriverList extends BaseAdapter {

    Race[] races;
    Race race;
    Context context;
    Rennverwaltung_mit_ausfallen_v1 rennen;

    TextView tvDriverName;
    TextView tvNumberRace;
    TextView tvFahrer;
    Switch sw_ausfall;
    Button btGoOn;

    View myView;
    Dialog dialog;
    int cntDrivers;

    boolean[] ausfaelle = new boolean[3];

    private static LayoutInflater inflater = null;
    public AdapterRaceDriverList(Activity someActivity,
                                 ArrayList<Race> races,
                                 Rennverwaltung_mit_ausfallen_v1 rennen) {
        // TODO Auto-generated constructor stub
        this.races = new Race[races.size()];            // Array in Größe der ArrayList
        races.toArray(this.races);                  // ArrayList in Array casten
        context = someActivity;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.rennen = rennen;
    }

    @Override
    public int getCount() {
        return races.length;
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

    private AlertDialog.Builder getBuilder(final View myView) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Ergebnis des Rennens")
                .setView(myView);
        return builder;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View rowView;
        rowView = inflater.inflate(R.layout.list_race_pairs, null);
        tvDriverName = (TextView) rowView.findViewById(R.id.TV_PAIRS);
        tvNumberRace = (TextView) rowView.findViewById(R.id.TV_NUMMER_RACE);

        tvDriverName.setText(races[position].toString());
        tvNumberRace.setText("R" + (position + 1));
        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                final int[] places = new int[3];
                cntDrivers = 0;
                race = races[position];
                myView = inflater.inflate(R.layout.complete_race, null);
                tvFahrer = (TextView) myView.findViewById(R.id.TV_ONE_OF_THREE);
                btGoOn = (Button) myView.findViewById(R.id.BUTTON_NEXT_DRIVER);
                btGoOn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RadioGroup rg = (RadioGroup) myView.findViewById(R.id.RG_FAHRER_PLATZ);
                        sw_ausfall = (Switch) myView.findViewById(R.id.SW_AUSGEFALLEN);
                        int selectedID = rg.getCheckedRadioButtonId();

                        switch(selectedID) {
                            case R.id.RB_ERSTER:
                                places[0] = cntDrivers;
                                break;
                            case R.id.RB_ZWEITER:
                                places[1] = cntDrivers;
                                break;
                            case R.id.RB_DRITTER:
                                places[2] = cntDrivers;
                                break;
                        }

                        // wenn alle Plätze verteilt wurden, wird Dialog geschlossen
                        // und Punkte werden verteilt
                        if(cntDrivers == 2) {
                            dialog.hide();
                            rennen.evaluate(places[0], places[1], places[2],
                                    false, false, false);
                        }
                        else {
                            tvFahrer.setText(race.get(++cntDrivers).getName());
                        }
                    }
                });
                if(cntDrivers == 0) {
                    tvFahrer.setText(race.get(cntDrivers).getName());
                }

                dialog = getBuilder(myView).create();
                dialog.setCanceledOnTouchOutside(true);
                dialog.show();
            }
        });
        return rowView;
    }
}