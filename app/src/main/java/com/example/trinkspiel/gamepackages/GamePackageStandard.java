package com.example.trinkspiel.gamepackages;

import java.util.ArrayList;

public class GamePackageStandard extends AbstractGamePackage{
    public static final String PACKAGE_NAME = "Standard Paket";

    private ArrayList<String> cards = fillList();

    public ArrayList<String> getCards(){
        return cards;
    }

    @Override
    public AbstractGamePackage clone() {
        return new GamePackageStandard();
    }

    private ArrayList<String> fillList() {
        ArrayList<String> filledList = new ArrayList<>();

        filledList.add("$Sp1 Trinke 3");
        filledList.add("$Sp1 Trinke 8");
        filledList.add("$Sp1 Trinke 111");
        filledList.add("$Sp1 Trinke bis zum Tod");

        return filledList;
    }
}
