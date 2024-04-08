package org.BobBuilders.FrenzyPenguins;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.SceneFactory;
import com.almasb.fxgl.core.math.Vec2;
import com.almasb.fxgl.dsl.EntityBuilder;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.FXGLForKtKt;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import org.BobBuilders.FrenzyPenguins.ui.CustomGameMenu;
import org.BobBuilders.FrenzyPenguins.ui.CustomMainMenu;
import org.BobBuilders.FrenzyPenguins.util.EntitySpawner;

import static com.almasb.fxgl.dsl.FXGL.*;
import static com.almasb.fxgl.dsl.FXGLForKtKt.getAppHeight;
import static org.BobBuilders.FrenzyPenguins.EntityType.EXIT;
import static org.BobBuilders.FrenzyPenguins.EntityType.GROUND;


public class FallingPenguinGame extends GameApplication {
    private Entity penguin;
    private Entity bottom;
    private Text distanceText;
    private double beginPoints = 0;

    Inventory inventory = Inventory.getInstance();
    Store store = Store.getInstance();
    private double jetpackTimeElapsed = 0.0;


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    protected void initSettings(GameSettings gameSettings) {
        gameSettings.setWidth(1200);
        gameSettings.setHeight(800);

        gameSettings.setSceneFactory(new SceneFactory() {
            @Override
            public FXGLMenu newMainMenu() {
                return new CustomMainMenu();
            }

            @Override
            public FXGLMenu newGameMenu() {
                return new CustomGameMenu();
            }
        });
        gameSettings.setMainMenuEnabled(true);
        gameSettings.setTitle("Game");
        gameSettings.setVersion("1.0");

    }

    @Override
    protected void initGame() {
        FXGL.getGameWorld().addEntityFactory(new CustomEntityFactory());
        Rectangle rectangle = new Rectangle(getAppWidth(), getAppHeight(), Color.TRANSPARENT);
        Image back = new Image("file:clouds.png");
        ImageView background = new ImageView(back);

        //Spawn moving background
        for (int i = 0; i < 10; i++) {
            FXGL.spawn("background", i * getAppWidth(), 0);
        }

        //Spawning the penguin entity
        penguin = FXGL.spawn("penguin", 10, 0);

        /*

            Ramp creation is being redone, code left incase of errors etc.

         */
        //Creating the ramp
//        FXGL.spawn("begin", 0, 100);
//        addRectangle(90,250,45);
//        addRectangle(200,350,25);
//        addRectangle(280,400,0);
//        addRectangle(480,380,-25);
//        createRamp(250, 200);
        createEntireRamp(0, 150);

        PhysicsComponent floor = new PhysicsComponent();
        floor.setBodyType(BodyType.STATIC);

        bottom = FXGL.entityBuilder()
                .at(300, 10000)
                .type(EXIT)
                .viewWithBBox(new Rectangle(20000, 20))
                .collidable()
                .with(floor)
                .buildAndAttach();

        //Displays the horizontal distance traveled by penguin
        distanceText = getUIFactoryService().newText("", Color.PURPLE, 16);
        distanceText.setTranslateX(20);
        distanceText.setTranslateY(20);
        getGameScene().addUINode(distanceText);

        //Applies a gravitational force onto the penguin
        PhysicsComponent physics = penguin.getComponent(PhysicsComponent.class);
        Vec2 forceful = new Vec2(0, -9.8);
        physics.applyBodyForceToCenter(forceful);
    }

    protected void onUpdate(double tpf) {
        Inventory inventory = Inventory.getInstance();
        //Constantly updates the x coordinates displayed in distanceText
        distanceText.setText("Position: (" + penguin.getX() + ", " + penguin.getY() + ")");

        if (penguin.getY() > 400) {
            double penguinX = penguin.getX();
            double penguinY = penguin.getY();

            // Get the width and height  of the game window
            double windowWidth = getAppWidth();
            double windowHeight = getAppHeight();

            // Calculate the X and Y position for the camera so that the penguin remains in the center
            double cameraX = penguinX - windowWidth / 2;
            double cameraY = penguinY - windowHeight / 2;

            // Set the X and Y position of the viewport to keep the penguin centered
            getGameScene().getViewport().setX(cameraX);
            getGameScene().getViewport().setY(cameraY);
        } else {
            getGameScene().getViewport().setX(0);
            getGameScene().getViewport().setY(0);
        }

        //Restarts game when penguin reaches the bottom
        if (penguin.getY() > 4000) {
            // Update the points based on the distance traveled
            inventory.addPoints((int) penguin.getX());
            goToMenu();
        }

    }


