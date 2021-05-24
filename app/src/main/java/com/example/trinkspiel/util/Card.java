package com.example.trinkspiel.util;

import android.graphics.Color;

import java.util.HashMap;
import java.util.Map;

public class Card {

    private int kartenNummer;
    private int schlucke;
    private String aufgabe;
    private Kategorie kategorie;

    public Card(int kartenNummer,String aufgabe,int schlucke, Kategorie kategorie){
        this.kartenNummer=kartenNummer;
        this.aufgabe=aufgabe;
        this.schlucke=schlucke;
        this.kategorie=kategorie;
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

    public Kategorie getKategorie(){ return kategorie; }
}
