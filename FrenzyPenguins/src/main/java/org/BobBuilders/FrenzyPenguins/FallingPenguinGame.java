package org.BobBuilders.FrenzyPenguins;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.SceneFactory;
import com.almasb.fxgl.core.math.Vec2;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
import com.almasb.fxgl.physics.box2d.dynamics.FixtureDef;
import com.almasb.fxgl.ui.FXGLButton;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import org.BobBuilders.FrenzyPenguins.ui.CustomGameMenu;
import org.BobBuilders.FrenzyPenguins.ui.CustomMainMenu;
import org.BobBuilders.FrenzyPenguins.util.Database;
import org.BobBuilders.FrenzyPenguins.util.EntitySpawner;

import java.util.Random;

import static org.BobBuilders.FrenzyPenguins.CustomEntityFactory.fix_for_Mac;
import static org.BobBuilders.FrenzyPenguins.Physics.*;
import static com.almasb.fxgl.dsl.FXGL.*;
import static com.almasb.fxgl.dsl.FXGLForKtKt.getAppHeight;
import static org.BobBuilders.FrenzyPenguins.EntityType.EXIT;
import static org.BobBuilders.FrenzyPenguins.EntityType.GROUND;
import static org.BobBuilders.FrenzyPenguins.Physics.Lift;
import static org.BobBuilders.FrenzyPenguins.Physics.penguin_velocity;


public class FallingPenguinGame extends GameApplication {
    private static Entity penguin;
    private Text distanceText;
    private double beginPoints = 0;
    private double angle;
    Inventory inventory = Inventory.getInstance();
    Store store = Store.getInstance();
    private double jetpackTimeElapsed = 0.0;
    private double cloud1SpawnTimer = 0;
    private double cloud2SpawnTimer = 0;
    private double cloud1SpawnInterval = 0.5;
    private double cloud2SpawnInterval = 0.5;
    private boolean beginAnimation = false;
    private static boolean spaceKeyPressed = false;
    private Text welcomeText;

    public static void main(String[] args) {
        Database.dbInit();
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

        welcomeText = new Text("Instructions:\nChange angle: A and D\nUse jetpack: Space key\nBegin: Right key\nAnd don't forget to have fun!");
        welcomeText.setFill(Color.WHITE);
        welcomeText.setTranslateX(350);
        welcomeText.setTranslateY(100);
        welcomeText.setFont(Font.font(25));
        getGameScene().addUINode(welcomeText);

        //Spawn moving background
        for (int i = 0; i < 20; i++) {
            FXGL.spawn(EntitySpawner.BACKGROUND, i * getAppWidth(), 2300);
        }

        //Spawning the penguin entity
        penguin = FXGL.spawn("penguin", 10, 0, 0);

        //Random clouds placed at the beginning of the game
        spawnCloud1();
        spawnCloud2();
        spawnCloud1();

        createEntireRamp(0, 150);

        //Displays the horizontal distance traveled by penguin
        distanceText = getUIFactoryService().newText("", Color.PURPLE, 16);
        distanceText.setTranslateX(20);
        distanceText.setTranslateY(20);
        getGameScene().addUINode(distanceText);

        //Applies a gravitational force onto the penguin
        PhysicsComponent physics = penguin.getComponent(PhysicsComponent.class);
        Vec2 forceful = new Vec2(0, -9.8);
        physics.applyBodyForceToCenter(forceful);

        //Displays control functions t the beginning of each game
        Text instructions = new Text("To change penguin angle, use keys A and D\nTo use jetpack, hold space key\n To start the game press the right key\nHave fun");
        instructions.setFill(Color.WHITE);
        instructions.setTranslateX(600);
        instructions.setTranslateY(200);
    }


