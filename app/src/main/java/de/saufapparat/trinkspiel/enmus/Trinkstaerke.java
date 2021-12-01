package de.saufapparat.trinkspiel.enmus;

public enum Trinkstaerke {


    entspannt(0.5),
    normal(1),
    stark(2),
    extrem(3),
    hardcore(5);

    private double multiplier;

    Trinkstaerke(double multiplier){
        this.multiplier = multiplier;
    }

    public double getMultiplier(){
        return this.multiplier;
    }
}
