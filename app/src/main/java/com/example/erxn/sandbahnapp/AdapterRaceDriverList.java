package com.example.erxn.sandbahnapp;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.util.ArrayList;

public class AdapterRaceDriverList extends BaseAdapter {

    Race[] races;
    Context context;

    TextView tvDriverName;
    TextView tvNumberRace;
    TextView tvFirstDriver;
    TextView tvSecondDriver;
    TextView tvThirdDriver;

    RadioGroup rgFirstDriver;
    RadioGroup rgSecondDriver;
    RadioGroup rgThirdDriver;

    int firstDriverPlace;
    int secondDriverPlace;
    int thirdDriverPlace;

    View myView = inflater.inflate(R.layout.complete_race, null);

    private static LayoutInflater inflater = null;
    public AdapterRaceDriverList(Activity someActivity, ArrayList<Race> races) {
        // TODO Auto-generated constructor stub
        this.races = new Race[races.size()];            // Array in Größe der ArrayList
        races.toArray(this.races);                  // ArrayList in Array casten
        context = someActivity;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
                .setView(myView)
                .setPositiveButton("Rennen abschließen", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // RBs auswerten
                        int selectedIDFirst = rgFirstDriver.getCheckedRadioButtonId();
                        int selectedIDSecond = rgSecondDriver.getCheckedRadioButtonId();
                        int selectedIDThird = rgThirdDriver.getCheckedRadioButtonId();
                        RadioButton rbFirstDriver = (RadioButton) myView.findViewById(selectedIDFirst);
                        RadioButton rbSecondDriver = (RadioButton) myView.findViewById(selectedIDSecond);
                        RadioButton rbThirdDriver = (RadioButton) myView.findViewById(selectedIDThird);

                        platzierungenAuswerten(rbFirstDriver, rbSecondDriver, rbThirdDriver);
                    }
                });
        return builder;
    }

    private void platzierungenAuswerten(RadioButton rb1, RadioButton rb2, RadioButton rb3) {
        switch(rb1.getId()) {
            case R.id.RB_FIRST_PLACE1 :
                firstDriverPlace = 0;
                break;
            case R.id.RB_FIRST_PLACE2 :
                firstDriverPlace = 1;
                break;
            case R.id.RB_FIRST_PLACE3 :
                firstDriverPlace = 2;
                break;
            case R.id.RB_FIRST_AUSG :
                firstDriverPlace = -1;
                break;
        }

        switch(rb2.getId()) {
            case R.id.RB_SECOND_PLACE1 :
                secondDriverPlace = 0;
                break;
            case R.id.RB_SECOND_PLACE2 :
                secondDriverPlace = 1;
                break;
            case R.id.RB_SECOND_PLACE3 :
                secondDriverPlace = 2;
                break;
            case R.id.RB_SECOND_AUSG :
                secondDriverPlace = -1;
                break;
        }

        switch(rb3.getId()) {
            case R.id.RB_THIRD_PLACE1 :
                thirdDriverPlace = 0;
                break;
            case R.id.RB_THIRD_PLACE2 :
                thirdDriverPlace = 1;
                break;
            case R.id.RB_THIRD_PLACE3 :
                thirdDriverPlace = 2;
                break;
            case R.id.RB_THIRD_AUSG :
                thirdDriverPlace = -1;
                break;
        }
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
/*
                tvFirstDriver = (TextView) myView.findViewById(R.id.TV_DRIVER_IN_RACE_FIRST);
                tvSecondDriver = (TextView) myView.findViewById(R.id.TV_DRIVER_IN_RACE_SECOND);
                tvThirdDriver = (TextView) myView.findViewById(R.id.TV_DRIVER_IN_RACE_THIRD);

                rgFirstDriver = (RadioGroup) myView.findViewById(R.id.RG_FIRST_PLACE);
                rgSecondDriver = (RadioGroup) myView.findViewById(R.id.RG_SECOND_PLACE);
                rgThirdDriver = (RadioGroup) myView.findViewById(R.id.RG_THIRD_PLACE);

                final Dialog dialog = getBuilder(myView).create();
                dialog.setCanceledOnTouchOutside(true);
                dialog.show();*/
            }
        });
        return rowView;
    }
}