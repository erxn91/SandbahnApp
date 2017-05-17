// Created by erxn on 26.04.2017.

package com.example.erxn.sandbahnapp;

import java.util.ArrayList;
import java.util.Calendar;

public class Event {

    private int eventID;
    private Calendar cal;
    private String eventDate;
    private String eventOrt;
    private ArrayList<Driver> listDriver;
    private int anzahlDrivers;
    private int finished = 0;       // DB kennt kein boolean... deshalb int!


    public Event() {
        listDriver = new ArrayList<Driver>();
        cal = Calendar.getInstance ();
        setEventDate(cal);

        this.eventOrt = "Stuttgart";
        this.anzahlDrivers = 55;
    }

    private void setEventDate(Calendar calendar) {
        eventDate = cal.get(Calendar.DAY_OF_MONTH) + "." +
                (cal.get(Calendar.MONTH) + 1) + "." +
                cal.get(Calendar.YEAR);
    }

    public void addDriver(Driver driver) {
        listDriver.add(driver);
    }

    public void removeDriver(int index) {
        listDriver.remove(index);
    }

    public ArrayList<Driver> getDrivers() {
        return this.listDriver;
    }

    public int getEventID() {
        return this.eventID;
    }

    public void setEventID(int id) {
        this.eventID = id;
    }

    public String getEventDate() {
        return eventDate;
    }

    // Methode um das Event-Datum, passend formatiert für die ListView, zurückzugeben
    public String getFormatEventDate() {
        String eventFormat = "";
        String[] months = {"JAN", "FEB", "MÄR", "APR", "MAI", "JUN", "JUL", "AUG", "SEP", "OKT", "NOV", "DEZ"};

        eventFormat = cal.get(Calendar.DAY_OF_MONTH) + "." + months[cal.get(Calendar.MONTH)];
        return eventFormat;
    }

    // Methode um das Event-Jahr, passend formatiert für die ListView, zurückzugeben
    public String getFormatEventYear() {
        String formatEventYear = Integer.toString(cal.get(Calendar.YEAR));
        return "'" + formatEventYear.charAt(2) + formatEventYear.charAt(3);
    }

    public int isFinished() {
        return this.finished;
    }

    public void setFinished() {
        this.finished = 1;
    }

    public void setUnfinished() {
        this.finished = 0;
    }

    public int getAnzahlDrivers() {
        return this.anzahlDrivers;
    }

    public void setAnzahlDrivers(int value) {
        this.anzahlDrivers = value;
    }

    public String getEventOrt() {
        return this.eventOrt;
    }

    public Event setEventOrt(String value) {
        this.eventOrt = value;
        return this;
    }

    public void setEventDateFromDB(String value) {
        this.eventDate = value;
    }
}
