package org.BobBuilders.FrenzyPenguins.ui;

import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.dsl.FXGL;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.BobBuilders.FrenzyPenguins.*;
import org.BobBuilders.FrenzyPenguins.util.Database;

import java.util.Random;

import static org.BobBuilders.FrenzyPenguins.CustomEntityFactory.fix_for_Mac;


public class CustomGameMenu extends FXGLMenu {
    private static final Color SELECTED_COLOR = Color.WHITE;
    private static final Color NOT_SELECTED_COLOR = Color.WHITE;
    private CustomGameMenu.customMenuButton purchaseJetpackBtn;
    private CustomGameMenu.customMenuButton purchaseGliderBtn;
    private CustomGameMenu.customMenuButton purchaseSlideBtn;
    private CustomGameMenu.customMenuButton equipJetpackBtn;
    private CustomGameMenu.customMenuButton equipGliderBtn;
    private CustomGameMenu.customMenuButton equipSlideBtn;
    private CustomGameMenu.customMenuButton unequipJetpackBtn;
    private CustomGameMenu.customMenuButton unequipGliderBtn;
    private CustomGameMenu.customMenuButton unequipSlideBtn;
    private customEquipmentButton upgradeJetpackBtn;
    private customEquipmentButton upgradeGliderBtn;
    private customEquipmentButton upgradeSlideBtn;
    private customEquipmentButton upgradeRampBtn;
    private VBox purchaseJetpackVbox;
    private VBox purchaseGliderVbox;
    private VBox purchaseSlideVbox;
    private VBox equipJetpackVbox;
    private VBox equipGliderVbox;
    private VBox equipSlideVbox;
    private VBox unequipJetpackVbox;
    private VBox unequipGliderVbox;
    private VBox unequipSlideVbox;
    private Inventory inventory;
    private Text jetPackLevel;
    private Text gliderLevel;
    private Text slideLevel;
    private double timer;
    private double snowHillsX = -300;
    private double snowHillsY = 265;
    private StackPane snowStack = new StackPane();
    private SimpleStringProperty usernameProperty = new SimpleStringProperty();

