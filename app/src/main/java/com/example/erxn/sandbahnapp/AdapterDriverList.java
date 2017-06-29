/* Adapter, um die Driver eines Events
in einer Liste auszugeben. Dem Konstruktor kann ein
Boolean mitgegeben werden, um zu ermitteln ob pro Listeneintrag
ein Löschen-Button zu sehen ist, der es ermöglicht einzelne
Fahrer zu löschen. Ist das nicht der Fall, so steht an der Stelle
die Startnummer des jeweiligen Fahrers.
 */

package com.example.erxn.sandbahnapp;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.util.ArrayList;

public class AdapterDriverList extends BaseAdapter {

    Driver[] drivers;
    Context context;

    TextView tvDriverName;
    TextView tvDriverMachine;
    TextView tvStartnummer;
    Button btRemoveDriver;
    // Sollen die Fahrer in der Liste löschbar sein?
    boolean driverRemoveable;
    boolean eventIsFinished;

    private static LayoutInflater inflater = null;
    public AdapterDriverList(Activity someActivity, ArrayList<Driver> drivers,
                             boolean driverRemoveable, boolean eventIsFinished) {
        // TODO Auto-generated constructor stub
        this.drivers = new Driver[drivers.size()];      // Array in Größe der ArrayList
        drivers.toArray(this.drivers);                  // ArrayList in Array casten
        context = someActivity;
        this.driverRemoveable = driverRemoveable;
        this.eventIsFinished = eventIsFinished;
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

        // Wenn Driver nicht mehr löschbar sein soll...
        if(!driverRemoveable) {
            btRemoveDriver.setVisibility(View.GONE);
            tvStartnummer.setVisibility(View.VISIBLE);
        }

        // Name des Fahrers an der Stelle im Array
        tvDriverName.setText(drivers[position].getName());
        // Maschine des Fahrers an der Stelle im Array
        tvDriverMachine.setText(drivers[position].getMachine());

        if(!eventIsFinished) {
            // Startnummer des Fahrers an der Stelle im Array
            tvStartnummer.setText("SN: " + drivers[position].getStartnummer());
        }
        else {
            // Wenn Event abgeschlossen ist sollen anstelle der Startnummern,
            // die Punkte der jeweiligen Fahrer stehen!
            tvStartnummer.setText(drivers[position].getPunkte() + " Punkte");
        }

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
            }
        });
        return rowView;
    }
}
