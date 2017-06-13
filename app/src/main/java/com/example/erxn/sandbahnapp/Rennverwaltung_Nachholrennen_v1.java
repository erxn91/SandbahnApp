package com.example.erxn.sandbahnapp;

import java.util.ArrayList;
import java.util.Collections;


public class Rennverwaltung_Nachholrennen_v1 extends RennverwaltungMitAusfallenBase{


    private ArrayList<Race> verbotene_rennen;
    private ArrayList<Race> gefahrene_rennen;
    private ArrayList<Driver> finishedDrivers;
    private ArrayList<Driver> leftoutDrivers;
    private Race nextRace;

    public Rennverwaltung_Nachholrennen_v1(Driver[] drivers,
                                           ArrayList<Driver> leftoutDrivers,
                                           ArrayList<Driver> finishedDrivers,
                                           ArrayList<Race> gefahrene_rennen){
        drivernum = 0;
        renncnt = 0;
        this.drivers = drivers;
        this.finishedDrivers = finishedDrivers;
        this.leftoutDrivers = leftoutDrivers;
        this.gefahrene_rennen = gefahrene_rennen;
        nextRace = null;

        computeMoeglicheRennen();
        verbotene_rennen = new ArrayList<>();
        gefahrene_rennen = new ArrayList<>();
    }

    private void computeMoeglicheRennen(){
        Driver[] leftoutDriversCopy = new Driver[leftoutDrivers.size()];
        for (int i = 0; i < leftoutDriversCopy.length; ++i){
            leftoutDriversCopy[i] = leftoutDrivers.get(i);
        }
        moegliche_rennen = Race.hoch3(leftoutDriversCopy);
        //Hier kommt (einmalig!) der Zufall beim nachholen der Rennen rein...
        Collections.shuffle(moegliche_rennen);
    }

    @Override
    public boolean hasNext(){
        //Gibt es noch Fahrer mit weniger als 3 Rennen?
        if (leftoutDrivers.isEmpty())
            return false;
        //Bilde Tripel
        computeMoeglicheRennen();
        //Ist ein Tripel gültig? Dann lass die 3 gegeneinander fahren.
        for (Race r : moegliche_rennen){
            if (isValidRace(r)){
                nextRace = r;
                return true;
            }
        }

        //Hier kommt wieder Zufall rein, diesmal auf Ebene der Driver
        Collections.shuffle(leftoutDrivers);
        System.out.println("2er Paar finden");
        //Gibt es 2 Driver, die noch gegeneinander antreten dürfen?
        Driver[] pairableDrivers = getPairOfPairableDrivers();

        //Wenn ja, finde einen Driver, der noch mal gegen die beiden fährt
        if (pairableDrivers != null){
            System.out.println("Mit 1 füllen");
            nextRace = fillwith1fromfinished(pairableDrivers);
            return true;
        }
        //Wenn nein, finde 2 Driver, die gegen den erstbesten (geshuffleten) Driver fahren.
        else{
            System.out.println("Mit 2 füllen");
            nextRace = fillwith2fromfinished(leftoutDrivers.get(0));
            return true;
        }
    }

    @Override
    public Race next(){
        return nextRace;
    }

    @Override
    public void reactivate(int startnummer){
        for (Driver d : drivers){
            if (d.getStartnummer() == startnummer)
                d.setAusfall(false);
            if (d.getAnzahlrennen() < 3)
                leftoutDrivers.add(d);
        }
    }

    @Override
    public void evaluate(int winner,
                         int second,
                         int third,
                         boolean ausgeschieden0,
                         boolean ausgeschieden1,
                         boolean ausgeschieden2){
        //Soeben gefahrenes, gültiges Rennen.
        Race thisrace = next();
        //Spieler ausscheiden lassen
        if (ausgeschieden0)
            thisrace.get(0).setAusfall();
        if (ausgeschieden1)
            thisrace.get(1).setAusfall();
        if (ausgeschieden2)
            thisrace.get(2).setAusfall();

        //Gefahrenes Rennen umbewegen
        //gezogene_rennen.remove(thisrace);
        gefahrene_rennen.add(thisrace);

        //Ausgefallene Fahrer rauswerfen, ansonsten Punkte zuweisen
        for (Driver d : thisrace)
            if (d.isAusgefallen())
                leftoutDrivers.remove(d);
            else{
                d.incrementAnzahlrennen();
                if (d == thisrace.get(winner) && d.getAnzahlrennen() < 4)
                    d.raisePunkte(3);
                if (d == thisrace.get(second) && d.getAnzahlrennen() < 4)
                    d.raisePunkte(2);
                if (d == thisrace.get(third) && d.getAnzahlrennen() < 4)
                    d.raisePunkte(1);
                if (d.getAnzahlrennen() >= 3){
                    leftoutDrivers.remove(d);
                    finishedDrivers.add(d);
                }
            }

    }
    //Hilfsfunktionen zu hasNext

