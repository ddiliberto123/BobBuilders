package org.BobBuilders.FrenzyPenguins;


import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Data singleton used to store user inventory
 */
public class Inventory {

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
    private IntegerProperty points;

    private Inventory() {
        rampLevel = 1;
        hasJetpack = false;
        hasSlide = false;
        points = new SimpleIntegerProperty();
        points.setValue(0);
    }

    /**
     * Creates an or gets the single instance of inventory
     * @return the instance
     */
    public static Inventory getInstance() {
        if (instance == null) {
            instance = new Inventory();
        }
        return instance;
    }

    /**
     * Adds points to the running total
     * @param addedPoints the points to be added to the total.
     */
    public void addPoints(int addedPoints) {
        this.points.setValue(this.points.getValue() + addedPoints);
    }

    /**
     * Gets the running points' total property
     * @return the {@code IntegerProperty} of points
     */
    public IntegerProperty getPointsProperty() {
        return this.points;
    }

    /**
     * Returns the running total
     * @return the {@code int} running total
     */
    public int getPoints() {
        return this.points.getValue();
    }

}
