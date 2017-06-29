package com.example.erxn.sandbahnapp;

import android.content.Context;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;


public class Rennverwaltung extends RennverwaltungMitAusfallenBase{

    private ArrayList<Race> verbotene_rennen;
    private ArrayList<Race> gefahrene_rennen;
    private Context context;

    public Rennverwaltung(Driver[] drivers, Context context){
        drivernum = 0;
        renncnt = 0;
        this.drivers = drivers;
        this.context = context;
        moegliche_rennen = Race.hoch3(drivers);
        verbotene_rennen = new ArrayList<>();
        gefahrene_rennen = new ArrayList<>();
        //Zieht initiale Rennfolge
        rennfolge(drivers);
    /*
    gezogene_rennen = new ArrayList<>(drivers.length);
    gezogene_rennen.add(new Race(drivers[1], drivers[0], drivers[5]));
    gezogene_rennen.add(new Race(drivers[0], drivers[4], drivers[6]));
    gezogene_rennen.add(new Race(drivers[2], drivers[6], drivers[1]));
    gezogene_rennen.add(new Race(drivers[0], drivers[2], drivers[3]));
    gezogene_rennen.add(new Race(drivers[5], drivers[2], drivers[4]));
    gezogene_rennen.add(new Race(drivers[4], drivers[3], drivers[1]));
    gezogene_rennen.add(new Race(drivers[3], drivers[6], drivers[5]));
    for (Race r1 : gezogene_rennen)
      for (Race r2 : moegliche_rennen)
        if (r1.equals(r2)){
          moegliche_rennen.remove(r2);
          break;
        }
    */

    }

    private static boolean einerAusgeschieden(Race r){
        for (Driver driver : r)
            if (driver.isAusgefallen())
                return true;
        return false;
    }

    public Driver[] getDrivers() {
        return this.drivers;
    }

    @Override
    public boolean hasNext(){
        if (gezogene_rennen.isEmpty())
            return false;

        while (einerAusgeschieden(gezogene_rennen.get(0))){
            Race h = gezogene_rennen.get(0);
            gezogene_rennen.remove(0);
            verbotene_rennen.add(h);
            if (gezogene_rennen.isEmpty())
                return false;
        }

        return true;
    }

    @Override
    public Race next(){
        return gezogene_rennen.get(0);
    }

    private void forbidById(int id){
        int currentsize = gezogene_rennen.size();
        for (int i = 0; i < currentsize; ++i){
            if (gezogene_rennen.get(i).idInRace(id)){
                Race h = gezogene_rennen.get(i);
                gezogene_rennen.remove(i);
                verbotene_rennen.add(h);
                --i;
                --currentsize;
            }
        }
    }

