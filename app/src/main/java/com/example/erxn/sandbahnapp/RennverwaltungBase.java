package com.example.erxn.sandbahnapp;

import java.util.ArrayList;
import java.util.Iterator;


public abstract class RennverwaltungBase implements Iterator<Race>, Iterable<Race>{

    protected int drivernum;
    protected int renncnt;
    protected Driver[] drivers;
    protected ArrayList<Race> moegliche_rennen;
    protected ArrayList<Race> gezogene_rennen;

    protected static char max(char[] arr){
        char ret = arr[0];
        for (char a : arr)
            ret = a > ret ? a : ret;
        return ret;
    }

    protected static int max(int[] arr){
        int ret = arr[0];
        for (int a : arr)
            ret = a > ret ? a : ret;
        return ret;
    }

    protected static long max(long[] arr){
        long ret = arr[0];
        for (long a : arr)
            ret = a > ret ? a : ret;
        return ret;
    }

    protected static float max(float[] arr){
        float ret = arr[0];
        for (float a : arr)
            ret = a > ret ? a : ret;
        return ret;
    }

    protected static double max(double[] arr){
        double ret = arr[0];
        for (double a : arr)
            ret = a > ret ? a : ret;
        return ret;
    }

    protected static int maxRacenum(ArrayList<Driver> arr){
        if (arr.isEmpty())
            return -1;
        int ret = arr.get(0).getAnzahlrennen();
        for (Driver d : arr){
            int a = d.getAnzahlrennen();
            ret = a > ret ? a : ret;
        }
        return ret;
    }

    protected static int maxId(ArrayList<Driver> arr){
        if (arr.isEmpty())
            return -1;
        int ret = arr.get(0).getDriverID();
        for (Driver d : arr){
            int a = d.getDriverID();
            ret = a > ret ? a : ret;
        }
        return ret;
    }

    protected static int maxDriverId(ArrayList<Race> arr){
        ArrayList<Driver> ret = new ArrayList<>();
        for (Race r : arr)
            for (Driver d : r)
                ret.add(d);
        return maxId(ret);
    }

    protected static char min(char[] arr){
        char ret = arr[0];
        for (char a : arr)
            ret = a < ret ? a : ret;
        return ret;
    }

    protected static int min(int[] arr){
        int ret = arr[0];
        for (int a : arr)
            ret = a < ret ? a : ret;
        return ret;
    }

    protected static long min(long[] arr){
        long ret = arr[0];
        for (long a : arr)
            ret = a < ret ? a : ret;
        return ret;
    }

    protected static float min(float[] arr){
        float ret = arr[0];
        for (float a : arr)
            ret = a < ret ? a : ret;
        return ret;
    }

    protected static double min(double[] arr){
        double ret = arr[0];
        for (double a : arr)
            ret = a < ret ? a : ret;
        return ret;
    }

    protected static int minRacenum(ArrayList<Driver> arr){
        if (arr.isEmpty())
            return -1;
        int ret = arr.get(0).getAnzahlrennen();
        for (Driver d : arr){
            int a = d.getAnzahlrennen();
            ret = a < ret ? a : ret;
        }
        return ret;
    }

    protected static int minId(ArrayList<Driver> arr){
        if (arr.isEmpty())
            return -1;
        int ret = arr.get(0).getDriverID();
        for (Driver d : arr){
            int a = d.getDriverID();
            ret = a < ret ? a : ret;
        }
        return ret;
    }

    protected static int minDriverId(ArrayList<Race> arr){
        ArrayList<Driver> ret = new ArrayList<>();
        for (Race r : arr)
            for (Driver d : r)
                ret.add(d);
        return minId(ret);
    }

    protected static boolean isIn(Driver o,
                                  Race a){
        for (Driver elem : a)
            if (o.equals(elem)) return true;
        return false;
    }

    protected static boolean einerFaehrtZweiMalHintereinander(ArrayList<Race> rennen){
        for (int i = 0; i < rennen.size() - 1; ++i)
            for (Driver driver : rennen.get(i))
                if (isIn(driver, rennen.get(i + 1))) return true;
        return false;
    }

    //Iterable Interface
    @Override
    public final Iterator<Race> iterator(){
        return this;
    }

    //Iterator Interface
    @Override
    public abstract boolean hasNext();

    @Override
    public abstract Race next();
}
