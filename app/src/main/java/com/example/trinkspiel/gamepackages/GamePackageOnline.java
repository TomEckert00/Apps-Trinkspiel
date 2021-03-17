package com.example.trinkspiel.gamepackages;

import java.util.ArrayList;

public class GamePackageOnline extends AbstractGamePackage{
    public static final String PACKAGE_NAME = "Online Paket";

    private ArrayList<String> cards = fillList();

    public ArrayList<String> getCards(){
        return cards;
    }

    @Override
    public AbstractGamePackage clone() {
        return new GamePackageOnline();
    }

    private ArrayList<String> fillList() {
        ArrayList<String> filledList = new ArrayList<>();

        filledList.add("$Sp1 Trinke zoom");
        filledList.add("$Sp1 Trinke skype");
        filledList.add("$Sp1 Trinke teams");

        return filledList;
    }
}
