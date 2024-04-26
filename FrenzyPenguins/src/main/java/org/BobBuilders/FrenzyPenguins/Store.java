package org.BobBuilders.FrenzyPenguins;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Store {
    public static Store instance;
    private BooleanProperty equipJetpack;
    private BooleanProperty equipGlider;
    private BooleanProperty equipSlide;
    private int jetPackLevel;
    private  int gliderLevel;
    private int slideLevel;
    private int rampLevel;
    private IntegerProperty rampLevelProperty;
    private IntegerProperty jetPackLevelProperty;
    private IntegerProperty gliderLevelProperty;
    private IntegerProperty slideLevelProperty;


    public Store() {
        equipJetpack = new SimpleBooleanProperty(false);
        equipGlider = new SimpleBooleanProperty(false);
        equipSlide = new SimpleBooleanProperty(false);
        jetPackLevel = 1;
        gliderLevel = 1;
        slideLevel = 1;
        rampLevel = 1;
        jetPackLevelProperty = new SimpleIntegerProperty(jetPackLevel);
        gliderLevelProperty = new SimpleIntegerProperty(gliderLevel);
        slideLevelProperty = new SimpleIntegerProperty(slideLevel);
        rampLevelProperty = new SimpleIntegerProperty(rampLevel);
    }

    public static Store getInstance() {
        if (instance == null) {
            instance = new Store();
        }
        return instance;
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

    public int getJetPackLevel() {
        return jetPackLevel;
    }
    public int getGliderLevel() {
        return gliderLevel;
    }
    public int getSlideLevel() {
        return slideLevel;
    }
    public void setJetPackLevel(int jetPackLevel) {
        this.jetPackLevel = jetPackLevel;
    }
    public void setGliderLevel(int gliderLevel) {
        this.gliderLevel = gliderLevel;
    }
    public void setSlideLevel(int slideLevel) {
        this.slideLevel = slideLevel;
    }
    public int getRampLevel() {
        return rampLevel;
    }

    public void setRampLevel(int rampLevel) {
        this.rampLevel = rampLevel;
    }

    public IntegerProperty getJetPackLevelProperty() {
        return jetPackLevelProperty;
    }

    public void setJetPackLevelProperty(int jetPackLevelProperty) {
        this.jetPackLevelProperty.set(jetPackLevelProperty);
    }

    public IntegerProperty getGliderLevelProperty() {
        return gliderLevelProperty;
    }


    public void setGliderLevelProperty(int gliderLevelProperty) {
        this.gliderLevelProperty.set(gliderLevelProperty);
    }

    public IntegerProperty getSlideLevelProperty() {
        return slideLevelProperty;
    }

    public void setSlideLevelProperty(int slideLevelProperty) {
        this.slideLevelProperty.set(slideLevelProperty);
    }
    public void setRampLevelProperty(int rampLevelProperty) {
        this.rampLevelProperty.set(rampLevelProperty);
    }

    private void withdrawMoney() {

    }

    //TODO: Add store items to Inventory
    private void addToInventory() {

    }

}
