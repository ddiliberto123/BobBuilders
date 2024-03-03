package org.BobBuilders.FrenzyPenguins.ui;

import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.dsl.FXGL;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;

public class CustomMainMenu extends FXGLMenu {
    public CustomMainMenu() {
        super(MenuType.MAIN_MENU);

        FXGL.getAssetLoader().loadTexture("background/background.png");
        VBox vbox = new VBox(10);
        vbox.setTranslateX(100);
        vbox.setTranslateY(450);
        vbox.getChildren().addAll(
                createButton("New Game"),
                createButton("Options"),
                createButton("Exit"));
        getContentRoot().getChildren().addAll(vbox);
    }

    private static Button createButton(String str){
        return FXGL.getUIFactoryService().newButton(str);
    }
}