    private Race fillwith2fromfinished(Driver driver1){
        Collections.shuffle(finishedDrivers);
        double ε = 1;
        int acceptableAnzahl = 0;
        ArrayList<Driver> copyOfFinishedDrivers = new ArrayList<>();
        copyOfFinishedDrivers.addAll(finishedDrivers);

        //Zweitkleinsten Wert bestimmen.
        //Bestimme zuerst kleinsten Wert, entferne alle Einträge dieses Wertes, hole den neuen kleinsten Wert.
        int minval = minRacenum(copyOfFinishedDrivers);
        for (int i = 0; i < copyOfFinishedDrivers.size(); ++i)
            if (copyOfFinishedDrivers.get(i).getAnzahlrennen() == minval)
                copyOfFinishedDrivers.remove(i--);

        //Wenn alle Werte gleich waren, ist die Liste leer. Als Fallback wird -1 zurückgegeben und
        //der vorherige minval wird neu berechnet.
        minval = minRacenum(copyOfFinishedDrivers);
        if (minval == -1)
            minval = minRacenum(finishedDrivers);

        while (true){
            for (Driver a : finishedDrivers)
                for (Driver b : finishedDrivers){
                    double avg = (a.getPunkte() + b.getPunkte()) / 2.0;
                    if (a.getAnzahlrennen() < minval &&
                            b.getAnzahlrennen() < minval &&
                            Math.abs(avg - driver1.getPunkte()) < ε
                            )
                        return new Race(driver1, a, b);
                }
            ε += 0.5;
            if (ε > 9){
                Collections.shuffle(finishedDrivers);
                if (minval > 1000){
                    return new Race(driver1, finishedDrivers.get(0), finishedDrivers.get(finishedDrivers.size() - 1));
                }
                for (Driver a : finishedDrivers)
                    for (Driver b : finishedDrivers)
                        if (a.getAnzahlrennen() < minval &&
                                b.getAnzahlrennen() < minval &&
                                !a.equals(b))
                            return new Race(driver1, a, b);
                ++minval;
            }
        }
    }

    private Race fillwith1fromfinished(Driver[] pairableDrivers){
        Collections.shuffle(finishedDrivers);
        double ε = 1;
        double avg = (pairableDrivers[0].getPunkte() + pairableDrivers[1].getPunkte()) / 2.0;

        ArrayList<Driver> copyOfFinishedDrivers = new ArrayList<>();
        copyOfFinishedDrivers.addAll(finishedDrivers);
        //Zweitkleinsten Wert bestimmen.
        //Bestimme zuerst kleinsten Wert, entferne alle Einträge dieses Wertes, hole den neuen kleinsten Wert.
        int minval = minRacenum(copyOfFinishedDrivers);
        for (int i = 0; i < copyOfFinishedDrivers.size(); ++i)
            if (copyOfFinishedDrivers.get(i).getAnzahlrennen() == minval)
                copyOfFinishedDrivers.remove(i--);

        //Wenn alle Werte gleich waren, ist die Liste leer. Als Fallback wird -1 zurückgegeben und
        //der vorherige minval wird neu berechnet.
        minval = minRacenum(copyOfFinishedDrivers);
        if (minval == -1)
            minval = minRacenum(finishedDrivers);

        while (true){
            for (Driver d : finishedDrivers)
                if (d.getAnzahlrennen() < minval &&
                        Math.abs(d.getPunkte() - avg) < ε
                        )
                    return new Race(pairableDrivers[0], pairableDrivers[1], d);
            ε += 0.5;
            if (ε > 9){
                Collections.shuffle(finishedDrivers);
                if (minval > 1000){
                    return new Race(pairableDrivers[0], pairableDrivers[1], finishedDrivers.get(0));
                }
                for (Driver d : finishedDrivers){
                    if (d.getAnzahlrennen() < minval)
                        return new Race(pairableDrivers[0], pairableDrivers[1], d);
                }
                ++minval;
            }
        }
    }

    private boolean isValidRace(Race pick){
        //Prüfen ob 2 Spieler doppelt gegeneinander fahren
        //Die Bedingung, dass niemand mehr als 3 mal fahren darf ist constraint von
        //evaluate und hasNext und kann daher hier ignoriert werden.
        for (Race rennen : gefahrene_rennen)
            if ((isIn(pick.get(0), rennen) && isIn(pick.get(1), rennen)) ||
                    (isIn(pick.get(0), rennen) && isIn(pick.get(2), rennen)) ||
                    (isIn(pick.get(1), rennen) && isIn(pick.get(2), rennen)))
                return false;
        return true;
    }

    private Driver[] getPairOfPairableDrivers(){
        for (Driver a : leftoutDrivers)
            for (Driver b : leftoutDrivers){
                //Man kann nicht gegen sich selbst fahren
                if (a == b)
                    continue;
                Driver[] pair = new Driver[]{a, b};
                //a, b dürfen noch nie gegeneinander angetreten sein.
                boolean pairable = true;
                for (Race r : gefahrene_rennen)
                    if (isIn(a, r) && isIn(b, r)){
                        pairable = false;
                        break;
                    }
                if (pairable)
                    return pair;
            }
        //wenn es kein Paar gibt, gib null zurück
        return null;
    }

    public void printminmax(){
        System.out.println(minRacenum(finishedDrivers) + " " + maxRacenum(finishedDrivers));
    }
}
