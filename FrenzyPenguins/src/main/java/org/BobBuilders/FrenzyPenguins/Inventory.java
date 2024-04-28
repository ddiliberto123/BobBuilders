package org.BobBuilders.FrenzyPenguins;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.BobBuilders.FrenzyPenguins.translators.InventoryDeserializer;
import org.BobBuilders.FrenzyPenguins.translators.InventorySerializer;

/**
 * Data singleton used to store user inventory
 */
@EqualsAndHashCode
@JsonSerialize(using = InventorySerializer.class)
@JsonDeserialize(using = InventoryDeserializer.class)
public class Inventory {
    private static Inventory instance = null;
    @Setter
    @Getter
    private BooleanProperty equipJetpack;
    private BooleanProperty equipGlider;
    private BooleanProperty equipSlide;
//    @Setter
//    @Getter
//    private int jetPackLevel;
//    @Setter
//    @Getter
//    private int gliderLevel;
//    @Setter
//    @Getter
//    private int slideLevel;
//    @Setter
//    @Getter
//    private int rampLevel;
    @Getter
    private IntegerProperty rampLevelProperty;
    @Getter
    private IntegerProperty jetPackLevelProperty;
    @Getter
    private IntegerProperty gliderLevelProperty;
    @Getter
    private IntegerProperty slideLevelProperty;
    @Getter
    private SimpleIntegerProperty pointsProperty;
    @Getter
    @Setter
    private int totalDistanceFlown = 0;
    @Getter
    @Setter
    private int maxDistanceFlown = 0;
    @Getter
    @Setter
    private int networth = 0;


    private Inventory() {
        pointsProperty = new SimpleIntegerProperty();
        pointsProperty.setValue(0);
        equipJetpack = new SimpleBooleanProperty(false);
        equipGlider = new SimpleBooleanProperty(false);
        equipSlide = new SimpleBooleanProperty(false);
//        jetPackLevel = 0;
//        gliderLevel = 0;
//        slideLevel = 0;
//        rampLevel = 0;
        jetPackLevelProperty = new SimpleIntegerProperty(0);
        gliderLevelProperty = new SimpleIntegerProperty(0);
        slideLevelProperty = new SimpleIntegerProperty(0);
        rampLevelProperty = new SimpleIntegerProperty(0);
    }

    public void clone(Inventory otherInventory) {
        this.pointsProperty.set(otherInventory.getPointsPropertyValue());
        this.equipJetpack.set(otherInventory.equipJetpack.getValue());
        this.equipGlider.set(otherInventory.equipGlider.getValue());
        this.equipSlide.set(otherInventory.equipSlide.getValue());
        jetPackLevelProperty.set(otherInventory.getJetPackLevelProperty().getValue());
        gliderLevelProperty.set(otherInventory.getGliderLevelProperty().getValue());
        slideLevelProperty.set(otherInventory.getSlideLevelProperty().getValue());
        rampLevelProperty.set(otherInventory.getRampLevelProperty().getValue());
//        setJetPackLevel(otherInventory.jetPackLevel);
//        setGliderLevel(otherInventory.gliderLevel);
//        setSlideLevel(otherInventory.slideLevel);
//        setRampLevel(otherInventory.rampLevel);

//        this.jetPackLevelProperty = otherInventory.jetPackLevelProperty;
//        this.gliderLevelProperty = otherInventory.gliderLevelProperty;
//        this.slideLevelProperty = otherInventory.slideLevelProperty;
//        this.rampLevelProperty = otherInventory.rampLevelProperty;
    }

    /**
     * Creates an or gets the single instance of inventory
     *
     * @return the instance
     */
    public static Inventory getInstance() {
        return instance = (instance == null) ? new Inventory() : instance;
    }

    public static Inventory createInstance() {
        return new Inventory();
    }

    //Getters and setters to know whether or not equipment is owned
    public boolean isEquipJetpack() {
        return equipJetpack.get();
    }

    public void setEquipJetpack(boolean answer) {
        equipJetpack.set(answer);
    }

    public boolean isEquipGlider() {
        return equipGlider.get();
    }

    public void setEquipGlider(boolean answer) {
        equipGlider.set(answer);
    }

    public boolean isEquipSlide() {
        return equipSlide.get();
    }

    public BooleanProperty hasEquippedJetpack() {
        return equipJetpack;
    }

    public BooleanProperty hasEquippedGlider() {
        return equipGlider;
    }

    public BooleanProperty hasEquippedSlide() {
        return equipSlide;
    }

    public void setEquipSlide(boolean answer) {
        equipSlide.set(answer);
    }

    public void setJetPackLevelProperty(int jetPackLevelProperty) {
        this.jetPackLevelProperty.set(jetPackLevelProperty);
    }

    public void setGliderLevelProperty(int gliderLevelProperty) {
        this.gliderLevelProperty.set(gliderLevelProperty);
    }
    public void setSlideLevelProperty(int slideLevelProperty) {
        this.slideLevelProperty.set(slideLevelProperty);
    }
    public void setRampLevelProperty(int rampLevelProperty) {
        this.rampLevelProperty.set(rampLevelProperty);
    }

    public int getJetpackLevel() {
        return getJetPackLevelProperty().getValue();
    }
    public int getGliderLevel() {
        return getGliderLevelProperty().getValue();
    }
    public int getSlideLevel() {
        return getSlideLevelProperty().getValue();
    }

    public int getRampLevel() {
        return getRampLevelProperty().getValue();
    }

    public void setJetpackLevel(int jetpackLevel) {
        getJetPackLevelProperty().set(jetpackLevel);
    }
    public void setGliderLevel(int gliderLevel) {
        getGliderLevelProperty().set(gliderLevel);
    }

    public void setSlideLevel(int slideLevel) {
        getSlideLevelProperty().set(slideLevel);
    }

    public void setRampLevel(int rampLevel) {
        getRampLevelProperty().set(rampLevel);
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

    public void setPointsPropertyValue(int points) {
        this.pointsProperty.set(points);
    }

    public String toString() {
        return String.format("Ramp Level: %d\n Jetpack Level: %d\n Glider Level: %d\n Slide Level: %d\n Points: %d",
                this.rampLevelProperty.getValue(),
                this.jetPackLevelProperty.getValue(),
                this.gliderLevelProperty.getValue(),
                this.slideLevelProperty.getValue(),
                this.pointsProperty.getValue());
    }
}
