package org.BobBuilders.FrenzyPenguins;


import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Data singleton used to store user inventory
 */
public class Inventory {
    /**
     *
     */
    private static Inventory instance = null;
    @Setter
    @Getter
    private int rampLevel;
    @Setter
    @Getter
    private boolean hasJetpack;
    @Setter
    @Getter
    private boolean hasSlide;
    private SimpleIntegerProperty points;

    private Inventory() {
        rampLevel = 1;
        hasJetpack = false;
        hasSlide = false;
        points = new SimpleIntegerProperty();
        points.setValue(0);
        Math.min(1, 2);
    }

    /**
     * Returns the instance of the {@code Inventory} if the instance is not null, else creates a new instance of {@code Inventory}
     * @return the instance of {@code Inventory}
     */
    public static Inventory getInstance() {
        return instance = (instance == null) ? new Inventory() : instance;
    }

    public void addPoints(int addedPoints) {
        this.points.setValue(this.points.getValue() + addedPoints);
    }

    public SimpleIntegerProperty getPointsProperty(){
        return this.points;
    }
    public int getPoints(){
        return this.points.getValue();
    }
}
