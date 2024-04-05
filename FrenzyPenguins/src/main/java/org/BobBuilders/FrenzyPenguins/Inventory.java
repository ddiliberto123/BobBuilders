package org.BobBuilders.FrenzyPenguins;


import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
/**
 * Data singleton used to store user inventory
 */
public class Inventory {

    private static Inventory instance = null;
    private int rampLevel;
    private boolean hasJetpack;
    private boolean hasSlide;
    private boolean hasGlider;
    private IntegerProperty points;

    public Inventory() {
        rampLevel = 1;
        points = new SimpleIntegerProperty();
        points.setValue(0);
    }

    public void setHasJetpack(boolean hasJetpack) {
        this.hasJetpack = hasJetpack;
    }

    public void setHasSlide(boolean hasSlide) {
        this.hasSlide = hasSlide;
    }
    public void setHasGlider(boolean hasGlider){
        this.hasGlider = hasGlider;
    }

    public boolean isHasJetpack() {
        return hasJetpack;
    }

    public boolean isHasSlide() {
        return hasSlide;
    }
    public boolean isHasGlider() {
        return hasGlider;
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
