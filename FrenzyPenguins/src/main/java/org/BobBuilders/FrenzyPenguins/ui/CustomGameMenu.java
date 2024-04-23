package org.BobBuilders.FrenzyPenguins.ui;

import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.dsl.FXGL;
import javafx.beans.binding.Bindings;
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
import static org.BobBuilders.FrenzyPenguins.CustomEntityFactory.fix_for_Mac;


public class CustomGameMenu extends FXGLMenu {
    private static final Color SELECTED_COLOR = Color.BLACK;
    private static final Color NOT_SELECTED_COLOR = Color.GRAY;
    private CustomGameMenu.customMenuButton btnOptions1;
    private CustomGameMenu.customMenuButton btnOptions2;
    private CustomGameMenu.customMenuButton btnOptions3;
    private CustomGameMenu.customMenuButton equipGear1;
    private CustomGameMenu.customMenuButton equipGear2;
    private CustomGameMenu.customMenuButton equipGear3;
    private CustomGameMenu.customMenuButton unequipGear1;
    private CustomGameMenu.customMenuButton unequipGear2;
    private CustomGameMenu.customMenuButton unequipGear3;
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

    public CustomGameMenu() {
        super(MenuType.GAME_MENU);

        inventory = Inventory.getInstance();
        store = Store.getInstance();


        Rectangle back = new Rectangle(getAppWidth(), getAppHeight());
        back.setFill(Color.WHITESMOKE);
        StackPane stack = new StackPane();
        getContentRoot().setBackground(new Background(new BackgroundFill(Color.WHITESMOKE, CornerRadii.EMPTY, null)));
        Text title = FXGL.getUIFactoryService().newText("Store", Color.BLACK, 70);
        title.setTranslateX(0);
        title.setTranslateY(-(getAppHeight() / 2 - 100));

        FallingPenguinGame fall = new FallingPenguinGame();

        //Placeholder to demonstrate where username and points possessed are displayed
        Text userName = FXGL.getUIFactoryService().newText("Username: sample123", Color.BLACK, 30);
        userName.setTranslateX(-(getAppWidth() / 2 - 200));
        userName.setTranslateY(-(getAppHeight() / 2 - 150));

        Text availablePoints = FXGL.getUIFactoryService().newText("Points available: " + inventory.getPointsPropertyValue(), Color.BLACK, 30);
        availablePoints.setTranslateX(getAppWidth() / 2 - 200);
        availablePoints.setTranslateY(-(getAppHeight() / 2 - 150));

        availablePoints.textProperty().bind(Bindings.convert(inventory.getPointsProperty()));

        //Creates the buttons
        CustomGameMenu.customMenuButton btnResume = new CustomGameMenu.customMenuButton("Restart", this::fireNewGame);
        CustomGameMenu.customMenuButton btnOptions = new CustomGameMenu.customMenuButton("Main Menu", this::fireExitToMainMenu);
        CustomGameMenu.customMenuButton btnMainMenu = new CustomGameMenu.customMenuButton("Quit", this::fireExitToMainMenu);
        updateButtons();


        //Creates the images for view of equipment
        Image jet = new Image("file:"+fix_for_Mac()+"jetpack.png");
        Image glider = new Image("file:"+fix_for_Mac()+"glider.png");
        Image sled = new Image("file:"+fix_for_Mac()+"sled.png");

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
        equip1 = new VBox(10, jetViewEquipped, equipGear1);
        equip1.setAlignment(Pos.CENTER);
        unequip1 = new VBox(10, jetViewUnequipped, unequipGear1);
        unequip1.setAlignment(Pos.CENTER);
        StackPane option1 = new StackPane(purchase1, equip1, unequip1);

        purchase2 = new VBox(10, gliderView, btnOptions2);
        purchase2.setAlignment(Pos.CENTER);
        equip2 = new VBox(10, gliderViewEquipped, equipGear2);
        equip2.setAlignment(Pos.CENTER);
        unequip2 = new VBox(10, gliderViewUnequipped, unequipGear2);
        unequip2.setAlignment(Pos.CENTER);
        StackPane option2 = new StackPane(purchase2, equip2, unequip2);

        purchase3 = new VBox(10, sledView, btnOptions3);
        purchase3.setAlignment(Pos.CENTER);
        equip3 = new VBox(10, sledViewEquipped, equipGear3);
        equip3.setAlignment(Pos.CENTER);
        unequip3 = new VBox(10, sledViewUnequipped, unequipGear3);
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

        stack.getChildren().addAll(back, container, title, userName, availablePoints);
        getContentRoot().getChildren().addAll(stack);

        inventory.getPointsProperty().addListener((observable, oldValue, newValue) -> updateButtons());
        inventory.hasJetpackProperty().addListener((observable, oldValue, newValue) -> updateButtons());
        inventory.hasGliderProperty().addListener((observable, oldValue, newValue) -> updateButtons());
        inventory.hasSlideProperty().addListener((observable, oldValue, newValue) -> updateButtons());

//        btnOptions1.visibleProperty().bind(Bindings.not(inventory.hasJetpackProperty()));
//        btnOptions2.visibleProperty().bind(Bindings.not(inventory.hasGliderProperty()));
//        btnOptions3.visibleProperty().bind(Bindings.not(inventory.hasSlideProperty()));

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
            store.setEquipSlide(false);
            store.setEquipGlider(false);
        });
        equipGear2 = new CustomGameMenu.customMenuButton("Equip Glider", () -> {
            store.setEquipGlider(true);
            store.setEquipJetpack(false);
            store.setEquipSlide(false);
        });
        equipGear3 = new CustomGameMenu.customMenuButton("Equip Slide", () -> {
            store.setEquipSlide(true);
            store.setEquipJetpack(false);
            store.setEquipGlider(false);
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
    }
}

