package org.BobBuilders.FrenzyPenguins;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class Store {
    public static Store instance;
    private BooleanProperty equipJetpack;
    private BooleanProperty equipGlider;
    private BooleanProperty equipSlide;
    public Store(){
        equipJetpack = new SimpleBooleanProperty(false);
        equipGlider = new SimpleBooleanProperty(false);
        equipSlide = new SimpleBooleanProperty(false);
    }
    public static Store getInstance(){
        if(instance == null){
            instance = new Store();
        }
        return instance;
    }
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
    public BooleanProperty hasEquippedJetpack(){
        return equipJetpack;
    }
    public BooleanProperty hasEquippedGlider(){
        return equipGlider;
    }
    public BooleanProperty hasEquippedSlide(){
        return equipSlide;
    }

    public void setEquipSlide(boolean answer) {
        equipSlide.set(answer);
    }

    private void withdrawMoney() {

    }

    private void addToInventory() {

    }

}
