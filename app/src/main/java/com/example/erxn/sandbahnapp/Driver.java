// Created by erxn on 16.05.2017.

package com.example.erxn.sandbahnapp;

public class Driver {

    private String name;
    private String machine;
    private int place;

    public Driver(String name, String machine) {
        this.name = name;
        this.machine = machine;
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
}
