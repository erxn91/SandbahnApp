package com.example.erxn.sandbahnapp;

public class Driver {

    private int driverID;
    private int startnummer;
    private int punkte;
    private int anzahlrennen;
    private boolean ausgefallen;
    private String name;
    private String machine;
    private int place;

    public Driver(String name, String machine) {
        this.name = name;
        this.machine = machine;
        this.punkte = 0;
        this.ausgefallen = false;
        this.anzahlrennen = 0;
    }

    public Driver(String name, String machine, int startnummer, int driverID) {
        this.driverID = driverID;
        this.startnummer = startnummer;
        this.punkte = 0;
        this.name = name;
        this.machine = machine;
        this.anzahlrennen = 0;
        this.ausgefallen = false;
    }

    //trueleans
    public static boolean isUniq(Driver[] drivers){
        for (int i = 0; i < drivers.length - 1; ++i)
            for (int j = i + 1; j < drivers.length; ++j)
                if (drivers[i].equals(drivers[j])) return false;
        return true;
    }

    //Inhertited, getter, setter
    @Override
    public int hashCode(){
        return this.driverID;
    }

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Driver driver = (Driver) o;

        return driverID == driver.driverID;
    }

    protected int getStartnummer(){
        return startnummer;
    }

    public boolean isAusgefallen(){
        return ausgefallen;
    }

    public void setAusfall(){
        ausgefallen = true;
    }

    public void setAusfall(boolean b){
        ausgefallen = b;
    }

    public void raisePunkte(int num){
        punkte += num;
    }

    protected void incrementAnzahlrennen(){
        ++this.anzahlrennen;
    }

    public String getName() {
        return this.name;
    }

    public String getMachine() {
        return this.machine;
    }

    public int getPlace() {
        return this.place;
    }

    public void setPlace(int place) {
        this.place = place;
    }

    public void setName(String value) {
        this.name = value;
    }

    public void setMachine(String value) {
        this.machine = value;
    }

    public void setDriverID(int value) {
        this.driverID = value;
    }

    public int getDriverID() {
        return this.driverID;
    }

    public void setPunkte(int value) {
        this.punkte = value;
    }

    public int getPunkte() {
        return this.punkte;
    }

    public void setAnzahlrennen(int value) {
        this.anzahlrennen = value;
    }

    public int getAnzahlrennen() {
        return this.anzahlrennen;
    }

    public void setStartnummer(int value) {
        this.startnummer = value;
    }
}