    @Override
    public void reactivate(int startnummer){
        for (Driver d : drivers){
            if (d.getStartnummer() == startnummer)
                d.setAusfall(false);
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
        gezogene_rennen.remove(thisrace);
        gefahrene_rennen.add(thisrace);

        //Ausgefallene Fahrer rauswerfen, ansonsten Punkte zuweisen
        for (Driver d : thisrace)
            if (!d.isAusgefallen()){
                d.incrementAnzahlrennen();
                if (d == thisrace.get(winner)) {
                    d.raisePunkte(3);
                }

                if (d == thisrace.get(second)) {
                    d.raisePunkte(2);
                }
                if (d == thisrace.get(third)) {
                    d.raisePunkte(1);
                }
            }
        Toast.makeText(context, "Punkte wurden vergeben", Toast.LENGTH_SHORT).show();
    }

    private void rennfolge(Driver[] teilnehmer){
        gezogene_rennen = new ArrayList<>(teilnehmer.length);
        rennfolge();
    }

    private void rennfolge(){
        final int anzahl_fahrer = drivers.length;

        int triescnt = 0;

        final int maxtries = Math.max(10_000, anzahl_fahrer * anzahl_fahrer * anzahl_fahrer);

        Random random = new Random();

        while (gezogene_rennen.size() < anzahl_fahrer){
            //Counter der Schleifendurchläufe
            //Manchmal scheitert die Auswahl und das/die letzten Rennen können nicht mehr gültig gezogen werden. Das fangen
            //wir ab (mit triescnt) und resetten die Werte von gezogene_rennen, moegliche_rennen und triescnt.
            if (triescnt++ == maxtries){
                moegliche_rennen.addAll(gezogene_rennen);
                gezogene_rennen.clear();
                triescnt = 1;
            }

            //Neues Spielertripel ziehen, aus moegliche_rennen entfernen
            int zufall = random.nextInt(moegliche_rennen.size() - 1);
            Race pick = moegliche_rennen.get(zufall);
            moegliche_rennen.remove(zufall);

            //Prüfen ob 2 Spieler doppelt gegeneinander fahren
            boolean pick_in_gezogene_rennen = false;
            for (Race rennen : gezogene_rennen){
                if ((isIn(pick.get(0), rennen) && isIn(pick.get(1), rennen)) ||
                        (isIn(pick.get(0), rennen) && isIn(pick.get(2), rennen)) ||
                        (isIn(pick.get(1), rennen) && isIn(pick.get(2), rennen))){
                    pick_in_gezogene_rennen = true;
                    break;
                }
            }

            //Falls 2 Spieler doppelt gegeneinander fahren, pick wieder in Topf werfen
            //und erneut auslosen
            if (pick_in_gezogene_rennen){
                moegliche_rennen.add(pick);
                continue;
            }

            //pick der Rennliste hinzufügen, prüfen ob jeder Spieler maximal 3 rennen fährt
            gezogene_rennen.add(pick);
            int maxId = 0;
            for (Race rennen : gezogene_rennen)
                for (Driver spieler : rennen)
                    maxId = Math.max(maxId, spieler.getDriverID());

            char[] rennen_je_fahrer = new char[maxId + 1];
            for (Race rennen : gezogene_rennen)
                for (Driver spieler : rennen)
                    ++rennen_je_fahrer[spieler.getDriverID()];

            //Falls ein Spieler mehr als 3 Rennen fährt, Tripel aus Liste der Rennen nehmen, wieder in Topf werfen und
            // erneut auslosen.
            if (max(rennen_je_fahrer) > 3){
                moegliche_rennen.add(pick);
                gezogene_rennen.remove(pick);
            }
        }

        /////////////////
        // Ende dieser sehr langen while Schleife. Wir haben einen Rennverlauf
        // erlost.
        // Juchu. Ab etwa 11 Spielern lässt sich in vertretbarer Zeit (und meinen
        // Tests
        // nach mit an Sicherheit grenzender Wahrscheinlichkeit prinzipiell) eine
        // Reihenfolge finden, in der kein Fahrer 2 mal hintereinander fahren muss.
        // Eine solche Reihenfolge finden wir jetzt.
        // Danach nur noch die Rennpaarungen hübsch ausgeben. Der Code dazu ist
        // häßlich.
        // Sorry not sorry.
        /////////////////

        if (anzahl_fahrer > 10)
            while (einerFaehrtZweiMalHintereinander(gezogene_rennen)) Collections.shuffle(gezogene_rennen);
    }

    @Override
    public String toString(){
        StringBuilder s = new StringBuilder();
        for (Driver d : drivers){
            s.append(d.toString());
            s.append(": ");
            s.append(d.getPunkte());
            s.append(" Punkte\n");
        }
        return s.toString().trim();
    }

    public ArrayList<Race> getRennfolge(){
        ArrayList<Race> ret = new ArrayList<>();
        ret.addAll(gefahrene_rennen);
        ret.addAll(gezogene_rennen);
        return ret;
    }

    public Rennverwaltung_Nachholrennen_v1 getNachholrennen(){
        if (!gezogene_rennen.isEmpty())
            return null;
        ArrayList<Driver> ausgelasseneDrivers = new ArrayList<>();
        ArrayList<Driver> otherDrivers = new ArrayList<>();

        for (Driver d : drivers)
            if (d.getAnzahlrennen() < 3)
                ausgelasseneDrivers.add(d);
            else
                otherDrivers.add(d);
        return new Rennverwaltung_Nachholrennen_v1(drivers, ausgelasseneDrivers, otherDrivers, gefahrene_rennen);
    }
}
