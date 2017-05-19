// Created by erxn on 26.04.2017.

package com.example.erxn.sandbahnapp;

import java.util.ArrayList;
import java.util.Calendar;

public class Event {

    private int eventID;
    private Calendar cal;
    private String eventDate;
    private String eventYear;
    private String eventOrt;
    private ArrayList<Driver> listDriver;
    private int anzahlDrivers;
    private boolean isCustom;
    private int finished = 0;       // Event abgeschlossen?


    public Event() {
        listDriver = new ArrayList<Driver>();
        cal = Calendar.getInstance ();
        this.eventOrt = "kein Ort hinzugefügt";
        this.anzahlDrivers = 0;
    }

    public void setEventDate() {
        this.eventDate = getFormatEventDate();
    }

    public void setDrivers(ArrayList<Driver> drivers) {
        this.listDriver = drivers;
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
    private String getFormatEventDate() {
        String eventFormat = "";
        String[] months = {"JAN", "FEB", "MÄR", "APR", "MAI", "JUN", "JUL", "AUG", "SEP", "OKT", "NOV", "DEZ"};

        eventFormat = cal.get(Calendar.DAY_OF_MONTH) + "." + months[cal.get(Calendar.MONTH)];
        return eventFormat;
    }

    // Methode um das Event-Jahr, passend formatiert für die ListView, zurückzugeben
    private String getFormatEventYear() {
        String formatEventYear = Integer.toString(cal.get(Calendar.YEAR));
        return "'" + formatEventYear.charAt(2) + formatEventYear.charAt(3);
    }

    public void setEventYear() {
        this.eventYear = getFormatEventYear();
    }

    public String getEventYear() {
        return this.eventYear;
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

    public void isFast() {
        this.isCustom = false;
    }

    public boolean eventTypeIsCustom() {
        return this.isCustom;
    }

    public void isCustom() {
        this.isCustom = true;
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
