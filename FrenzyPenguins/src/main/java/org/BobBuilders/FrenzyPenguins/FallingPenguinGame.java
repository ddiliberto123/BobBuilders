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
import com.almasb.fxgl.physics.box2d.dynamics.FixtureDef;
import com.almasb.fxgl.ui.FXGLButton;
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
import static org.BobBuilders.FrenzyPenguins.Physics.Lift;
import static org.BobBuilders.FrenzyPenguins.Physics.penguin_velocity;


public class FallingPenguinGame extends GameApplication {
    private static Entity penguin;
    private Entity bottom;
    private Text distanceText;
    private double beginPoints = 0;

    Inventory inventory = Inventory.getInstance();
    Store store = Store.getInstance();
    private double jetpackTimeElapsed = 0.0;
    private double angle;


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
            FXGL.spawn(EntitySpawner.BACKGROUND, i * getAppWidth(), 0);
        }

        //Spawning the penguin entity
        penguin = FXGL.spawn("penguin", 10, 0);

        createEntireRamp(0, 150);

        PhysicsComponent floor = new PhysicsComponent();
        floor.setBodyType(BodyType.STATIC);

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
        PhysicsComponent physics = penguin.getComponent(PhysicsComponent.class);
        Inventory inventory = Inventory.getInstance();

        //Constantly updates the x coordinates displayed in distanceText
        distanceText.setText("Position: (" + penguin.getX() + ", " + penguin.getY() + ")");

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

        //Restarts game when penguin reaches the bottom
        if (penguin.getY() > 4000) {
            // Update the points based on the distance traveled
            inventory.addPoints((int) penguin.getX());
            goToMenu();
        }
        
        if(penguin.getX() < 230){
            physics.setVelocityX(40);
        }
        if(penguin.getX() == 250){
            Vec2 downtime = new Vec2(-10,-50);
            physics.applyBodyForceToCenter(downtime);
        }

        //Temporary until full floor is constructed
        if(penguin.getX() > 1000){
            if(!physics.isMoving()){
                inventory.addPoints((int) penguin.getX());
                goToMenu();
            }
        }

        angle = penguin.getRotation();
        angle = angle % 360;
        if (angle < 0) {
            angle += 360;
        }
        System.out.println(angle);
        System.out.println(penguin_velocity());

        if(angle>=45 && angle<=90){
            angle = angle -45;
        }
        if(angle >= 270 && angle <= 315){
            angle = angle +45;
        }
        if(angle >= 90 && angle <= 270){
            angle = -angle;
        }


        //Conditions for which lift force is applied
        if(penguin.getX() > 250 && store.isEquipGlider()) {
            physics.applyBodyForceToCenter(Lift(angle));
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

        //Gives penguin the ability to change angle which it faces
        onKey(KeyCode.D, () -> {
            if(penguin.getX() >= 1000) {
                PhysicsComponent physics = penguin.getComponent(PhysicsComponent.class);
                physics.setAngularVelocity(120);
            }
        });
        onKey(KeyCode.A, () -> {
            if(penguin.getX() >= 1000) {
                PhysicsComponent physics = penguin.getComponent(PhysicsComponent.class);
                physics.setAngularVelocity(-120);
            }
        });

        onKey(KeyCode.SPACE, () -> {
            if (store.isEquipJetpack() && penguin.getX() >= 1000) {
                jetpackTimeElapsed += tpf();
                System.out.println(jetpackTimeElapsed);
                //If statement limits the amount of time the user can use the jetpack for
                if (jetpackTimeElapsed < 5) {
                    PhysicsComponent physics = penguin.getComponent(PhysicsComponent.class);
                    double speedMultiplier = 2;
                    
//                    double angle = penguin.getRotation() % 360;
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

        //Creates floor for penguin to run into
//        FXGL.spawn("floor",1000,3000);
    }

    private void goToMenu() {
        // Show game menu
        getGameController().gotoGameMenu();
        penguin.removeFromWorld();
        penguin = FXGL.spawn("penguin", 10, 4);
        getGameScene().getViewport().setX(0);
        getGameScene().getViewport().setY(0);
    }
    public static double penguin_x_velocity(){
        PhysicsComponent physics = penguin.getComponent(PhysicsComponent.class);
        double vx = physics.getVelocityX();
        return Math.round(getPhysicsWorld().toMeters(vx));
    }
    public static double penguin_y_velocity(){
        PhysicsComponent physics = penguin.getComponent(PhysicsComponent.class);
        double vy = physics.getVelocityY();
        return Math.round(getPhysicsWorld().toMeters(vy));
    }

    public static double wing_area(){
        //This is temporary, the wing_area should be taken from the area of the gliders
        //System.out.println("width: " + penguin.getWidth()*penguin.getHeight());
        return (penguin.getWidth()*penguin.getHeight())/10;
    }
}


