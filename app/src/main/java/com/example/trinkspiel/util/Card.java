package com.example.trinkspiel.util;

public class Card {

    private int kartenNummer;
    private int schlucke;
    private String aufgabe;

    public Card(int kartenNummer,String aufgabe,int schlucke){
        this.kartenNummer=kartenNummer;
        this.aufgabe=aufgabe;
        this.schlucke=schlucke;
    }

    public void setAufgabe(String aufgabe) {
        this.aufgabe = aufgabe;
    }

    public int getKartenNummer(){
        return kartenNummer;
    }

    public int getSchlucke(){
        return schlucke;
    }

    public String getAufgabe(){
        return aufgabe;
    }
}
