// Created by erxn on 27.04.2017.

package com.example.erxn.sandbahnapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;


public class MyDBManager extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Finished_Events.db";

    // Aufbau der Tabelle Event
    private static final String TABELLE_EVENT = "Event";
    private static final String SPALTE_EVENT_ID = "ID";
    private static final String SPALTE_EVENT_DATUM = "EventDatum";
    private static final String SPALTE_EVENT_ORT = "EventOrt";
    private static final String SPALTE_ANZAHL_DRIVERS = "AnzahlFahrer";
    private static final String SPALTE_FINISHED = "isFinished";

    private static final String TABELLE_DRIVERS = "Fahrer";
    private static final String SPALTE_DRIVER_ID = "ID";
    private static final String SPALTE_DRIVER_NAME = "Name";
    private static final String SPALTE_DRIVER_MACHINE = "Maschine";
    private static final String SPALTE_EVENT_ID_FK = "Event_ID";

    public MyDBManager(Context cxt) {
        super(cxt, DATABASE_NAME, null, DATABASE_VERSION);
        SQLiteDatabase db = this.getWritableDatabase();
    }


    /* Methoden für die Event Tabelle -------------------------------
    -----------------------------------------------------------------
     */

    // Daten des Events in die verschiedenen Spalten schreiben
    public boolean insertEvent(Event event) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues neueZeile = new ContentValues();
        neueZeile.put(SPALTE_EVENT_DATUM, event.getFormatEventDate());
        neueZeile.put(SPALTE_EVENT_ORT, event.getEventOrt());
        neueZeile.put(SPALTE_ANZAHL_DRIVERS, event.getAnzahlDrivers());
        neueZeile.put(SPALTE_FINISHED, event.isFinished());
        // db.insert liefert -1, wenn insert fehlgeschlagen
        long result = db.insert(TABELLE_EVENT, null, neueZeile);
        if(result == -1) {
            return false;
        }
        else {
            Cursor meinZeiger;
            meinZeiger = db.rawQuery("SELECT * FROM " + TABELLE_EVENT, null);
            int idCounter = meinZeiger.getCount();
            insertDrivers(event, idCounter);
            return true;
        }
    }

    public void finishEvent() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SPALTE_FINISHED, 1);
        String where = SPALTE_FINISHED + "= 0";
        db.update(TABELLE_EVENT, values, where, null);
    }

    // einzelnes Event über EventID selektieren
    private Cursor selectOneEvent(int eventID) {
        String ID = Integer.toString(eventID);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor meinZeiger;
        meinZeiger = db.rawQuery("SELECT * FROM " + TABELLE_EVENT +
                " WHERE " + SPALTE_EVENT_ID + "= " + ID, null);

        meinZeiger.moveToFirst();
        return meinZeiger;
    }

    public Event getOneEvent(int eventID) {
        Cursor meinZeiger = selectOneEvent(eventID);
        Event e = new Event();
        e.setEventID(meinZeiger.getInt(meinZeiger.getColumnIndex(SPALTE_EVENT_ID)));
        e.setEventDateFromDB(meinZeiger.getString(meinZeiger.getColumnIndex(SPALTE_EVENT_DATUM)));
        e.setEventOrt(meinZeiger.getString(meinZeiger.getColumnIndex(SPALTE_EVENT_ORT)));
        e.setAnzahlDrivers(meinZeiger.getInt(meinZeiger.getColumnIndex(SPALTE_ANZAHL_DRIVERS)));
        meinZeiger.close();
        return e;
    }

    // Cursor auf finished oder unfinished Events setzen
    private Cursor selectEvents(boolean finished) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor meinZeiger;
        if(finished) {
            meinZeiger = db.rawQuery("SELECT * FROM " + TABELLE_EVENT +
                    " WHERE " + SPALTE_FINISHED + "= 1", null);
        }
        else {
            meinZeiger = db.rawQuery("SELECT * FROM " + TABELLE_EVENT +
                    " WHERE " + SPALTE_FINISHED + "= 0", null);
        }
        meinZeiger.moveToFirst();
        return meinZeiger;
    }

    // gibt Events in ArrayList zurück, ---> Auswahl ob finished oder unfinished Events
    public ArrayList<Event> getEvents(boolean finished) {
        ArrayList<Event> events = new ArrayList<>();    // Liste der Events
        Cursor meinZeiger = selectEvents(finished);     // true gibt finished, false gibt unfinished Events zurück

        // Cursor auf Position -1 setzen, sodass er im while-loop vorne beginnt
        meinZeiger.moveToPosition(-1);

        try {
            while(meinZeiger.moveToNext()) {
                Event e = new Event();
                e.setEventID(meinZeiger.getInt(meinZeiger.getColumnIndex(SPALTE_EVENT_ID)));
                e.setEventDateFromDB(meinZeiger.getString(meinZeiger.getColumnIndex(SPALTE_EVENT_DATUM)));
                e.setEventOrt(meinZeiger.getString(meinZeiger.getColumnIndex(SPALTE_EVENT_ORT)));
                e.setAnzahlDrivers(meinZeiger.getInt(meinZeiger.getColumnIndex(SPALTE_ANZAHL_DRIVERS)));
                events.add(e);
            }
        } finally {
            meinZeiger.close();
        }
        return events;
    }



    /* Methoden für die Drivers Tabelle ----------------------------
    ----------------------------------------------------------------
     */

    private boolean insertDrivers(Event event, int eventID) {
        ArrayList<Driver> drivers = event.getDrivers();

        SQLiteDatabase db = this.getWritableDatabase();
        for(Driver driver : drivers) {
            ContentValues neueZeile = new ContentValues();
            neueZeile.put(SPALTE_DRIVER_NAME, driver.getName());
            neueZeile.put(SPALTE_DRIVER_MACHINE, driver.getMachine());
            neueZeile.put(SPALTE_EVENT_ID_FK, eventID);
            long result = db.insert(TABELLE_DRIVERS, null, neueZeile);
            if(result == -1) {
                return false;
            }
        }
        return true;
    }

    private Cursor selectDriversOfEvent(int eventID) {
        SQLiteDatabase db = this.getWritableDatabase();
        String id = Integer.toString(eventID);
        Cursor meinZeiger;
        meinZeiger = db.rawQuery("SELECT * FROM " + TABELLE_DRIVERS +
                " WHERE " + SPALTE_EVENT_ID_FK + "= " + id, null);
        return meinZeiger;
    }

    public ArrayList<Driver> getDriversOfEvent(int eventID) {
        ArrayList<Driver> drivers = new ArrayList<Driver>();
        Cursor meinZeiger = selectDriversOfEvent(eventID);

        // Cursor auf Position -1 setzen, sodass er im while-loop vorne beginnt
        meinZeiger.moveToPosition(-1);

        try {
            while(meinZeiger.moveToNext()) {
                String driverName = meinZeiger.getString(meinZeiger.getColumnIndex(SPALTE_DRIVER_NAME));
                String driverMachine = meinZeiger.getString(meinZeiger.getColumnIndex(SPALTE_DRIVER_MACHINE));
                Driver driver = new Driver(driverName, driverMachine);
                drivers.add(driver);
            }
        } finally {
            meinZeiger.close();
        }
        return drivers;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE " + TABELLE_EVENT + " (" +
                        SPALTE_EVENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        SPALTE_EVENT_DATUM + " TEXT," +
                        SPALTE_EVENT_ORT+ " TEXT," +
                        SPALTE_ANZAHL_DRIVERS + " INTEGER," +
                        SPALTE_FINISHED + " INTEGER" +
                        ")"
        );

        db.execSQL(
                "CREATE TABLE " + TABELLE_DRIVERS + " (" +
                        SPALTE_DRIVER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        SPALTE_DRIVER_NAME + " TEXT," +
                        SPALTE_DRIVER_MACHINE + " TEXT," +
                        SPALTE_EVENT_ID_FK + " INTEGER" +
                        ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP IF EXISTS " + TABELLE_EVENT);
        onCreate(db);
    }
}
