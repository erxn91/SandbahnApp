// Created by erxn on 27.04.2017.

package com.example.erxn.sandbahnapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;


public class MyDBManager extends SQLiteOpenHelper{

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Finished_Events.db";

    // Aufbau der Tabelle
    public static final String TABELLE_EVENT = "Event";
    public static final String SPALTE_EVENT_ID = "ID";
    public static final String SPALTE_EVENT_DATUM = "EventDatum";
    public static final String SPALTE_EVENT_ORT = "EventOrt";
    public static final String SPALTE_ANZAHL_DRIVERS = "AnzahlFahrer";
    public static final String SPALTE_FINISHED = "isFinished";

    public MyDBManager(Context cxt) {
        super(cxt, DATABASE_NAME, null, DATABASE_VERSION);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    // Daten des Events in die verschiedenen Spalten schreiben
    public boolean insertEvent(Event event) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues neueZeile = new ContentValues();
            neueZeile.put(SPALTE_EVENT_DATUM, event.getFormatEventDate() + "." + event.getFormatEventYear());
            neueZeile.put(SPALTE_EVENT_ORT, event.getEventOrt());
            neueZeile.put(SPALTE_ANZAHL_DRIVERS, event.getAnzahlDrivers());
            neueZeile.put(SPALTE_FINISHED, event.isFinished());
            // db.insert liefert -1, wenn insert fehlgeschlagen
            long result = db.insert(TABELLE_EVENT, null, neueZeile);
            if(result == -1) {
                return false;
            }
            else return true;
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

        try {
            while(meinZeiger.moveToNext()) {
                Event e = new Event();
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
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP IF EXISTS " + TABELLE_EVENT);
        onCreate(db);
    }
}
