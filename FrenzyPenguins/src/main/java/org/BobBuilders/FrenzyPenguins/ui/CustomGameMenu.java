package org.BobBuilders.FrenzyPenguins.ui;

import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.dsl.FXGL;
import javafx.beans.binding.Bindings;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class CustomGameMenu extends FXGLMenu {
    private static final Color SELECTED_COLOR = Color.BLACK;
    private static final Color NOT_SELECTED_COLOR = Color.GRAY;


    public CustomGameMenu() {
        super(MenuType.GAME_MENU);

        //Creates the buttons
        CustomGameMenu.customMenuButton btnResume = new CustomGameMenu.customMenuButton("Resume", this::fireResume);
        CustomGameMenu.customMenuButton btnOptions = new CustomGameMenu.customMenuButton("Options", () -> {});
        CustomGameMenu.customMenuButton btnMainMenu = new CustomGameMenu.customMenuButton("Main Menu", this::fireExitToMainMenu);

        Region region = new Region();
        region.setStyle("-fx-background-radius: 20000; -fx-background-color: rgb(0,0,0);");
        region.setEffect(new DropShadow(10,Color.GREY));
        getContentRoot().setStyle("-fx-background-color: null;");
        
        //Creates a vbox to store the menu in
        var vbox = new VBox(10,
                btnResume,
                btnOptions,
                btnMainMenu,
                new Text(""),
                new CustomGameMenu.LineSeparator(),
                FXGL.getUIFactoryService().newText("Not Logged in",Color.GRAY,15));
        vbox.setTranslateX(150);
        vbox.setTranslateY(350);
        getContentRoot().getChildren().addAll(vbox);
    }

    private static class customMenuButton extends StackPane {

        private String name;
        private Runnable action;

        private Text text;
        private Rectangle selector;
        public customMenuButton(String name, Runnable action){
            this.name = name;
            this.action = action;

            //Calls the UI factory apart of FXGL to create a text box
            text = FXGL.getUIFactoryService().newText(name,Color.BLACK,20.0);
            //This is the rectangle next to the buttons that show its been selected (color of button also changes)
            selector = new Rectangle(8,20, Color.BLACK);
            selector.setTranslateX(-20);
            //Sets it visible if its focused
            selector.visibleProperty().bind(focusedProperty());

            text.setStrokeWidth(.5);
            //Changes the colour if its focused
            text.fillProperty().bind(Bindings.when(focusedProperty())
                    .then(SELECTED_COLOR).otherwise(NOT_SELECTED_COLOR));
            text.strokeProperty().bind(
                    Bindings.when(focusedProperty()).then(SELECTED_COLOR).otherwise(NOT_SELECTED_COLOR)
            );
            hoverProperty().addListener((observableValue, aBoolean, isHovered) -> {
                if (isHovered){
                    setFocused(true);
                } else {
                    setFocused(false);
                }
            });
            //Checks if the button is focused before allowed it to be pressed
            setOnMouseClicked(e -> {
                if(isFocused()){
                    action.run();
                }
            });

            setOnKeyPressed(e -> {
                //Checks if the button is focused before allowing enter to work (incase mouse is broken)
                if(isFocused() && e.getCode() == KeyCode.ENTER){
                    action.run();
                }
            });

            setAlignment(Pos.CENTER_LEFT);
            setFocusTraversable(true);
            getChildren().addAll(selector, text);

        }
    }

    //Used to create the line seperate (also fixes the issue of the text below it, defining its length)
    private static class LineSeparator extends Parent {
        private Rectangle line = new Rectangle(600,2);
        public LineSeparator(){
            var gradient = new LinearGradient(0,0,0.5,0.5,true, CycleMethod.NO_CYCLE,
                    new Stop(0,Color.BLACK),
                    new Stop(0.5, Color.GRAY),
                    new Stop(2.0,Color.TRANSPARENT));

            line.setFill(gradient);
            getChildren().add(line);
        }
    }
}
