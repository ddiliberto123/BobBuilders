package org.BobBuilders.FrenzyPenguins;


import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Inventory {

    private static Inventory instance = null;
    private int rampLevel;
    private boolean hasJetpack;
    private boolean hasSlide;
    private IntegerProperty points;

    private Inventory() {
        rampLevel = 1;
        hasJetpack = false;
        hasSlide = false;
        points = new SimpleIntegerProperty();
        points.setValue(0);
    }

    public static Inventory getInstance() {
        if (instance == null){
            instance = new Inventory();
        }
        return instance;
    }

    public void addPoints(int addedPoints) {
        this.points.setValue(this.points.getValue() + addedPoints);
    }

    public IntegerProperty getPointsProperty(){
        return this.points;
    }
    public int getPoints(){
        return this.points.getValue();
    }
}
