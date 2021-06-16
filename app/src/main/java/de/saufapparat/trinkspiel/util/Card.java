package de.saufapparat.trinkspiel.util;

public class Card {

    private int schlucke;
    private String aufgabe;
    private Kategorie kategorie;

    public Card(String aufgabe, int schlucke, Kategorie kategorie){
        this.aufgabe = aufgabe;
        this.schlucke = schlucke;
        this.kategorie = kategorie;
    }

    public void setAufgabe(String aufgabe){
        this.aufgabe = aufgabe;
    }

    public int getSchlucke(){
        return schlucke;
    }

    public String getAufgabe(){
        return aufgabe;
    }

    public Kategorie getKategorie(){
        return kategorie;
    }
}
