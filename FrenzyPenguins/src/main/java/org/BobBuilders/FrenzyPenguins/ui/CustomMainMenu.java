package org.BobBuilders.FrenzyPenguins.ui;

import com.almasb.fxgl.animation.Interpolators;
import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.texture.Texture;
import javafx.animation.AnimationTimer;
import javafx.animation.TranslateTransition;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.BobBuilders.FrenzyPenguins.CustomEntityFactory;
import org.BobBuilders.FrenzyPenguins.EntityType;
import org.BobBuilders.FrenzyPenguins.Inventory;
import org.BobBuilders.FrenzyPenguins.User;
import org.BobBuilders.FrenzyPenguins.util.Database;

import java.util.Random;

import static com.almasb.fxgl.dsl.FXGLForKtKt.getGameWorld;


public class CustomMainMenu extends FXGLMenu {
    private static final Color SELECTED_COLOR = Color.BLACK;
    private static final Color NOT_SELECTED_COLOR = Color.GRAY;
    private VBox vboxOptions;
    private VBox vboxMainMenu;
    private VBox vboxAccount;
    private VBox vboxLoggedIn;
    private SimpleStringProperty usernameProperty = new SimpleStringProperty();
    private double timer = 0;
    private double penguinTimer = 0;

    //    private ObjectProperty<customMenuButton> selectedButton;
    public CustomMainMenu() {
        super(MenuType.MAIN_MENU);

        getGameWorld().addEntityFactory(new CustomEntityFactory());

        ImageView mainMenuImage = new ImageView("file:pixel_mountain.png");
        mainMenuImage.setFitWidth(getAppWidth());
        mainMenuImage.setFitHeight(getAppHeight());

        if (User.getInstance().getUserId() == 0) {
            usernameProperty.set("Not Logged in");
        } else {
            usernameProperty.set("Logged in as " + User.getInstance().getUsername());
        }

        Text menuUsernameText = FXGL.getUIFactoryService().newText("Not Logged in", Color.GRAY, 15);
        Text accountUsernameText = FXGL.getUIFactoryService().newText("Not Logged in", Color.GRAY, 15);
        Text optionsUsernameText = FXGL.getUIFactoryService().newText("Not Logged in", Color.GRAY, 15);
        Text loggedInUsernameText = FXGL.getUIFactoryService().newText("Not Logged in", Color.GRAY, 15);


        //Creates the buttons
        customMenuButton btnPlayGame = new customMenuButton("Play Game", this::fireNewGame);
        customMenuButton btnAccount = new customMenuButton("Account", () -> {
            vboxMainMenu.setVisible(false);
            vboxAccount.setVisible(true);
        });

        customMenuButton btnOptions = new customMenuButton("Options", () -> {
            vboxMainMenu.setVisible(false);
            vboxOptions.setVisible(true);
        });
        customMenuButton btnQuit = new customMenuButton("Quit", this::fireExit);
        customMenuButton btnBrightness = new customMenuButton("Brightness", () -> {
        });
        customMenuButton btnVolume = new customMenuButton("Volume", () -> {
        });
        customMenuButton btnOptionsReturn = new customMenuButton("Return to Main Menu", () -> {
            vboxMainMenu.setVisible(true);
            vboxOptions.setVisible(false);
        });
        customMenuButton btnAccountReturn = new customMenuButton("Return to Main Menu", () -> {
            vboxMainMenu.setVisible(true);
            vboxAccount.setVisible(false);
        });

        customMenuButton btnLoggedInReturn = new customMenuButton("Return to Main Menu", () -> {
            vboxMainMenu.setVisible(true);
            vboxLoggedIn.setVisible(false);
        });

        customMenuButton btnLogout = new customMenuButton("Logout", () -> {
            vboxAccount.setVisible(true);
            vboxLoggedIn.setVisible(false);
            usernameProperty.set("Not Logged in");
        });

        //Creates a vbox for the main menu
        vboxMainMenu = new VBox(10,
                btnPlayGame,
                btnAccount,
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

        //Account
        customTextField usernameField = new customTextField("Username");
        customTextField passwordField = new customTextField("Password");
        customMenuButton btnLogin = new customMenuButton("Login", () -> {
            String username = usernameField.getInput();
            String password = passwordField.getInput();
            int userId = Database.loginUser(username, password);
            if (userId == -1) {
                usernameField.hideTaken();
                usernameField.showIncorrect();
                passwordField.showIncorrect();
            } else {
                usernameField.hideTaken();
                usernameField.hideIncorrect();
                passwordField.hideIncorrect();
                User user = User.getInstance();
                user.setUsername(username);
                user.setUserId(userId);

                Database.load(user.getUserId());
                usernameProperty.set("Logged in as " + user.getUsername());
                vboxAccount.setVisible(false);
                vboxLoggedIn.setVisible(true);
            }
        });

        customMenuButton btnCreateAccount = new customMenuButton("Create account", () -> {
            String username = usernameField.getInput();
            String password = passwordField.getInput();
            if (Database.createUser(username, password)) {
                usernameField.hideTaken();
                usernameField.hideIncorrect();
                passwordField.hideIncorrect();

                User user = User.getInstance();
                user.setUserId(Database.loginUser(username, password));
                user.setUsername(username);

                Database.save(user.getUserId(), Inventory.getInstance());
                usernameProperty.set("Logged in as " + user.getUsername());
                vboxAccount.setVisible(false);
                vboxLoggedIn.setVisible(true);
            } else {
                usernameField.hideIncorrect();
                passwordField.hideIncorrect();
                usernameField.showTaken();
            }
        });

        //Creates a vbox for Account
        vboxAccount = new VBox(10,
                usernameField,
                passwordField,
                btnLogin,
                btnCreateAccount,
                btnAccountReturn,
                new Text(""),
                new LineSeparator(),
                accountUsernameText);
        vboxAccount.setTranslateX(100);
        vboxAccount.setTranslateY(450);

        vboxLoggedIn = new VBox(10,
                btnLogout,
                btnLoggedInReturn,
                new Text(""),
                new LineSeparator(),
                loggedInUsernameText);
        vboxLoggedIn.setTranslateX(100);
        vboxLoggedIn.setTranslateY(450);

        menuUsernameText.textProperty().bind(Bindings.convert(usernameProperty));
        optionsUsernameText.textProperty().bind(Bindings.convert(usernameProperty));
        accountUsernameText.textProperty().bind(Bindings.convert(usernameProperty));
        loggedInUsernameText.textProperty().bind(Bindings.convert(usernameProperty));


        StackPane stackMenu = new StackPane(mainMenuImage, vboxMainMenu, vboxOptions, vboxAccount, vboxLoggedIn);
        vboxOptions.setVisible(false);
        vboxAccount.setVisible(false);
        vboxLoggedIn.setVisible(false);
        getContentRoot().getChildren().addAll(stackMenu);
    }

    protected void onUpdate(double tpf) {
        timer += tpf;
        if(timer >= 2){
            ImageView snowflakeImage = new ImageView("file:snowflake.png");
            snowflakeImage.setTranslateX(Math.random()*getAppWidth());
            snowflakeImage.setTranslateY(-50);
            snowflakeImage.setFitWidth(30);
            snowflakeImage.setPreserveRatio(true);

            Random random = new Random();
            int randomRotate = random.nextInt(3);
            if(randomRotate == 1){
                snowflakeImage.setRotate(30);
            }
            if(randomRotate == 2){
                snowflakeImage.setRotate(50);
            }
            getRoot().getChildren().add(snowflakeImage);
            AnimationTimer animationTimer = new AnimationTimer() {
                @Override
                public void handle(long now) {
                    // Calculate the new Y position
                    double newY = snowflakeImage.getTranslateY() + 10 * tpf;

                    // Set the new Y position
                    snowflakeImage.setTranslateY(newY);

                    // Remove the animation when the snowflake is out of the screen
                    if (newY >= getAppHeight()) {
                        getRoot().getChildren().remove(snowflakeImage);
                        this.stop(); // Stop the animation
                    }
                }
            };

            // Start the animation
            animationTimer.start();

            timer = 0;
        }

        ImageView penguinView = new ImageView("file:penguin.png");
        penguinView.setRotate(-30);
        penguinView.setFitHeight(125);
        penguinView.setPreserveRatio(true);
        getRoot().getChildren().add(penguinView);
    }

    private static class customTextField extends StackPane {

        private String name;
        private TextField textField;
        private Text incorrectSubtext;
        private Text takenSubtext;

        public customTextField(String name) {
            this.name = name;

            textField = new TextField();
            textField.setPromptText(name);
            textField.setMaxWidth(300);

            incorrectSubtext = FXGL.getUIFactoryService().newText("Wrong username or password", Color.RED, 10);
            incorrectSubtext.setTranslateX(5);
            incorrectSubtext.setVisible(false);

            takenSubtext = FXGL.getUIFactoryService().newText("Username already taken", Color.RED, 10);
            takenSubtext.setTranslateX(5);
            takenSubtext.setVisible(false);

//            textField.setStyle("-fx-text-box-border: red; -fx-focus-color: red;");
            StackPane subtextStack = new StackPane();
            subtextStack.getChildren().addAll(incorrectSubtext, takenSubtext);
            subtextStack.setAlignment(Pos.CENTER_LEFT);

            VBox vBox = new VBox();
            vBox.getChildren().addAll(textField, subtextStack);
            setAlignment(Pos.CENTER_LEFT);
            getChildren().addAll(vBox);

        }

        public String getInput() {
            return textField.getText();
        }

        public void showIncorrect() {
            incorrectSubtext.setVisible(true);
            textField.setStyle("-fx-text-box-border: red; -fx-focus-color: red;");
        }

        public void hideIncorrect() {
            incorrectSubtext.setVisible(false);
            textField.setStyle("-fx-text-box-border: black; -fx-focus-color: black;");
        }

        public void showTaken() {
            takenSubtext.setVisible(true);
            textField.setStyle("-fx-text-box-border: red; -fx-focus-color: red;");
            System.out.println("Hello");
        }

        public void hideTaken() {
            takenSubtext.setVisible(false);
            textField.setStyle("-fx-text-box-border: black; -fx-focus-color: black;");
        }

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