    public CustomGameMenu() {
        super(MenuType.GAME_MENU);

        this.inventory = Inventory.getInstance();

        Rectangle back = new Rectangle(getAppWidth(), getAppHeight());
        back.setFill(Color.CORNFLOWERBLUE);
        StackPane stack = new StackPane();

        ImageView snowHills1 = new ImageView("file:" + fix_for_Mac() + "snow_hills.png");
        snowHills1.setTranslateX(snowHillsX);
        snowHills1.setTranslateY(snowHillsY);

        ImageView snowHills2 = new ImageView("file:" + fix_for_Mac() + "snow_hills.png");
        snowHills2.setTranslateX(-snowHillsX);
        snowHills2.setTranslateY(snowHillsY);

        Text title = FXGL.getUIFactoryService().newText("Store", Color.WHITE, 70);
        title.setTranslateX(0);
        title.setTranslateY(-(getAppHeight() / 2 - 100));

        //Placeholder to demonstrate where username and points possessed are displayed
        Text userName = FXGL.getUIFactoryService().newText("Not logged in", Color.BLACK, 16);
        userName.setTranslateX(-(getAppWidth() / 2 - 150));
        userName.setTranslateY(-(getAppHeight() / 2 - 150));

        inventory.getPointsProperty().addListener(observable -> {
            if (User.getInstance().getUserId() == 0) {
                userName.setText("Not Logged in");
            } else {
                userName.setText("Logged in as " + User.getInstance().getUsername());
            }
        });

//        userName.textProperty().bind(Bindings.convert(usernameProperty));
        Text availablePoints = FXGL.getUIFactoryService().newText("Points available: " + inventory.getPointsPropertyValue(), Color.BLACK, 24);

        availablePoints.setTranslateX(getAppWidth() / 2 - 200);
        availablePoints.setTranslateY(-(getAppHeight() / 2 - 150));

        jetPackLevel = FXGL.getUIFactoryService().newText("" + this.inventory.getJetpackLevel(), Color.BLACK, 30);
        gliderLevel = FXGL.getUIFactoryService().newText("" + this.inventory.getGliderLevel(), Color.BLACK, 30);
        slideLevel = FXGL.getUIFactoryService().newText("" + this.inventory.getSlideLevel(), Color.BLACK, 30);

        availablePoints.textProperty().bind(Bindings.convert(inventory.getPointsProperty()));
        jetPackLevel.textProperty().bind(Bindings.convert(this.inventory.getJetPackLevelProperty()));
        gliderLevel.textProperty().bind(Bindings.convert(this.inventory.getGliderLevelProperty()));
        slideLevel.textProperty().bind(Bindings.convert(this.inventory.getSlideLevelProperty()));

        //Creates the buttons
        CustomGameMenu.customMenuButton btnResume = new CustomGameMenu.customMenuButton("Play", this::fireNewGame);
        CustomGameMenu.customMenuButton btnOptions = new CustomGameMenu.customMenuButton("Main Menu", this::fireExitToMainMenu);
        CustomGameMenu.customMenuButton btnMainMenu = new CustomGameMenu.customMenuButton("Quit", this::fireExitToMainMenu);
        updateButtons();


        //Creates the images for view of equipment
        Image jet = new Image("file:" + fix_for_Mac() + "jetpack.png");
        Image glider = new Image("file:" + fix_for_Mac() + "glider.png");
        Image sled = new Image("file:" + fix_for_Mac() + "sled.png");

        ImageView jetView = new ImageView(jet);
        ImageView jetViewEquipped = new ImageView(jet);
        ImageView jetViewUnequipped = new ImageView(jet);
        ImageView gliderView = new ImageView(glider);
        ImageView gliderViewEquipped = new ImageView(glider);
        ImageView gliderViewUnequipped = new ImageView(glider);
        ImageView sledView = new ImageView(sled);
        ImageView sledViewEquipped = new ImageView(sled);
        ImageView sledViewUnequipped = new ImageView(sled);

        //Establish proportional size for all images
        jetView.setFitHeight(150);
        jetView.setPreserveRatio(true);
        jetViewEquipped.setFitHeight(150);
        jetViewEquipped.setPreserveRatio(true);
        jetViewUnequipped.setFitHeight(150);
        jetViewUnequipped.setPreserveRatio(true);

        gliderView.setFitHeight(150);
        gliderView.setPreserveRatio(true);
        gliderViewEquipped.setFitHeight(150);
        gliderViewEquipped.setPreserveRatio(true);
        gliderViewUnequipped.setFitHeight(150);
        gliderViewUnequipped.setPreserveRatio(true);

        sledView.setFitHeight(150);
        sledView.setPreserveRatio(true);
        sledViewEquipped.setFitHeight(150);
        sledViewEquipped.setPreserveRatio(true);
        sledViewUnequipped.setFitHeight(150);
        sledViewUnequipped.setPreserveRatio(true);

        this.upgradeJetpackBtn = new customEquipmentButton("Test", this.inventory.getJetPackLevelProperty());
        this.upgradeGliderBtn = new customEquipmentButton("Test", this.inventory.getGliderLevelProperty());
        this.upgradeSlideBtn = new customEquipmentButton("Test", this.inventory.getSlideLevelProperty());
        this.upgradeRampBtn = new customEquipmentButton("Upgrade Ramp to level 1 \n10000$", this.inventory.getRampLevelProperty());

        //Creating a container for each option of purchase
        purchaseJetpackVbox = new VBox(10, jetView, purchaseJetpackBtn);
        purchaseJetpackVbox.setAlignment(Pos.CENTER);
        equipJetpackVbox = new VBox(10, jetViewEquipped, equipJetpackBtn, upgradeJetpackBtn);
        equipJetpackVbox.setAlignment(Pos.CENTER);
        unequipJetpackVbox = new VBox(10, jetViewUnequipped, unequipJetpackBtn, upgradeJetpackBtn);
        unequipJetpackVbox.setAlignment(Pos.CENTER);
        StackPane option1 = new StackPane(purchaseJetpackVbox, equipJetpackVbox, unequipJetpackVbox);

        purchaseGliderVbox = new VBox(10, gliderView, purchaseGliderBtn);
        purchaseGliderVbox.setAlignment(Pos.CENTER);
        equipGliderVbox = new VBox(10, gliderViewEquipped, equipGliderBtn, upgradeGliderBtn);
        equipGliderVbox.setAlignment(Pos.CENTER);
        unequipGliderVbox = new VBox(10, gliderViewUnequipped, unequipGliderBtn, upgradeGliderBtn);
        unequipGliderVbox.setAlignment(Pos.CENTER);
        StackPane option2 = new StackPane(purchaseGliderVbox, equipGliderVbox, unequipGliderVbox);

        purchaseSlideVbox = new VBox(10, sledView, purchaseSlideBtn);
        purchaseSlideVbox.setAlignment(Pos.CENTER);
        equipSlideVbox = new VBox(10, sledViewEquipped, equipSlideBtn, upgradeSlideBtn);
        equipSlideVbox.setAlignment(Pos.CENTER);
        unequipSlideVbox = new VBox(10, sledViewUnequipped, unequipSlideBtn, upgradeSlideBtn);
        unequipSlideVbox.setAlignment(Pos.CENTER);
        StackPane option3 = new StackPane(purchaseSlideVbox, equipSlideVbox, unequipSlideVbox);

        //Storing al container options into one
        HBox choices = new HBox(20, option1, option2, option3);
        choices.setAlignment(Pos.CENTER);
        choices.setPadding(new Insets(50));

        //Creates a vbox to store buttons unrelated to store purchases
        VBox vbox = new VBox(30,
                btnResume,
                btnOptions,
                btnMainMenu
        );
        vbox.setPadding(new Insets(50));

        GridPane container = new GridPane();
        container.setPadding(new Insets(10));
        container.add(vbox, 0, 0);
        container.add(choices, 1, 0);
        container.setGridLinesVisible(true);
        container.setAlignment(Pos.CENTER);

        upgradeRampBtn.setTranslateX(450);
        VBox upgradeRampVbox = new VBox(50, container, upgradeRampBtn);
        upgradeRampVbox.setAlignment(Pos.CENTER);
        upgradeRampVbox.setPadding(new Insets(20));

        snowStack.setTranslateX(-getAppWidth() / 2);
        snowStack.setTranslateY(-getAppHeight() / 2);

        stack.getChildren().addAll(back, snowStack, upgradeRampVbox, title, userName, availablePoints, snowHills1, snowHills2);
        getContentRoot().getChildren().addAll(stack);

        //Ensures that the buttons keep track of points obtained and whether or not equipment is owned

        this.inventory.getPointsProperty().addListener((observable, oldValue, newValue) -> updateButtons());
        this.inventory.getJetPackLevelProperty().addListener((observable, oldValue, newValue) -> updateButtons());
        this.inventory.getGliderLevelProperty().addListener((observable, oldValue, newValue) -> updateButtons());
        this.inventory.getSlideLevelProperty().addListener((observable, oldValue, newValue) -> updateButtons());
        this.inventory.getRampLevelProperty().addListener((observable, oldValue, newValue) -> updateButtons());

        //Makes buttons visible or invisible depending on whether or not equipment is owned and equipped
        //Checks if they're owned or not
        StringProperty test = new SimpleStringProperty();
        test.bind(Bindings.convert(this.inventory.getJetPackLevelProperty()));

        purchaseJetpackVbox.visibleProperty().bind(Bindings.equal(0, this.inventory.getJetPackLevelProperty()));
        purchaseGliderVbox.visibleProperty().bind(Bindings.equal(0, this.inventory.getGliderLevelProperty()));
        purchaseSlideVbox.visibleProperty().bind(Bindings.equal(0, this.inventory.getSlideLevelProperty()));


        equipJetpackVbox.visibleProperty().bind(Bindings.and(Bindings.not(this.inventory.hasEquippedJetpack()),
                Bindings.not(Bindings.equal(0, this.inventory.getJetPackLevelProperty()))));
        equipGliderVbox.visibleProperty().bind(Bindings.and(Bindings.not(this.inventory.hasEquippedGlider()),
                Bindings.not(Bindings.equal(0, this.inventory.getGliderLevelProperty()))));
        equipSlideVbox.visibleProperty().bind(Bindings.and(Bindings.not(this.inventory.hasEquippedSlide()),
                Bindings.not(Bindings.equal(0, this.inventory.getSlideLevelProperty()))));
        upgradeRampVbox.setVisible(true);

        unequipJetpackVbox.visibleProperty().bind(this.inventory.hasEquippedJetpack());
        unequipGliderVbox.visibleProperty().bind(this.inventory.hasEquippedGlider());
        unequipSlideVbox.visibleProperty().bind(this.inventory.hasEquippedSlide());
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
                if (isHovered) {
                    setFocused(true);
                } else {
                    setFocused(false);
                }
            });
            //Checks if the button is focused before allowed it to be pressed
            setOnMouseClicked(e -> {
                if (isFocused()) {
                    action.run();
                }
            });
            setOnKeyPressed(e -> {
                //Checks if the button is focused before allowing enter to work (incase mouse is broken)
                if (isFocused() && e.getCode() == KeyCode.ENTER) {
                    action.run();
                }
            });
            setAlignment(Pos.CENTER_LEFT);
            setFocusTraversable(true);
            getChildren().addAll(selector, text);
        }
    }

    private static class customEquipmentButton extends StackPane {
        Inventory inventory = Inventory.getInstance();
        private String name;
        private Runnable action;
        private Text text;
        private StringProperty stringProperty;
        private Rectangle selector;
        private IntegerProperty equipmentLevelProperty;

        public customEquipmentButton(String name, IntegerProperty equipmentLevelProperty) {
            this.name = name;
            this.equipmentLevelProperty = equipmentLevelProperty;
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

            //Equipment Functionality
            this.equipmentLevelProperty.addListener((observable, oldValue, newValue) -> {
                if (newValue.intValue() >= 10) {
                    if (equipmentLevelProperty.equals(this.inventory.getRampLevelProperty())) {
                        text.setText("Ramp Maxed out");
                    } else {
                        text.setText("Maxed Out");
                    }
                } else {
                    if (equipmentLevelProperty.equals(this.inventory.getRampLevelProperty())) {
                        double jetpackCost = 1000 * Math.pow(this.inventory.getRampLevel() + 1,2) + 5000;
                        text.setText("Upgrade Ramp to level " + (newValue.intValue() + 1) +
                                "\n" + jetpackCost + "$");
                    } else if (equipmentLevelProperty.equals(this.inventory.getJetPackLevelProperty())) {
                        double jetpackCost = 1000 * Math.pow(this.inventory.getJetpackLevel() + 1,2) + 5000;
                        text.setText("Upgrade to level " + (newValue.intValue() + 1) +
                                "\n" + jetpackCost + "$");
                    } else if (equipmentLevelProperty.equals(this.inventory.getGliderLevelProperty())) {
                        double gliderCost = 800 * Math.pow(this.inventory.getGliderLevel() + 1,2) + 3000;
                        text.setText("Upgrade to level " + (newValue.intValue() + 1) +
                                "\n" + gliderCost + "$");
                    } else if (equipmentLevelProperty.equals(this.inventory.getSlideLevelProperty())) {
                        double slideCost = 500 * Math.pow(this.inventory.getSlideLevel() + 1,2) + 2000;
                        text.setText("Upgrade to level " + (newValue.intValue() + 1) +
                                "\n" + slideCost + "$");
                    } else {
                        text.setText("Upgrade to level " + (newValue.intValue() + 1) +
                                "\n" + (newValue.intValue() + 1) * 10000 + "$");
                    }
                }
            });
            this.action = () -> {
                if (this.equipmentLevelProperty.getValue() < 10) {
                    if (equipmentLevelProperty.equals(this.inventory.getJetPackLevelProperty())) {
                        double jetpackCost = 1000 * Math.pow(equipmentLevelProperty.getValue() + 1,2) + 5000;
                        if (this.inventory.getPointsPropertyValue() > jetpackCost) {
                            this.inventory.addPoints((int) -jetpackCost);
                            this.equipmentLevelProperty.set(this.equipmentLevelProperty.getValue() + 1);
                        }
                    } else if (equipmentLevelProperty.equals(this.inventory.getGliderLevelProperty())) {
                        double gliderCost = 800 * Math.pow(equipmentLevelProperty.getValue(),2) + 3000;
                        if (this.inventory.getPointsPropertyValue() > gliderCost) {
                            this.inventory.addPoints((int) -gliderCost);
                            this.equipmentLevelProperty.set(this.equipmentLevelProperty.getValue() + 1);
                        }
                    } else if (equipmentLevelProperty.equals(this.inventory.getSlideLevelProperty())) {
                        double slideCost = 500 * Math.pow(equipmentLevelProperty.getValue(),2) + 2000;
                        if (this.inventory.getPointsPropertyValue() > slideCost) {
                            this.inventory.addPoints((int) -slideCost);
                            this.equipmentLevelProperty.set(this.equipmentLevelProperty.getValue() + 1);
                        }
                    } else if (equipmentLevelProperty.equals(this.inventory.getRampLevelProperty())) {
                        double jetpackCost = 500 * Math.pow(equipmentLevelProperty.getValue(),2) + 2000;
                        if (this.inventory.getPointsPropertyValue() > jetpackCost) {
                            this.inventory.addPoints((int) -jetpackCost);
                            this.equipmentLevelProperty.set(this.equipmentLevelProperty.getValue() + 1);
                        }
                    }

                    //User exists
                    if (User.getInstance().getUserId() != 0) {
                        Database.save(User.getInstance().getUserId(), this.inventory);
                    }
                }
            };

            hoverProperty().addListener((observableValue, aBoolean, isHovered) -> {
                if (isHovered) {
                    setFocused(true);
                } else {
                    setFocused(false);
                }
            });
            //Checks if the button is focused before allowed it to be pressed
            setOnMouseClicked(e -> {
                if (isFocused()) {
                    action.run();
                }
            });
            setOnKeyPressed(e -> {
                //Checks if the button is focused before allowing enter to work (incase mouse is broken)
                if (isFocused() && e.getCode() == KeyCode.ENTER) {
                    action.run();
                }
            });

            setAlignment(Pos.CENTER_LEFT);
            setFocusTraversable(true);
            getChildren().addAll(selector, text);
        }
    }

    protected void updateButtons() {
        this.purchaseJetpackBtn = new customMenuButton("Buy Jetpack - 10000$", () -> {
            if (this.inventory.getPointsPropertyValue() >= 5000) {
                this.inventory.setJetpackLevel(1);
                this.inventory.addPoints(-5000);
                updateButtons();
            }
        });
        this.purchaseGliderBtn = new customMenuButton(" Buy Glider - 2000$", () -> {
            if (this.inventory.getPointsPropertyValue() >= 3000) {
                this.inventory.setGliderLevel(1);
                this.inventory.addPoints(-3000);
            }
        });
        this.purchaseSlideBtn = new customMenuButton("Buy Snowboard - 3000$", () -> {
            if (this.inventory.getPointsPropertyValue() >= 2000) {
                this.inventory.setSlideLevel(1);
                this.inventory.addPoints(-2000);
                updateButtons();
            }
        });
        this.equipJetpackBtn = new customMenuButton("Equip Jetpack", () -> {
            this.inventory.setEquipJetpack(true);
        });
        this.equipGliderBtn = new customMenuButton("Equip Glider", () -> {
            this.inventory.setEquipGlider(true);
        });
        this.equipSlideBtn = new customMenuButton("Equip Slide", () -> {
            this.inventory.setEquipSlide(true);
        });
        this.unequipJetpackBtn = new customMenuButton("Unequip Jetpack", () -> {
            this.inventory.setEquipJetpack(false);
        });
        this.unequipGliderBtn = new customMenuButton("Unequip Glider", () -> {
            this.inventory.setEquipGlider(false);
        });
        this.unequipSlideBtn = new customMenuButton("Unequip Slide", () -> {
            this.inventory.setEquipSlide(false);
        });
    }

    @Override
    protected void onUpdate(double tpf) {
        super.onUpdate(tpf);
        timer += tpf;
        if (timer >= 1) {
            ImageView snowflakeImage = new ImageView("file:snowflake.png");
            snowflakeImage.setTranslateX(Math.random() * getAppWidth());
            snowflakeImage.setTranslateY(-50);
            snowflakeImage.setFitWidth(30);
            snowflakeImage.setPreserveRatio(true);

            Random random = new Random();
            int randomRotate = random.nextInt(4);
            if (randomRotate == 1) {
                snowflakeImage.setRotate(30);
            }
            if (randomRotate == 2) {
                snowflakeImage.setFitWidth(snowflakeImage.getFitWidth() * 0.7);
                snowflakeImage.setPreserveRatio(true);
            }
            if (randomRotate == 3) {
                snowflakeImage.setFitWidth(snowflakeImage.getFitWidth() * 0.4);
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

                    //Deletes the snowflakes after 90 seconds to ensure that the game doesn't start lagging
                    Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(90), event -> {
                        snowStack.getChildren().remove(snowflakeImage);
                    }));
                    timeline.setCycleCount(1);
                    timeline.play();
                }
            };
            // Start the animation
            animationTimer.start();
            timer = 0;
        }
    }
}
