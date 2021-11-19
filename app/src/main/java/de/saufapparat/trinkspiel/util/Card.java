package de.saufapparat.trinkspiel.util;

import lombok.Getter;
import lombok.Setter;

@Getter
public class Card {

    private int schlucke;
    @Setter
    private String aufgabe;
    private Kategorie kategorie;

    public Card(String aufgabe, int schlucke, Kategorie kategorie){
        this.aufgabe = aufgabe;
        this.schlucke = schlucke;
        this.kategorie = kategorie;
    }
}
