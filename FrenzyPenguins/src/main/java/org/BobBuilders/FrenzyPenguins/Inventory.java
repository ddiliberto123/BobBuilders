package org.BobBuilders.FrenzyPenguins;


import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
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
    private BooleanProperty hasJetpack;
    private BooleanProperty hasSlide;
    private BooleanProperty hasGlider;

    @Getter
    private IntegerProperty pointsProperty;

    public Inventory() {
        rampLevel = 1;
        pointsProperty = new SimpleIntegerProperty();
        pointsProperty.setValue(0);
        hasJetpack = new SimpleBooleanProperty(false);
        hasSlide = new SimpleBooleanProperty(false);
        hasGlider = new SimpleBooleanProperty(false);
    }

    /**
     * Creates an or gets the single instance of inventory
     *
     * @return the instance
     */
    public static Inventory getInstance() {
        return instance = (instance == null) ? new Inventory() : instance;
    }

    public void setHasJetpack(boolean answer) {
        hasJetpack.set(answer);
    }

    public void setHasSlide(boolean answer) {
        hasSlide.set(answer);
    }

    public void setHasGlider(boolean answer) {
        hasGlider.set(answer);
    }

    public BooleanProperty hasJetpackProperty() {
        if (hasJetpack == null) {
            hasJetpack = new SimpleBooleanProperty();
        }
        return hasJetpack;
    }

    public BooleanProperty hasSlideProperty() {
        if (hasSlide == null) {
            hasSlide = new SimpleBooleanProperty();
        }
        return hasSlide;
    }

    public BooleanProperty hasGliderProperty() {
        if (hasGlider == null) {
            hasGlider = new SimpleBooleanProperty();
        }
        return hasGlider;
    }

    public boolean isHasJetpack() {
        return hasJetpack.get();
    }

    public boolean isHasSlide() {
        return hasSlide.get();
    }

    public boolean isHasGlider() {
        return hasGlider.get();
    }

    /**
     * Adds points to the running total
     *
     * @param addedPoints the points to be added to the total.
     */
    public void addPoints(int addedPoints) {
        this.pointsProperty.setValue(this.pointsProperty.getValue() + addedPoints);
    }

    /**
     * Returns the running total
     *
     * @return the running total
     */
    public int getPointsPropertyValue() {
        return this.pointsProperty.getValue();
    }

}
