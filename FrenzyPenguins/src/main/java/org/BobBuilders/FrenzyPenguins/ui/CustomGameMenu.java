package org.BobBuilders.FrenzyPenguins.ui;

import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.dsl.FXGL;
import javafx.animation.AnimationTimer;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import org.BobBuilders.FrenzyPenguins.*;

import java.util.Random;

import static org.BobBuilders.FrenzyPenguins.CustomEntityFactory.fix_for_Mac;


public class CustomGameMenu extends FXGLMenu {
    private static final Color SELECTED_COLOR = Color.WHITE;
    private static final Color NOT_SELECTED_COLOR = Color.WHITE;
    private CustomGameMenu.customMenuButton btnOptions1;
    private CustomGameMenu.customMenuButton btnOptions2;
    private CustomGameMenu.customMenuButton btnOptions3;
    private CustomGameMenu.customMenuButton equipGear1;
    private CustomGameMenu.customMenuButton equipGear2;
    private CustomGameMenu.customMenuButton equipGear3;
    private CustomGameMenu.customMenuButton unequipGear1;
    private CustomGameMenu.customMenuButton unequipGear2;
    private CustomGameMenu.customMenuButton unequipGear3;
    private CustomGameMenu.customMenuButton upgradeJetpack;
    private CustomGameMenu.customMenuButton upgradeGlider;
    private CustomGameMenu.customMenuButton upgradeSlide;
    private CustomGameMenu.customMenuButton upgradeRamp;
    private VBox purchase1;
    private VBox purchase2;
    private VBox purchase3;
    private VBox equip1;
    private VBox equip2;
    private VBox equip3;
    private VBox unequip1;
    private VBox unequip2;
    private VBox unequip3;
    private Inventory inventory;
    private Store store;
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

        if (User.getInstance().getUserId() == 0) {
            usernameProperty.set("Not Logged in");
        } else {
            usernameProperty.set("Logged in as " + User.getInstance().getUsername());
        }
        inventory = Inventory.getInstance();
        store = Store.getInstance();

        Rectangle back = new Rectangle(getAppWidth(), getAppHeight());
        back.setFill(Color.CORNFLOWERBLUE);
        StackPane stack = new StackPane();

        ImageView snowHills1 = new ImageView("file:"+fix_for_Mac()+"snow_hills.png");
        snowHills1.setTranslateX(snowHillsX);
        snowHills1.setTranslateY(snowHillsY);

        ImageView snowHills2 = new ImageView("file:"+fix_for_Mac()+"snow_hills.png");
        snowHills2.setTranslateX(-snowHillsX);
        snowHills2.setTranslateY(snowHillsY);

        Text title = FXGL.getUIFactoryService().newText("Store", Color.WHITE, 70);
        title.setTranslateX(0);
        title.setTranslateY(-(getAppHeight() / 2 - 100));

        //Placeholder to demonstrate where username and points possessed are displayed
        Text userName = FXGL.getUIFactoryService().newText("Not logged in", Color.BLACK, 24);
        userName.setTranslateX(-(getAppWidth() / 2 - 150));
        userName.setTranslateY(-(getAppHeight() / 2 - 150));

        userName.textProperty().bind(Bindings.convert(usernameProperty));

        Text availablePoints = FXGL.getUIFactoryService().newText("Points available: " + inventory.getPointsPropertyValue(), Color.BLACK, 24);

        availablePoints.setTranslateX(getAppWidth() / 2 - 200);
        availablePoints.setTranslateY(-(getAppHeight() / 2 - 150));

        jetPackLevel = FXGL.getUIFactoryService().newText("" + store.getJetPackLevel(), Color.BLACK, 30);
        gliderLevel = FXGL.getUIFactoryService().newText("" + store.getGliderLevel(), Color.BLACK, 30);
        slideLevel = FXGL.getUIFactoryService().newText("" + store.getSlideLevel(), Color.BLACK, 30);

        availablePoints.textProperty().bind(Bindings.convert(inventory.getPointsProperty()));
        jetPackLevel.textProperty().bind(Bindings.convert(store.getJetPackLevelProperty()));
        gliderLevel.textProperty().bind(Bindings.convert(store.getGliderLevelProperty()));
        slideLevel.textProperty().bind(Bindings.convert(store.getSlideLevelProperty()));

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


