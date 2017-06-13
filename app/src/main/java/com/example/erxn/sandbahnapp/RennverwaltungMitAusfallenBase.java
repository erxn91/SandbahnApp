package com.example.erxn.sandbahnapp;

public abstract class RennverwaltungMitAusfallenBase extends RennverwaltungBase{

    public abstract void reactivate(int startnummer);

    public abstract void evaluate(int winner,
                                  int second,
                                  int third,
                                  boolean ausgeschieden0,
                                  boolean ausgeschieden1,
                                  boolean ausgeschieden2);
}