    protected void onUpdate(double tpf) {
        PhysicsComponent physics = penguin.getComponent(PhysicsComponent.class);
        Inventory inventory = Inventory.getInstance();

        super.onUpdate(tpf);

        if (penguin.getX() != 10) {
            welcomeText.setVisible(false);
        }

        // Define the transition heights where the color will start to change
        double transitionHeight1 = -500;
        double transitionHeight2 = 800; // Increase the distance between transitionHeight1 and transitionHeight2

        // Define the colors
        Color lightBlue = Color.DARKBLUE;
        Color midBlue = Color.DODGERBLUE;
        Color darkBlue = Color.DEEPSKYBLUE;

        // Get the root of the game scene
        Pane root = (Pane) getGameScene().getRoot();

        // Calculate the relative height of the penguin
        double relativeHeight = penguin.getY() / getAppHeight();

        // Define transition factors to control the interpolation
        double transitionFactor1 = (relativeHeight - transitionHeight1 / getAppHeight()) / (transitionHeight2 / getAppHeight() - transitionHeight1 / getAppHeight());
        double transitionFactor2 = (relativeHeight - transitionHeight2 / getAppHeight()) / (1 - transitionHeight2 / getAppHeight());

        // Interpolate between the colors based on relative height
        Color backgroundColor;
        if (relativeHeight < transitionHeight1 / getAppHeight()) {
            backgroundColor = lightBlue;
        } else if (relativeHeight < transitionHeight2 / getAppHeight()) {
            // Interpolate between lightBlue and midBlue
            backgroundColor = lightBlue.interpolate(midBlue, transitionFactor1);
        } else {
            // Interpolate between midBlue and darkBlue
            backgroundColor = midBlue.interpolate(darkBlue, transitionFactor2);
        }

        // Set the background color
        root.setBackground(new Background(new BackgroundFill(backgroundColor, null, null)));


        if (beginAnimation) {
            if (penguin.getX() < 230) {
                physics.setVelocityX(40);
            }
            if (penguin.getX() == 250) {
                Vec2 downtime = new Vec2(-10, -50);
                physics.applyBodyForceToCenter(downtime);
            }
        }


        //Constantly updates the x coordinates displayed in distanceText
        //Constantly updates the x coordinates displayed in distanceText
        distanceText.setText("Position x: (" + penguin.getX() + ") Position y: (" + penguin.getY() + ") " +
                "Velocity x: (" + penguin_x_velocity() + ") Velocity y: (" + penguin_y_velocity() + ")" +
                "Angle: (" + Math.round(get_penguin_angle()) + ")" +
                "FPS: (" + 1 / tpf() + ")");

        if (penguin.getX() >= 200) {
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

        if (penguin.getX() >= 151000) {
            physics.applyBodyForceToCenter(B_mockup(get_penguin_angle()));
        }


        //Temporary until full floor is constructed
        if (!physics.isMoving() && beginAnimation) {
            inventory.addPoints((int) (penguin.getX()));
            goToMenu();
            beginAnimation = false;
            jetpackTimeElapsed = 0;
            //Applies Drag without having a glider equiped
            physics.applyBodyForceToCenter(Drag(angle));
        }

        angle = penguin.getRotation();
        angle = angle % 360;
        if (angle < 0) {
            angle += 360;
        }
        if (angle >= 45 && angle <= 90) {
            angle = angle - 45;
        }
        if (angle >= 270 && angle <= 315) {
            angle = angle + 45;
        }
        if (angle >= 90 && angle <= 270) {
            angle = -angle;
        }

        //Conditions for which lift+drag force is applied
        if (penguin.getX() > 250 && store.isEquipGlider()) {
            physics.applyBodyForceToCenter(Flight_vectors(angle));
        }
        //Locks angle when player isn't pressing key
        if (penguin.getX() > 1000 && (physics.getBody().getAngularVelocity() >= 1 || physics.getBody().getAngularVelocity() <= -1)) {
            physics.setAngularVelocity(0);
        }

        //Spawns clouds once animation is complete
        if (penguin.getX() > 1000) {
            super.onUpdate(tpf);
            Random random = new Random();
            //Keeps track of time since last cloud spawned
            cloud1SpawnTimer += tpf;
            if (cloud1SpawnTimer >= cloud1SpawnInterval) {
                //Spawns cloud 1 once time interval is complete
                spawnCloud1();
                cloud1SpawnInterval = random.nextDouble();
                System.out.println(cloud1SpawnInterval);
                //Resets time interval between 0 and 0.99 seconds
                cloud1SpawnTimer = 0;
            }
            cloud2SpawnTimer += tpf;
            if (cloud2SpawnTimer >= cloud2SpawnInterval) {
                //Spawns cloud 2 once time interval is complete
                spawnCloud2();
                cloud2SpawnInterval = random.nextDouble();
                //Resets time interval between 0 and 0.99 seconds
                cloud2SpawnTimer = 0;
            }
        }
        System.out.println(spaceKeyPressed);

    }

    @Override
    protected void initInput() {
        super.initInput();
        getInput().addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                event.consume();
            }
        });

        getInput().addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.RIGHT) {
                beginAnimation = true;
            }
        });

        getInput().addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.SPACE) {
                if (store.isEquipJetpack() && penguin.getX() >= 1000) {
                    spaceKeyPressed = true;  // Set the flag to true when space key is released
                    CustomEntityFactory.penguinJet.setVisible(false);
                    CustomEntityFactory.penguinJetActive.setVisible(true);
                }
            }
        });

        getInput().addEventHandler(KeyEvent.KEY_RELEASED, event -> {
            if (event.getCode() == KeyCode.SPACE) {
                if (store.isEquipJetpack() && penguin.getX() >= 1000) {
                    spaceKeyPressed = false;  // Set the flag to false when space key is released
                    CustomEntityFactory.penguinJet.setVisible(true);
                    CustomEntityFactory.penguinJetActive.setVisible(false);
                }
            }
        });

        //Gives penguin the ability to change angle which it faces
        onKey(KeyCode.D, () -> {
            if (penguin.getX() >= 1000) {
                PhysicsComponent physics = penguin.getComponent(PhysicsComponent.class);
                physics.setAngularVelocity(120);
            }
        });
        onKey(KeyCode.A, () -> {
            if (penguin.getX() >= 1000) {
                PhysicsComponent physics = penguin.getComponent(PhysicsComponent.class);
                physics.setAngularVelocity(-120);
            }
        });
        onKey(KeyCode.SPACE, () -> {
            if (store.isEquipJetpack() && penguin.getX() >= 1000) {
                jetpackTimeElapsed += tpf();
                //If statement limits the amount of time the user can use the jetpack for
                if (jetpackTimeElapsed < 5) {
                    PhysicsComponent physics = penguin.getComponent(PhysicsComponent.class);
                    double speedMultiplier = 2;

                    double jetAngle = penguin.getRotation() % 360;
                    if (jetAngle < 0) {
                        jetAngle += 360;
                    }

                    double forceX = speedMultiplier * Math.cos(Math.toRadians(jetAngle));
                    double forceY = -speedMultiplier * Math.sin(Math.toRadians(jetAngle));

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
        //Buffer so that there is overlay
        double buffer = 1;

        //Creates the initial ramp
        EntitySpawner.spawnRectangle(-500, 100, 700, 2000);
        EntitySpawner.spawnCircle(200 - 50, 100, 50);
        EntitySpawner.spawnRectangle(200 - buffer, 150 - buffer, 50 + buffer, 1950 + buffer);
        EntitySpawner.spawnCurve(250, 1000, rampRadius, 90, 180);
        EntitySpawner.spawnCurve(250, 1000, rampRadius, 50, 90);
        EntitySpawner.spawnRectangle(250, 1600 - buffer, 2 * rampRadius, 400 + buffer);

        //Creates floor for penguin to run into
        FXGL.spawn("ground", 1000, 3000);
        FXGL.spawn("water", 151000, 3000);
    }

    private void goToMenu() {
        // Show game menu
        getGameController().gotoGameMenu();
        penguin.removeFromWorld();
        penguin = FXGL.spawn("penguin", 10, 4);
        getGameScene().getViewport().setX(0);
        getGameScene().getViewport().setY(0);
        jetpackTimeElapsed = 0;
    }

    public static double penguin_x_velocity() {
        PhysicsComponent physics = penguin.getComponent(PhysicsComponent.class);
        double vx = physics.getVelocityX();
        return Math.round(getPhysicsWorld().toMeters(vx));
    }

    public static double penguin_y_velocity() {
        PhysicsComponent physics = penguin.getComponent(PhysicsComponent.class);
        double vy = physics.getVelocityY();
        return Math.round(getPhysicsWorld().toMeters(vy));
    }

    public static double wing_area() {
        //This is temporary, the wing_area should be taken from the area of the gliders
        //System.out.println("width: " + penguin.getWidth()*penguin.getHeight());
        return (penguin.getWidth() * penguin.getHeight()) / 10;
    }

    public static double get_penguin_angle() {
        double angle = penguin.getRotation();
        if (angle > 360) {
            angle = angle % 360;
        }
        return Math.round(angle);
    }

    //Get the area of the penguin
    public static double get_penguin_area() {
        double p_area = getPhysicsWorld().toMeters(penguin.getWidth() * penguin.getHeight());
        return p_area;
    }

    //Method to spawn cloud 1
    private void spawnCloud1() {
        //Ensures that clouds do not spawn once the penguin approaches bottom
        //This ensures that clouds will not spawn over the floor
        if (penguin.getY() < 2900 - getAppHeight()) {
            Random random = new Random();
            //Generates a random number within the app height
            double randomY = random.nextDouble() * getAppHeight();
            //Spawns a cloud slightly out of frame within the penguin's y coordinates
            Entity cloud = FXGL.spawn("cloud1", penguin.getX() + getAppWidth() / 2, penguin.getY() + randomY - 300);
            if (penguin.getX() >= cloud.getX()) {
                cloud.removeFromWorld();
            }
            PhysicsComponent physics = cloud.getComponent(PhysicsComponent.class);
            physics.setVelocityX(-10);
        }
    }

    //Method to spawn cloud 2
    private void spawnCloud2() {
        if (penguin.getY() < 2900 - getAppHeight()) {
            Random random = new Random();
            //Generates a random number within the app height
            double randomY = random.nextDouble() * getAppHeight();
            //Spawns a cloud slightly out of frame within the penguin's y coordinates
            Entity cloud = FXGL.spawn("cloud2", penguin.getX() + getAppWidth() / 2, penguin.getY() + randomY);
            if (penguin.getX() >= cloud.getX()) {
                cloud.removeFromWorld();
            }
            PhysicsComponent physics = cloud.getComponent(PhysicsComponent.class);
            physics.setVelocityX(-10);
        }
    }

    public static boolean isSpaceKeyPressed() {
        return spaceKeyPressed;
    }
}


