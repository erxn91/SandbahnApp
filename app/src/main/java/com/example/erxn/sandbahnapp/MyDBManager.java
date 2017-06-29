package com.example.erxn.sandbahnapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;


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

    // Aufbau der Tabelle Driver
    private static final String TABELLE_DRIVERS = "Fahrer";
    private static final String SPALTE_DRIVER_ID = "ID";
    private static final String SPALTE_DRIVER_STARTNUM = "Startnummer";
    private static final String SPALTE_DRIVER_NAME = "Name";
    private static final String SPALTE_DRIVER_MACHINE = "Maschine";
    private static final String SPALTE_DRIVER_PLACE = "Platzierung";
    private static final String SPALTE_DRIVER_POINTS = "Punkte";
    private static final String SPALTE_EVENT_ID_FK = "Event_ID";

    private Context cxt;

    public MyDBManager(Context cxt) {
        super(cxt, DATABASE_NAME, null, DATABASE_VERSION);
        this.cxt = cxt;
        SQLiteDatabase db = this.getWritableDatabase();
    }

    public void deleteTables() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE " + TABELLE_EVENT);
        db.execSQL("DROP TABLE " + TABELLE_DRIVERS);
    }


    /* Methoden für die Event Tabelle -------------------------------
    -----------------------------------------------------------------
     */

    // Daten des Events in die verschiedenen Spalten schreiben
    public boolean insertEvent(Event event) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues neueZeile = new ContentValues();
        neueZeile.put(SPALTE_EVENT_DATUM, event.getEventDate() + event.getEventYear());
        neueZeile.put(SPALTE_EVENT_ORT, event.getEventOrt());
        neueZeile.put(SPALTE_ANZAHL_DRIVERS, event.getAnzahlDrivers());
        neueZeile.put(SPALTE_FINISHED, event.isFinished());
        // db.insert liefert -1, wenn insert fehlgeschlagen
        long result = db.insert(TABELLE_EVENT, null, neueZeile);
        if(result == -1) {
            return false;
        }
        else {
            int eventID = (int)result;
            insertDrivers(event, eventID);
            return true;
        }
    }

    // wenn keine EventID übergeben wird, werden allen Events,
    // wo die Spalte_FINISHED noch 0 ist auf 1 gesetzt
    // somit sind die Events abgeschlossen
    public void finishEvents() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SPALTE_FINISHED, 1);
        String where = SPALTE_FINISHED + "= 0";
        db.update(TABELLE_EVENT, values, where, null);
    }

    // hier kann mithilfe der eventID ein einzelnes Event abgeschlossen werden
    public void finishEvents(int eventID) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SPALTE_FINISHED, 1);
        String where = SPALTE_FINISHED + "= 0" + " AND " + SPALTE_EVENT_ID + "= " + Integer.toString(eventID);
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

    // Einzelnes Event aus Datenbank holen und zurückgeben
    public Event getOneEvent(int eventID) {
        // in selectOneEvent wird ein Zeiger zurückgegeben
        Cursor meinZeiger = selectOneEvent(eventID);

        // neue Instanz eines Events erstellen und alle relevanten
        // Daten aus dem Zeiger ins Event einfügen
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

    public void deleteEvent(int eventID) {
        SQLiteDatabase db = this.getWritableDatabase();
        // Event in der DB löschen
        db.execSQL("DELETE FROM " + TABELLE_EVENT +
                   " WHERE " + SPALTE_EVENT_ID + "= " + eventID);

        // Driver des Events in der DB löschen
        db.execSQL("DELETE FROM " + TABELLE_DRIVERS +
                   " WHERE " + SPALTE_EVENT_ID_FK + "= " + eventID);
    }



    /* Methoden für die Drivers Tabelle ----------------------------
    ----------------------------------------------------------------
     */

    // Ähnlich zu den Event inserts werden hier Driver in die
    // DB geschrieben
    private boolean insertDrivers(Event event, int eventID) {
        ArrayList<Driver> drivers = event.getDrivers();

        SQLiteDatabase db = this.getWritableDatabase();
        for(Driver driver : drivers) {
            ContentValues neueZeile = new ContentValues();
            neueZeile.put(SPALTE_DRIVER_STARTNUM, driver.getStartnummer());
            neueZeile.put(SPALTE_DRIVER_NAME, driver.getName());
            neueZeile.put(SPALTE_DRIVER_MACHINE, driver.getMachine());
            neueZeile.put(SPALTE_DRIVER_PLACE, driver.getPlace());
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
                " WHERE " + SPALTE_EVENT_ID_FK + "= " + id +
                " ORDER BY " + SPALTE_DRIVER_POINTS + " DESC", null);
        return meinZeiger;
    }

    // Die Punkte der einzelnen Driver in DB schreiben,
    // Driver werden mit Driver-Array übergeben
    public void addDriverRacePoints(Driver[] drivers) {
        Arrays.sort(drivers, new Comparator<Driver>() {
            @Override
            public int compare(Driver o1, Driver o2) {
                int n1 = o1.getPunkte();
                int n2 = o2.getPunkte();
                return n2-n1;
            }
        });
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        for(Driver d : drivers) {
            int pts = d.getPunkte();
            int driverID = d.getDriverID();
            values.put(SPALTE_DRIVER_POINTS, pts);
            String where =  SPALTE_DRIVER_ID + "= " + driverID;
            db.update(TABELLE_DRIVERS, values, where, null);
        }
    }

    // alle Driver, die zu dem jeweiligen Event gehören
    // werden aus der DB geholt
    public ArrayList<Driver> getDriversOfEvent(int eventID) {
        ArrayList<Driver> drivers = new ArrayList<>();
        Cursor meinZeiger = selectDriversOfEvent(eventID);

        // Cursor auf Position -1 setzen, sodass er im while-loop vorne beginnt
        meinZeiger.moveToPosition(-1);

        try {
            while(meinZeiger.moveToNext()) {
                int driverStartNum = meinZeiger.getInt(meinZeiger.getColumnIndex(SPALTE_DRIVER_STARTNUM));
                String driverName = meinZeiger.getString(meinZeiger.getColumnIndex(SPALTE_DRIVER_NAME));
                String driverMachine = meinZeiger.getString(meinZeiger.getColumnIndex(SPALTE_DRIVER_MACHINE));
                int driverPoints = meinZeiger.getInt(meinZeiger.getColumnIndex(SPALTE_DRIVER_POINTS));
                int driverID = meinZeiger.getInt(meinZeiger.getColumnIndex(SPALTE_DRIVER_ID));
                Driver driver = new Driver(driverName, driverMachine, driverStartNum, driverID);
                driver.setPunkte(driverPoints);
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
                // Aufbau und Spezifizierung der Event Tabelle
                "CREATE TABLE " + TABELLE_EVENT + " (" +
                        SPALTE_EVENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        SPALTE_EVENT_DATUM + " TEXT," +
                        SPALTE_EVENT_ORT+ " TEXT," +
                        SPALTE_ANZAHL_DRIVERS + " INTEGER," +
                        SPALTE_FINISHED + " INTEGER" +
                        ")"
        );

        db.execSQL(
                // Aufbau und Spezifizierung der Driver Tabelle
                "CREATE TABLE " + TABELLE_DRIVERS + " (" +
                        SPALTE_DRIVER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        SPALTE_DRIVER_STARTNUM + " INTEGER," +
                        SPALTE_DRIVER_NAME + " TEXT," +
                        SPALTE_DRIVER_MACHINE + " TEXT," +
                        SPALTE_DRIVER_PLACE + " INTEGER," +
                        SPALTE_DRIVER_POINTS + " INTEGER," +
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
