package org.BobBuilders.FrenzyPenguins.ui;

import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.ui.FXGLButton;
import javafx.animation.AnimationTimer;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import org.BobBuilders.FrenzyPenguins.CustomEntityFactory;
import org.BobBuilders.FrenzyPenguins.Inventory;
import org.BobBuilders.FrenzyPenguins.User;
import org.BobBuilders.FrenzyPenguins.data.TableData;
import org.BobBuilders.FrenzyPenguins.util.Database;

import java.util.Random;

import static com.almasb.fxgl.dsl.FXGLForKtKt.getGameWorld;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;


public class CustomMainMenu extends FXGLMenu {
    private static final Color SELECTED_COLOR = Color.WHITE;
    private static final Color NOT_SELECTED_COLOR = Color.GRAY;
    private VBox vboxOptions;
    private VBox vboxMainMenu;
    private VBox vboxAccount;
    private StackPane loginStackPane = new StackPane();
    private VBox vboxLoggedIn;
    private VBox vboxAdminMenu = new VBox();

    private GridPane loadSelectGrid = new GridPane();

    Inventory inventory = Inventory.getInstance();

    private SimpleStringProperty usernameProperty = new SimpleStringProperty();
    private double timer = 0;
    private double penguinTimer = 0;
    private ImageView penguinView;
    private TableView<TableData> table = new TableView<>();
    ObservableList<TableData> tableList;
    TextField searchField = new TextField();
    StackPane snowStack = new StackPane();

