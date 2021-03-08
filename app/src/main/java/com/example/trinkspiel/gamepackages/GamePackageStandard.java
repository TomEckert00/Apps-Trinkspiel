package com.example.trinkspiel.gamepackages;

import java.util.ArrayList;

public class GamePackageStandard {
    public static final String PACKAGE_NAME = "Standard Packet";

    private ArrayList<String> cards = fillList();

    public ArrayList<String> getCards(){
        return cards;
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