    @Override
    protected void initPhysics() {
        //Collision handler for penguin and bottom
        FXGL.getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.PENGUIN, EXIT) {
            //Closes application once penguin hits bottom
            //This is temporary for us to build upon the surface which the penguin will eventually collide with
            @Override
            protected void onCollisionBegin(Entity penguin, Entity bottom) {
                getGameController().gotoMainMenu();
            }
        });
        FXGL.getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.PENGUIN, GROUND) {
            @Override
            protected void onCollision(Entity a, Entity b) {
                super.onCollision(a, b);
//                System.out.println("I AM COLLIDING WITH THE CURVE HELLO!");
            }
        });
    }

    @Override
    protected void initInput() {
        super.initInput();
        getInput().addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                event.consume();
            }
        });
        //Gives penguin the ability to move left and right in ramp vicinity
        onKey(KeyCode.RIGHT, () -> {
            if (penguin.getX() < 800) {
                PhysicsComponent physics = penguin.getComponent(PhysicsComponent.class);
                Vec2 object = new Vec2(3, 0);
                physics.applyBodyForceToCenter(object);
            }
        });
        onKey(KeyCode.LEFT, () -> {
            if (penguin.getX() < 800) {
                PhysicsComponent physics = penguin.getComponent(PhysicsComponent.class);
                Vec2 object = new Vec2(-3, 0);
                physics.applyBodyForceToCenter(object);
            }
        });

        //Gives penguin the ability to change angle which it faces
        onKey(KeyCode.D, () -> {
            PhysicsComponent physics = penguin.getComponent(PhysicsComponent.class);
            physics.setAngularVelocity(120);
        });
        onKey(KeyCode.A, () -> {
            PhysicsComponent physics = penguin.getComponent(PhysicsComponent.class);
            physics.setAngularVelocity(-120);
        });

        onKey(KeyCode.SPACE, () -> {
            if (store.isEquipJetpack()) {
                jetpackTimeElapsed += tpf();
                System.out.println(jetpackTimeElapsed);
                if (jetpackTimeElapsed < 5) {
                    PhysicsComponent physics = penguin.getComponent(PhysicsComponent.class);
                    double speedMultiplier = 2;

                    double angle = penguin.getRotation() % 360;
                    if (angle < 0) {
                        angle += 360;
                    }

                    double forceX = speedMultiplier * Math.cos(Math.toRadians(angle));
                    double forceY = -speedMultiplier * Math.sin(Math.toRadians(angle));

                    Vec2 vec = new Vec2(forceX, forceY);
                    physics.applyBodyForceToCenter(vec);
                }
            }
        });
    }

    @Override
    protected void initUI() {
    }

    private void addRectangle(double x, double y, double rotation) {
        FXGL.spawn("rectangle", new SpawnData(x, y).put("rotation", rotation));
    }

    private void createEntireRamp(double spawnX, double spawnY) {
        //Variables left for store implementation
        int horizontalRampLength = 250;
        int rampLength = 1000;
        int verticalRampLength = 400;
        int lowerRampLength = 200;
        int secondRampLength = 300;
        double rampRadius = 300;

        //Creates the initial ramp
        EntitySpawner.spawnRectangle(-300, 100, 500, 2000);
        EntitySpawner.spawnCircle(200 - 50, 100, 50);
        EntitySpawner.spawnRectangle(200, 150, 50, 1950);
        EntitySpawner.spawnCurve(250, 1000, rampRadius, 90, 180);
        EntitySpawner.spawnCurve(250, 1000, rampRadius, 50, 90);
        EntitySpawner.spawnRectangle(250, 1600, 2 * rampRadius, 400);
    }

    private void goToMenu() {
        // Show game menu
        getGameController().gotoGameMenu();
        penguin.removeFromWorld();
        penguin = FXGL.spawn("penguin", 10, 4);
        getGameScene().getViewport().setX(0);
        getGameScene().getViewport().setY(0);
    }

}
