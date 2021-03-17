package com.example.trinkspiel.gamepackages;

import java.util.ArrayList;

public abstract class AbstractGamePackage implements Cloneable {
    public abstract ArrayList<String> getCards();

    public abstract AbstractGamePackage clone();
}