        //Creating a container for each option of purchase
        purchase1 = new VBox(10, jetView, btnOptions1);
        purchase1.setAlignment(Pos.CENTER);
        equip1 = new VBox(10, jetViewEquipped, equipGear1,upgradeJetpack);
        equip1.setAlignment(Pos.CENTER);
        unequip1 = new VBox(10, jetViewUnequipped, unequipGear1,upgradeJetpack);
        unequip1.setAlignment(Pos.CENTER);
        StackPane option1 = new StackPane(purchase1, equip1, unequip1);

        purchase2 = new VBox(10, gliderView, btnOptions2);
        purchase2.setAlignment(Pos.CENTER);
        equip2 = new VBox(10, gliderViewEquipped, equipGear2,upgradeGlider);
        equip2.setAlignment(Pos.CENTER);
        unequip2 = new VBox(10, gliderViewUnequipped, unequipGear2,upgradeGlider);
        unequip2.setAlignment(Pos.CENTER);
        StackPane option2 = new StackPane(purchase2, equip2, unequip2);

        purchase3 = new VBox(10, sledView, btnOptions3);
        purchase3.setAlignment(Pos.CENTER);
        equip3 = new VBox(10, sledViewEquipped, equipGear3,upgradeSlide);
        equip3.setAlignment(Pos.CENTER);
        unequip3 = new VBox(10, sledViewUnequipped, unequipGear3,upgradeSlide);
        unequip3.setAlignment(Pos.CENTER);
        StackPane option3 = new StackPane(purchase3, equip3, unequip3);

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

        upgradeRamp.setTranslateX(450);
        VBox endContainer = new VBox(50,container,upgradeRamp);
        endContainer.setAlignment(Pos.CENTER);
        endContainer.setPadding(new Insets(20));

        snowStack.setTranslateX(-getAppWidth()/2);
        snowStack.setTranslateY(-getAppHeight()/2);

        stack.getChildren().addAll(back, snowStack, endContainer, title, userName, availablePoints,snowHills1,snowHills2);
        getContentRoot().getChildren().addAll(stack);

        //Ensures that the buttons keep track of points obtained and whether or not equipment is owned

        inventory.getPointsProperty().addListener((observable, oldValue, newValue) -> updateButtons());
        inventory.hasJetpackProperty().addListener((observable, oldValue, newValue) -> updateButtons());
        inventory.hasGliderProperty().addListener((observable, oldValue, newValue) -> updateButtons());
        inventory.hasSlideProperty().addListener((observable, oldValue, newValue) -> updateButtons());
        store.getJetPackLevelProperty().addListener((observable,oldValue,newValue) -> updateButtons());
        store.getGliderLevelProperty().addListener((observable,oldValue,newValue) -> updateButtons());
        store.getSlideLevelProperty().addListener((observable,oldValue,newValue) -> updateButtons());

        //Makes buttons visible or invisible depending on whether or not equipment is owned and equipped

        purchase1.visibleProperty().bind(Bindings.not(inventory.hasJetpackProperty()));
        equip1.visibleProperty().bind(Bindings.and(inventory.hasJetpackProperty(), Bindings.not(store.hasEquippedJetpack())));
        unequip1.visibleProperty().bind(Bindings.and(inventory.hasJetpackProperty(), store.hasEquippedJetpack()));
        purchase2.visibleProperty().bind(Bindings.not(inventory.hasGliderProperty()));
        equip2.visibleProperty().bind(Bindings.and(inventory.hasGliderProperty(), Bindings.not(store.hasEquippedGlider())));
        unequip2.visibleProperty().bind(Bindings.and(inventory.hasGliderProperty(), store.hasEquippedGlider()));
        purchase3.visibleProperty().bind(Bindings.not(inventory.hasSlideProperty()));
        equip3.visibleProperty().bind(Bindings.and(inventory.hasSlideProperty(), Bindings.not(store.hasEquippedSlide())));
        unequip3.visibleProperty().bind(Bindings.and(inventory.hasSlideProperty(), store.hasEquippedSlide()));
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