    //    private ObjectProperty<customMenuButton> selectedButton;
    public CustomMainMenu() {
        super(MenuType.MAIN_MENU);


        getGameWorld().addEntityFactory(new CustomEntityFactory());

        ImageView mainMenuImage = new ImageView("file:pixel_mountain.png");
        mainMenuImage.setFitWidth(getAppWidth());
        mainMenuImage.setFitHeight(getAppHeight());
        createAdminMenu();

        ImageView snowflakeImage = new ImageView("file:snowflake.png");
        snowflakeImage.setTranslateX(Math.random()*getAppWidth());
        snowflakeImage.setTranslateY(-50);
        snowflakeImage.setFitWidth(30);
        snowflakeImage.setPreserveRatio(true);
        snowStack.getChildren().add(snowflakeImage);

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
            if (Objects.equals(menuUsernameText.getText(), "Not Logged in")) {
                vboxAccount.setVisible(true);
            } else {
                loginStackPane.setVisible(true);
            }
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
            loginStackPane.setVisible(false);
        });

        customMenuButton btnAdmin = new customMenuButton("Admin Menu", () -> {
            loginStackPane.setVisible(false);
            vboxAdminMenu.setVisible(true);
            vboxAdminMenu.setAlignment(Pos.CENTER);
            vboxAdminMenu.setTranslateX(50);
            //Removes the old table from the vbox
            vboxAdminMenu.getChildren().remove(this.table);
            //Requeries - needed if a user is created after the admin menu is initialized
            this.table = new TableView<>();
            this.table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
            this.table.setMaxWidth(TableData.TABLEWIDTH);
            this.tableList = Database.loadTable(this.table,this.searchField);
            //Inserts new table into the vbox
            vboxAdminMenu.getChildren().add(2,this.table);
        });

        customMenuButton btnLogout = new customMenuButton("Logout", () -> {
            vboxAccount.setVisible(true);
            loginStackPane.setVisible(false);
            vboxLoggedIn.getChildren().remove(btnAdmin);
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
        vboxMainMenu.setTranslateX(75);
        vboxMainMenu.setTranslateY(500);

        //Creates a vbox for options
        vboxOptions = new VBox(10,
                btnBrightness,
                btnVolume,
                btnOptionsReturn,
                new Text(""),
                new LineSeparator(),
                optionsUsernameText);
        vboxOptions.setTranslateX(75);
        vboxOptions.setTranslateY(500);

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
                this.inventory = Inventory.getInstance();
                usernameProperty.set("Logged in as " + user.getUsername());
                vboxAccount.setVisible(false);
                loginStackPane.setVisible(true);
                if (!this.inventory.equals(Database.loadInventory(userId))) {
                    vboxLoggedIn.setDisable(true);
                    loginStackPane.getChildren().add(loadSelectGrid);
                    loadSelectGrid.setTranslateY(0);
                    Text header = FXGL.getUIFactoryService().newText("Inventory Mismatch",Color.BLACK,30);
                    loadSelectGrid.add(header, 0, 0, 2,1);
                    loadSelectGrid.setAlignment(Pos.CENTER);
                    loadSelectGrid.setHalignment(header, HPos.CENTER);
                    loadSelectGrid.setHgap(30);
                    loadSelectGrid.add(FXGL.getUIFactoryService().newText("Inventory 1", Color.BLACK,20),0,1);
                    loadSelectGrid.add(FXGL.getUIFactoryService().newText("Inventory 2", Color.BLACK,20),1,1);
                    loadSelectGrid.add(FXGL.getUIFactoryService().newText(this.inventory.toString(), Color.BLACK,20),0,2);
                    loadSelectGrid.add(FXGL.getUIFactoryService().newText(Database.loadInventory(userId).toString(), Color.BLACK,20),1,2);
                    loadSelectGrid.add(new customMenuButton("Load inventory 1", () -> {
                        Database.save(User.getInstance().getUserId(), this.inventory);
                        loadSelectGrid.setVisible(false);
                        vboxLoggedIn.setDisable(false);
                    }),0,3);
                    loadSelectGrid.add(new customMenuButton("Load Inventory 2", () -> {
                        this.inventory.clone(Objects.requireNonNull(Database.loadInventory(User.getInstance().getUserId())));
                        loadSelectGrid.setVisible(false);
                        vboxLoggedIn.setDisable(false);
                    }), 1,3);
                } else {
                    this.inventory.clone(Objects.requireNonNull(Database.loadInventory(User.getInstance().getUserId())));
                }

                user.setAdmin(Database.getAdminStatus(userId));
                if (user.isAdmin()) {
                    vboxLoggedIn.getChildren().add(0, btnAdmin);
                }

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
                loginStackPane.setVisible(true);
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
        vboxAccount.setTranslateX(75);
        vboxAccount.setTranslateY(500);

        vboxLoggedIn = new VBox(10,
                btnLogout,
                btnLoggedInReturn,
                new Text(""),
                new LineSeparator(),
                loggedInUsernameText);
        vboxLoggedIn.setTranslateX(75);
        vboxLoggedIn.setTranslateY(500);

        menuUsernameText.textProperty().bind(Bindings.convert(usernameProperty));
        optionsUsernameText.textProperty().bind(Bindings.convert(usernameProperty));
        accountUsernameText.textProperty().bind(Bindings.convert(usernameProperty));
        loggedInUsernameText.textProperty().bind(Bindings.convert(usernameProperty));


        snowStack.setTranslateX(-getAppWidth()/2);
        snowStack.setTranslateY(-getAppHeight()/2);
        //Creating the Gridpane for login
        loginStackPane.getChildren().add(vboxLoggedIn);

        StackPane stackMenu = new StackPane(mainMenuImage, snowStack, vboxMainMenu, vboxOptions, vboxAccount, loginStackPane, vboxAdminMenu);
        vboxOptions.setVisible(false);
        vboxAccount.setVisible(false);
        loginStackPane.setVisible(false);
        vboxAdminMenu.setVisible(false);
        getContentRoot().getChildren().addAll(stackMenu);
    }

    protected void onUpdate(double tpf) {
        timer += tpf;
        if(timer >= 1){
            ImageView snowflakeImage = new ImageView("file:snowflake.png");
            snowflakeImage.setTranslateX(Math.random()*getAppWidth());
            snowflakeImage.setTranslateY(-50);
            snowflakeImage.setFitWidth(30);
            snowflakeImage.setPreserveRatio(true);

            Random random = new Random();
            int randomRotate = random.nextInt(4);
            if(randomRotate == 1){
                snowflakeImage.setRotate(30);
            }
            if(randomRotate == 2){
                snowflakeImage.setFitWidth(snowflakeImage.getFitWidth()*0.7);
                snowflakeImage.setPreserveRatio(true);
            }
            if(randomRotate == 3){
                snowflakeImage.setFitWidth(snowflakeImage.getFitWidth()*0.4);
                snowflakeImage.setPreserveRatio(true);
            }

            snowStack.getChildren().add(snowflakeImage);
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

        penguinTimer += tpf;
        if(penguinTimer >= 2 && penguinView == null){
            Random random = new Random();
            int randomPenguin = random.nextInt(4);
            switch (randomPenguin){
                case 0:penguinView = new ImageView("file:penguin.png");break;
                case 1:penguinView = new ImageView("file:penguin_and_glider.png");break;
                case 2:penguinView = new ImageView("file:penguin_and_sled.png");break;
                case 3:penguinView = new ImageView("file:jetpack_active.gif");break;
            }

            //Offset by 30 for penguin to spawn out of screen
            penguinView.setTranslateX(-30);
            penguinView.setTranslateY(getAppHeight() + 30);
            penguinView.setRotate(-30);
            penguinView.setFitHeight(125);
            penguinView.setPreserveRatio(true);
            snowStack.getChildren().add(penguinView);
            AnimationTimer animationTimer = new AnimationTimer() {
                @Override
                public void handle(long now) {
                    // Calculate the new Y position
                    double newY = penguinView.getTranslateY() - 10 * tpf;

                    // Set the new Y position
                    penguinView.setTranslateY(newY);

                    // Calculate the new Y position
                    double newX = penguinView.getTranslateX() + 12 * tpf;

                    // Set the new Y position
                    penguinView.setTranslateX(newX);

                    //Change rotation of penguin
                    penguinView.setRotate(penguinView.getRotate() + 5*tpf);

                    // Remove the animation when the snowflake is out of the screen
                    if (newX >= getAppWidth()) {
                        getRoot().getChildren().remove(penguinView);
                        this.stop(); // Stop the animation
                    }
                }
            };

            // Start the animation
            animationTimer.start();

            penguinTimer = 0;
        }
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
            text = FXGL.getUIFactoryService().newText(name, Color.WHITE, 20.0);
            //This is the rectangle next to the buttons that show its been selected (color of button also changes)
            selector = new Rectangle(8, 20, Color.WHITE);
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
                    new Stop(0, Color.GRAY),
                    new Stop(0.5, Color.WHITE),
                    new Stop(2.0, Color.TRANSPARENT));

            line.setFill(gradient);
            getChildren().add(line);
        }
    }

    //Creates admin menu and functionality
    private void createAdminMenu() {
        vboxAdminMenu.setAlignment(Pos.CENTER);
        vboxAdminMenu.setSpacing(10);
        //Making the UI
        Text header = FXGL.getUIFactoryService().newText("Admin Menu", Color.BLACK,20);
        header.setStyle("-fx-underline: true;");
        HBox textfieldHbox = new HBox();
        this.searchField.setPromptText("Keywords...");
        textfieldHbox.getChildren().addAll(FXGL.getUIFactoryService().newText("Search User", Color.BLACK, 14), this.searchField);
        textfieldHbox.setSpacing(10);
        textfieldHbox.setAlignment(Pos.CENTER_LEFT);

        //Defining the Table
        this.tableList = Database.loadTable(this.table,this.searchField);
        System.out.println(tableList);

        HBox bottomButtons = new HBox();
        customMenuButton back = new customMenuButton("Back", () -> {
            vboxAdminMenu.setVisible(false);
            loginStackPane.setVisible(true);
        });
        customMenuButton delete = new customMenuButton("Delete", () -> {
            boolean adminReference = false;
            ArrayList<TableData> usersToDelete = new ArrayList<>();

            for (TableData e : tableList) {
                if (e.getDelete().isSelected()) {
                    if (Database.getAdminStatus(e.getUserId())) {
                        adminReference = true;
                        break;
                    }
                    usersToDelete.add(e);
                }
            }
            if (adminReference) {
                Alert self = new Alert(
                        Alert.AlertType.WARNING,
                        "You can not delete an admin account.\nNice try :P",
                        ButtonType.OK);
                self.setTitle("Admin Account Detected");
                self.setHeaderText("Can not delete admins!");
                self.showAndWait();
            } else {
                Alert confirmation = new Alert(
                        Alert.AlertType.WARNING,
                        "",
                        ButtonType.YES, ButtonType.CANCEL
                );
                confirmation.setTitle("Warning!");
                confirmation.setHeaderText("Are you sure you want to delete ");
                if (usersToDelete.size() > 1) {
                    confirmation.setHeaderText(confirmation.getHeaderText() + "the users?");
                    confirmation.setContentText(usersToDelete.size() + " users will be deleted, are you sure? ");
                } else {
                    confirmation.setHeaderText(confirmation.getHeaderText() + "the user?");
                    confirmation.setContentText(usersToDelete.size() + " user will be deleted, are you sure? ");
                }
                Optional<ButtonType> result = confirmation.showAndWait();
                if (!result.isPresent()) {
                    // alert exited
                } else if (result.get() == ButtonType.YES) {
                    for (TableData e : usersToDelete) {
                        Database.delete(e.getUserId());
                        tableList.remove(e);
                    }
                } else if (result.get() == ButtonType.CANCEL) {
                    // cancel button is pressed
                }
            }
        });

        bottomButtons.getChildren().addAll(back, delete);
        //Needed to allign the table again
        bottomButtons.setSpacing(400);
        textfieldHbox.setTranslateX(200);
        bottomButtons.setTranslateX(200);
        vboxAdminMenu.getChildren().addAll(header, textfieldHbox, this.table, bottomButtons);
    }
}
