package de.saufapparat.trinkspiel.model;

import lombok.Getter;
import lombok.Setter;

@Getter
public class Card {

    private int trinkeinheit;
    @Setter
    private String aufgabe;
    private Kategorie kategorie;

    public Card(String aufgabe, int trinkeinheit, Kategorie kategorie){
        this.aufgabe = aufgabe;
        this.trinkeinheit = trinkeinheit;
        this.kategorie = kategorie;
    }
}
