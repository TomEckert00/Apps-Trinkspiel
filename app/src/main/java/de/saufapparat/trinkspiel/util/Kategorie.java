package de.saufapparat.trinkspiel.util;

public class Kategorie {

    private String kategorieName;
    private String kategorieColorName;

    public Kategorie(String kategorieName, String kategorieColorName){
        this.kategorieName = kategorieName;
        this.kategorieColorName = kategorieColorName;
    }

    public String getKategorieName(){
        return kategorieName;
    }

    public String getKategorieColorName(){
        return kategorieColorName;
    }
}