    protected void updateButtons() {
        Inventory inventory = Inventory.getInstance();
        Store store = Store.getInstance();
        btnOptions1 = new CustomGameMenu.customMenuButton("Buy Jetpack - 10000$", () -> {
            if (inventory.getPointsPropertyValue() >= 10000) {
                inventory.setHasJetpack(true);
                inventory.addPoints(-10000);
                updateButtons();
            }
        });
        btnOptions2 = new CustomGameMenu.customMenuButton(" Buy Glider - 2000$", () -> {
            if (inventory.getPointsPropertyValue() >= 2000) {
                inventory.setHasGlider(true);
                inventory.addPoints(-2000);
            }
        });
        btnOptions3 = new CustomGameMenu.customMenuButton("Buy Snowboard - 3000$", () -> {
            if (inventory.getPointsPropertyValue() >= 3000) {
                inventory.setHasSlide(true);
                inventory.addPoints(-3000);
                updateButtons();
            }
        });
        equipGear1 = new CustomGameMenu.customMenuButton("Equip Jetpack", () -> {
            store.setEquipJetpack(true);
//            store.setEquipSlide(false);
//            store.setEquipGlider(false);
        });
        equipGear2 = new CustomGameMenu.customMenuButton("Equip Glider", () -> {
            store.setEquipGlider(true);
//            store.setEquipJetpack(false);
//            store.setEquipSlide(false);
        });
        equipGear3 = new CustomGameMenu.customMenuButton("Equip Slide", () -> {
            store.setEquipSlide(true);
//            store.setEquipJetpack(false);
//            store.setEquipGlider(false);
        });
        unequipGear1 = new CustomGameMenu.customMenuButton("Unequip Jetpack", () -> {
            store.setEquipJetpack(false);
        });
        unequipGear2 = new CustomGameMenu.customMenuButton("Unequip Glider", () -> {
            store.setEquipGlider(false);
        });
        unequipGear3 = new CustomGameMenu.customMenuButton("Unequip Slide", () -> {
            store.setEquipSlide(false);
        });
        upgradeJetpack = new CustomGameMenu.customMenuButton("Upgrade Jetpack (" + (10000*(store.getJetPackLevel() + 1)) + "$)\nLevel " + store.getJetPackLevel(), ()->{
            //If statement ensures you can only upgrade jetpack up a certain amount
            if(store.getJetPackLevel() < 10 && inventory.getPointsPropertyValue() >= 10000*(store.getJetPackLevel()+1)) {
                //Increases the jetpack level by 1 once pressed
                store.setJetPackLevel(store.getJetPackLevel() + 1);
                store.setJetPackLevelProperty(store.getJetPackLevel());
                inventory.addPoints(-(10000*store.getJetPackLevel()));
            }
        });
        upgradeGlider = new CustomGameMenu.customMenuButton("Upgrade Glider (" + (10000*(store.getGliderLevel()+1)) + "$)\nLevel "+ store.getGliderLevel(), ()->{
            //If statement ensures you can only upgrade glider up a certain amount
            if(store.getGliderLevel() < 10 && inventory.getPointsPropertyValue() >= 10000*(store.getGliderLevel() + 1)) {
                //Increases the glider level by 1 once pressed
                store.setGliderLevel(store.getGliderLevel() + 1);
                store.setGliderLevelProperty(store.getGliderLevel());
                inventory.addPoints(-(10000*store.getGliderLevel()));
            }
        });
        upgradeSlide = new CustomGameMenu.customMenuButton("Upgrade Sled (" + 10000*(store.getSlideLevel() + 1) + "$)\nLevel " + store.getSlideLevel(), ()->{
            //If statement ensures you can only upgrade sled up a certain amount
            if(store.getSlideLevel() < 10 && inventory.getPointsPropertyValue() >= 10000*(store.getSlideLevel() + 1)) {
                //Increases the sled level by 1 once pressed
                store.setSlideLevel(store.getSlideLevel() + 1);
                store.setSlideLevelProperty(store.getSlideLevel());
                inventory.addPoints(-(10000*store.getSlideLevel()));
            }
        });
        upgradeRamp = new CustomGameMenu.customMenuButton("Upgrade Ramp (" + 10000*(store.getRampLevel()) + "$)\nLevel" + store.getRampLevel(), () ->{
            //If statement ensures you can only upgrade sled up a certain amount
            if(store.getRampLevel() < 10 && inventory.getPointsPropertyValue() >= 10000*(store.getRampLevel())){
                //Increases ramp level by 1 once pressed
                store.setRampLevel(store.getRampLevel() + 1);
                store.setRampLevelProperty(store.getSlideLevel());
                inventory.addPoints(-(10000*store.getSlideLevel()));
            }
        });
    }

    @Override
    protected void onUpdate(double tpf) {
        super.onUpdate(tpf);
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
    }
}
