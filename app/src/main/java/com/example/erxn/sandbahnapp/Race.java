package com.example.erxn.sandbahnapp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;


public class Race implements Iterable<Driver>, Iterator<Driver>{

    private final Driver[] drivers;
    private int itercnt;

    public Race(Driver driver1,
                Driver driver2,
                Driver driver3){
        this.drivers = new Driver[]{driver1, driver2, driver3};
        itercnt = 0;
    }

    //Rennenausgeben
    public static void rennprint(ArrayList<Race> gezogene_rennen){
        String formatstring = "Rennen %d: %s %s %s\n";

        //Iteration über Rennen
        for (int i = 0; i < gezogene_rennen.size(); ++i){
            Driver[] thisrennen = gezogene_rennen.get(i).drivers;

            System.out.printf(formatstring, i + 1,
                              thisrennen[0].getName(),
                              thisrennen[1].getName(),
                              thisrennen[2].getName());
        }
    }

    public static ArrayList<Race> hoch3(Driver[] drivers){
        HashSet<Race> set = new HashSet<>(drivers.length * drivers.length * drivers.length);
        for (Driver a : drivers)
            for (Driver b : drivers)
                for (Driver c : drivers){
                    Race newrace = new Race(a, b, c);
                    if (newrace.isUniq())
                        set.add(newrace);
                }
        ArrayList<Race> ret = new ArrayList<>(drivers.length * drivers.length * drivers.length);
        ret.addAll(set);
        return ret;
    }

    //Hilfsfunktion zu hoch3
    private boolean isUniq(){
        for (int i = 0; i < drivers.length - 1; ++i)
            for (int j = i + 1; j < drivers.length; ++j)
                if (drivers[i].equals(drivers[j])) return false;
        return true;
    }

    //Inhertited, getter, setter
    @Override
    public Iterator<Driver> iterator(){
        return this;
    }

    @Override
    public boolean hasNext(){
        if (itercnt>=3){
            itercnt = 0;
            return false;
        }
        return true;
    }

    @Override
    public Driver next(){
        return drivers[itercnt++];
    }


    public Driver get(int index){
        return drivers[index];
    }

    public boolean idInRace(int id){
        for (Driver d : drivers)
            if (d.getDriverID() == id)
                return true;
        return false;
    }

    @Override
    public int hashCode(){
        return Arrays.hashCode(drivers);
    }

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Race race = (Race) o;

        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        return Arrays.equals(drivers, race.drivers);
    }

    @Override
    public String toString(){
        String driversStr = "";
        int cnt = 0;
        for (Driver driver : drivers) {
            driversStr += driver.getName() + " - " + driver.getStartnummer();
            if(++cnt < 3) driversStr += "\n";
        }
        return driversStr;
    }
}