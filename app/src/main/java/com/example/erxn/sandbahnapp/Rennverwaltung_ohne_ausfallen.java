package com.example.erxn.sandbahnapp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;


public class Rennverwaltung_ohne_ausfallen extends RennverwaltungBase{


    public Rennverwaltung_ohne_ausfallen(Driver[] drivers){
        gezogene_rennen = rennfolge(drivers);
        drivernum = drivers.length;
        renncnt = 0;
    }

    private static ArrayList<Race> rennfolge(Driver[] teilnehmer){
        ArrayList<Race> moegliche_rennen = Race.hoch3(teilnehmer);
        ArrayList<Race> gezogene_rennen = new ArrayList<>(teilnehmer.length);
        return rennfolge(teilnehmer, moegliche_rennen, gezogene_rennen);
    }

    private static ArrayList<Race> rennfolge(Driver[] teilnehmer,
                                             ArrayList<Race> moegliche_rennen,
                                             ArrayList<Race> gezogene_rennen){

        final int anzahl_fahrer = teilnehmer.length;

        int triescnt = 0;

        final int maxtries = Math.max(10_000, anzahl_fahrer * anzahl_fahrer * anzahl_fahrer);

        Random random = new Random();

        while (gezogene_rennen.size() < anzahl_fahrer){
            //Counter der Schleifendurchläufe
            //Manchmal scheitert die Auswahl und das/die letzten Rennen können nicht mehr gültig gezogen werden. Das fangen
            //wir ab (mit triescnt) und resetten die Werte von gezogene_rennen, moegliche_rennen und triescnt.
            if (triescnt++ == maxtries){
                System.out.println("Neuversuch");
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
                if ((isIn(pick.get(0), rennen) && isIn(pick.get(2), rennen)) ||
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
            char[] rennen_je_fahrer = new char[anzahl_fahrer];
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

        return gezogene_rennen;
    }

    @Override
    public boolean hasNext(){
        return renncnt < drivernum;
    }

    @Override
    public Race next(){
        return gezogene_rennen.get(renncnt++);
    }
}
