package org.BobBuilders.FrenzyPenguins.ui;

import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.dsl.FXGL;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import org.BobBuilders.FrenzyPenguins.Inventory;
import org.BobBuilders.FrenzyPenguins.User;
import org.BobBuilders.FrenzyPenguins.util.Database;


public class CustomMainMenu extends FXGLMenu {
    private static final Color SELECTED_COLOR = Color.BLACK;
    private static final Color NOT_SELECTED_COLOR = Color.GRAY;
    private VBox vboxOptions;
    private VBox vboxMainMenu;
    private SimpleStringProperty usernameProperty = new SimpleStringProperty();
    private VBox vboxLogin;

    //    private ObjectProperty<customMenuButton> selectedButton;
    public CustomMainMenu() {
        super(MenuType.MAIN_MENU);


        if (User.getInstance().getUserId() == 0) {
            usernameProperty.set("Not Logged in");
        } else {
            usernameProperty.set("Logged in as " + User.getInstance().getUsername());
        }

        Text menuUsernameText = FXGL.getUIFactoryService().newText("Not Logged in", Color.GRAY, 15);
        Text loginUsernameText = FXGL.getUIFactoryService().newText("Not Logged in", Color.GRAY, 15);
        Text optionsUsernameText = FXGL.getUIFactoryService().newText("Not Logged in", Color.GRAY, 15);



//        availablePoints.textProperty().bind(Bindings.convert(inventory.getPointsProperty()));

        //Creates the buttons
        customMenuButton btnPlayGame = new customMenuButton("Play Game", this::fireNewGame);
        customMenuButton btnLogin = new customMenuButton("Login", () -> {
            vboxMainMenu.setVisible(false);
            vboxLogin.setVisible(true);
        });

        customMenuButton btnOptions = new customMenuButton("Options", () -> {
            vboxMainMenu.setVisible(false);
            vboxOptions.setVisible(true);
        });
        customMenuButton btnQuit = new customMenuButton("Quit", this::fireExit);
        customMenuButton btnBrightness = new customMenuButton("Brightness", () -> {});
        customMenuButton btnVolume = new customMenuButton("Volume", () -> {});
        customMenuButton btnOptionsReturn = new customMenuButton("Return to Main Menu", () -> {
            vboxMainMenu.setVisible(true);
            vboxOptions.setVisible(false);
        });
        customMenuButton btnLoginReturn = new customMenuButton("Return to Main Menu", () -> {
            vboxMainMenu.setVisible(true);
            vboxLogin.setVisible(false);
        });

        //Creates a vbox for the main menu
        vboxMainMenu = new VBox(10,
                btnPlayGame,
                btnLogin,
                btnOptions,
                btnQuit,
                new Text(""),
                new LineSeparator(),
                menuUsernameText);
        vboxMainMenu.setTranslateX(100);
        vboxMainMenu.setTranslateY(450);

        //Creates a vbox for options
        vboxOptions = new VBox(10,
                btnBrightness,
                btnVolume,
                btnOptionsReturn,
                new Text(""),
                new LineSeparator(),
                optionsUsernameText);
        vboxOptions.setTranslateX(100);
        vboxOptions.setTranslateY(450);


        TextField usernameField = new TextField("Username");
        TextField passwordField = new TextField("Password");
        customMenuButton btnSubmit = new customMenuButton("Submit", () -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            int userId = Database.loginUser(username,password);
            if (userId == 0) {
                System.out.println("Username or password Incorrect");
            } else {
                System.out.println("SUCCESS");
                User user = User.getInstance();
                user.setUsername(username);
                user.setUserId(userId);

                Database.load(user.getUserId());
                usernameProperty.set("Logged in as " + user.getUsername());
            }
        });

        //Creates a vbox for Login
        vboxLogin = new VBox(10,
                usernameField,
                passwordField,
                btnSubmit,
                btnLoginReturn,
                new Text(""),
                new LineSeparator(),
                loginUsernameText);
        vboxLogin.setTranslateX(100);
        vboxLogin.setTranslateY(450);

        menuUsernameText.textProperty().bind(Bindings.convert(usernameProperty));
        optionsUsernameText.textProperty().bind(Bindings.convert(usernameProperty));
        loginUsernameText.textProperty().bind(Bindings.convert(usernameProperty));

        StackPane stackMenu = new StackPane(vboxMainMenu, vboxOptions, vboxLogin);
        vboxOptions.setVisible(false);
        vboxLogin.setVisible(false);
        getContentRoot().getChildren().addAll(stackMenu);
    }

    private static class customMenuButton extends StackPane {
        private String name;
        private Runnable action;
        private Text text;
        private Rectangle selector;

        public customMenuButton(String name, Runnable action) {
            this.name = name;
            this.action = action;

            //Calls the UI factory apart of FXGL to create a text box
            text = FXGL.getUIFactoryService().newText(name, Color.BLACK, 20.0);
            //This is the rectangle next to the buttons that show its been selected (color of button also changes)
            selector = new Rectangle(8, 20, Color.BLACK);
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
                setFocused(isHovered);
            });
            //Checks if the button is focused before allowed it to be pressed
            setOnMouseClicked(e -> {
                if (isFocused()) {
                    action.run();
                }
            });

            setOnKeyPressed(e -> {
                //Checks if the button is focused before allowing enter to work (in case mouse is broken)
                if (isFocused() && e.getCode() == KeyCode.ENTER) {
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
        private Rectangle line = new Rectangle(600, 2);

        public LineSeparator() {
            var gradient = new LinearGradient(0, 0, 0.5, 0.5, true, CycleMethod.NO_CYCLE,
                    new Stop(0, Color.BLACK),
                    new Stop(0.5, Color.GRAY),
                    new Stop(2.0, Color.TRANSPARENT));

            line.setFill(gradient);
            getChildren().add(line);
        }
    }
}
