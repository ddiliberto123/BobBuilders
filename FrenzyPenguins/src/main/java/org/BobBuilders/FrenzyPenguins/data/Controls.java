package org.BobBuilders.FrenzyPenguins.data;

import com.almasb.fxgl.input.UserAction;
import javafx.scene.input.KeyCode;
import org.BobBuilders.FrenzyPenguins.CustomEntityFactory;

public class Controls {
    public static KeyCode ROLLCLOCKWISE = KeyCode.D;
    public static KeyCode ROLLCOUNTERCLOCKWISE = KeyCode.A;
    public static KeyCode JETPACK = KeyCode.SPACE;

    public static final String ABILITY = "ABILITY";
    public static final String PITCHUP = "PITCHUP";
    public static final String PITCHDOWN = "PITCHDOWN";

    UserAction useAbility = new UserAction(ABILITY) {
        @Override
        protected void onAction() {
            super.onAction();
        }

        @Override
        protected void onActionBegin() {
            super.onActionBegin();
        }

        @Override
        protected void onActionEnd() {
            super.onActionEnd();
        }
    };

    UserAction userPitchUP = new UserAction(PITCHUP) {

        @Override
        protected void onAction() {
//            if (store.isEquipJetpack() && penguin.getX() >= 1000) {
//                jetpackKeyPressed = true;  // Set the flag to true when space key is released
//                CustomEntityFactory.penguinJet.setVisible(false);
//                CustomEntityFactory.penguinJetActive.setVisible(true);
//            }
        }

        @Override
        protected void onActionBegin() {
            super.onActionBegin();
        }

        @Override
        protected void onActionEnd() {
            super.onActionEnd();
        }
    };
    UserAction userPitchDown = new UserAction(PITCHDOWN) {
        @Override
        protected void onAction() {
            super.onAction();
        }

        @Override
        protected void onActionBegin() {
            super.onActionBegin();
        }

        @Override
        protected void onActionEnd() {
            super.onActionEnd();
        }
    };

}
