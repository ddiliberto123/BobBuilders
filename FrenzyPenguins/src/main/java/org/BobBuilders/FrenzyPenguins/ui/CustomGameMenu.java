package org.BobBuilders.FrenzyPenguins.ui;

import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.dsl.FXGL;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
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
        Rectangle back = new Rectangle(getAppWidth(),getAppHeight());
        back.setFill(Color.WHITESMOKE);
        StackPane stack = new StackPane();
        getContentRoot().setBackground(new Background(new BackgroundFill(Color.WHITESMOKE, CornerRadii.EMPTY,null)));
        Text title = FXGL.getUIFactoryService().newText("Store",Color.BLACK,70);
        title.setTranslateX(0);
        title.setTranslateY(-300);

        //Creates the buttons
        CustomGameMenu.customMenuButton btnResume = new CustomGameMenu.customMenuButton("Resume", this::fireResume);
        CustomGameMenu.customMenuButton btnOptions = new CustomGameMenu.customMenuButton("Main Menu", this::fireExitToMainMenu);
        CustomGameMenu.customMenuButton btnOptions1 = new CustomGameMenu.customMenuButton("Buy Jetpack", () -> {});
        CustomGameMenu.customMenuButton btnOptions2 = new CustomGameMenu.customMenuButton(" Buy Glider", () -> {});
        CustomGameMenu.customMenuButton btnOptions3 = new CustomGameMenu.customMenuButton("Buy Snowboard", () -> {});
        CustomGameMenu.customMenuButton btnMainMenu = new CustomGameMenu.customMenuButton("Quit", this::fireExitToMainMenu);

        var hboxChoices = new HBox(10,
                btnOptions1,
                btnOptions2,
                btnOptions3,
                new Text("")
                );
        hboxChoices.setPadding(new Insets(50));
        hboxChoices.setAlignment(Pos.BOTTOM_CENTER);

        var hboxDisplay = new HBox(10);

        //Creates a vbox to store the menu in
        var vbox = new VBox(10,
                btnResume,
                btnOptions,
                btnMainMenu,
                new Text("")
                );
        vbox.setPadding(new Insets(50));

        var container = new GridPane();
        container.setPadding(new Insets(10));
        container.add(vbox,0,0);
        container.add(hboxChoices,1,0);
        container.setGridLinesVisible(true);
        container.setAlignment(Pos.CENTER);

//        vbox.setTranslateX(150);
//        vbox.setTranslateY(350);
        stack.getChildren().addAll(back,container,title);
        getContentRoot().getChildren().addAll(stack);

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


//    private static class LineSeparator extends Parent {
//        private Rectangle line = new Rectangle(FXGL.getAppWidth(),FXGL.getAppHeight());
//        public LineSeparator(){
//            var gradient = new LinearGradient(0,0,0.5,0.5,true, CycleMethod.NO_CYCLE,
//                    new Stop(0,Color.BLACK),
//                    new Stop(0.5, Color.GRAY),
//                    new Stop(2.0,Color.TRANSPARENT));
//
//            line.setFill(gradient);
//            line.setFill(Color.BLACK);
//            getChildren().add(line);
//        }
//    }
}
