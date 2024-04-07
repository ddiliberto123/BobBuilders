package org.BobBuilders.FrenzyPenguins;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.app.scene.SceneFactory;
import com.almasb.fxgl.core.math.Vec2;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import org.BobBuilders.FrenzyPenguins.ui.CustomGameMenu;
import org.BobBuilders.FrenzyPenguins.ui.CustomMainMenu;
import org.BobBuilders.FrenzyPenguins.util.Constant;
import org.BobBuilders.FrenzyPenguins.util.EntityNames;

import static com.almasb.fxgl.dsl.FXGL.*;
import static com.almasb.fxgl.dsl.FXGLForKtKt.getAppHeight;
import static org.BobBuilders.FrenzyPenguins.EntityType.EXIT;
import static org.BobBuilders.FrenzyPenguins.EntityType.GROUND;


public class FallingPenguinGame extends GameApplication {
    private Entity penguin;
    private Entity bottom;
    private Text distanceText;

    private static final String END_Y = "endy";

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
        FXGL.getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.PENGUIN,GROUND) {
            @Override
            protected void onCollision(Entity a, Entity b) {
                super.onCollision(a, b);
//                System.out.println("I AM COLLIDING WITH THE CURVE HELLO!");
            }
        });
    }

    @Override
    protected void initInput() {
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
        onKey(KeyCode.ESCAPE, () -> {

        });

    }

    @Override
    protected void initUI() {
    }

    private void addRectangle(double x, double y, double rotation) {
        FXGL.spawn("rectangle", new SpawnData(x, y).put("rotation", rotation));
    }

    private void createEntireRamp(double spawnX, double spawnY) {
        int horizontalRampLength = 250;
        int rampLength = 1000;
        int verticalRampLength = 400;
        int lowerRampLength = 200;
        int secondRampLength = 300;


//        FXGL.spawn("triangle",new SpawnData(0,0)
//                .put("endX",horizontalRampLength)
//                .put("endY",spawnY)
//                .put("controlX",0)
//                .put("controlY",spawnY));
//        FXGL.spawn("rectangle", new SpawnData(spawnX, spawnY)
//                .put("width", horizontalRampLength)
//                .put("height", 1000));
//        FXGL.spawn("triangle", new SpawnData(horizontalRampLength / 2, spawnY / 2)
//                .put("endX", horizontalRampLength / 2 + rampLength/2)
//                .put("endY", spawnY + rampLength)
//                .put("controlX", horizontalRampLength / 2)
//                .put("controlY", spawnY + rampLength));
//        FXGL.spawn("rectangle", new SpawnData(horizontalRampLength / 2 + rampLength/2, spawnY + rampLength)
//                .put("width", lowerRampLength)
//                .put("height", 1000));
//        FXGL.spawn("triangle", new SpawnData((horizontalRampLength + rampLength + lowerRampLength) / 2 - 70,
//                (spawnY + rampLength) / 2)
//                .put("endX", (horizontalRampLength + rampLength) / 2 + secondRampLength)
//                .put("endY", ((spawnY + rampLength)) / 2 - secondRampLength)
//                .put("controlX", (horizontalRampLength + rampLength) / 2 + secondRampLength)
//                .put("controlY", (spawnY + rampLength) / 2));
//        FXGL.spawn(EntityNames.RECTANGLE, new SpawnData(0,100)
//                .put(Constant.WIDTH,100)
//                .put(Constant.HEIGHT,1000));
//        FXGL.spawn(EntityNames.CURVE, new SpawnData(0,100)
//                .put(Constant.FROM_ANGLE,90)
//                .put(Constant.TO_ANGLE,180)
//                .put(Constant.END_X, 100)
//                .put(Constant.END_Y,200)
//                .put(Constant.CONTROL_X,0)
//                .put(Constant.CONTROL_Y,200));
        spawnRectangle(-300,100,500,2000);
        spawnCircle( 200-50, 100,50);
        spawnRectangle(200,150,50,1950);
//        spawnCurve(400,500,400,600,500,600,90,180);
        spawnCurve(250,500,250,600,500,600,90,180);
        spawnCurve(250,500,0,0,500,600,90,180);
//        spawnCurve(500,600,500,600,600,400,270,360);
    }

    private void goToMenu() {
        // Show game menu
        getGameController().gotoGameMenu();
        penguin.removeFromWorld();
        penguin = FXGL.spawn("penguin", 10, 4);
        getGameScene().getViewport().setX(0);
        getGameScene().getViewport().setY(0);
    }

    /**
     * Spawns a rectangle centered at the top left point
     * @param startX {@code double} top left x position
     * @param startY {@code double} top left y position
     * @param width {@code double} width of rectangle
     * @param height {@code double} height of rectangle
     */
    public void spawnRectangle(double startX,double startY,double width,double height){
        FXGL.spawn(EntityNames.RECTANGLE, new SpawnData(startX,startY)
                .put(Constant.WIDTH,width)
                .put(Constant.HEIGHT,height));
    }

    /**
     * Spawns a circle at a designated position centered around an x,y coordinate
     * @param startX {@code double} top left x position
     * @param startY {@code double} top left y position
     * @param radius {@code double} radius
     */
    public void spawnCircle(double startX,double startY,double radius){
        FXGL.spawn(EntityNames.CIRCLE, new SpawnData(startX,startY)
                .put(Constant.RADIUS,radius));
    }

    /**
     * Spawns a curve at a designated position and fills in the area around it to a control point.
     * @param startX {@code double} starting x position
     * @param startY {@code double} starting y position
     * @param controlX {@code double} control y position - used to encapsulate the area on either side of the curve
     * @param controlY {@code double} control y position - used to encapsulate the area on either side of the curve
     * @param endX {@code double} end x position
     * @param endY {@code double} end y position
     * @param fromAngle {@code double} angle where the curve begins - north is 0 degrees
     * @param toAngle {@code double} angle where the curve ends
     */
    public void spawnCurve(double startX, double startY, double controlX, double controlY, double endX, double endY, double fromAngle, double toAngle){
        if (startX == endX || startX == endY){
            System.out.println("You've just made a dot, not a curve!");
        }
        FXGL.spawn(EntityNames.CURVE, new SpawnData(startX,startY)
                .put(Constant.FROM_ANGLE,fromAngle)
                .put(Constant.TO_ANGLE,toAngle)
                .put(Constant.CONTROL_X,controlX)
                .put(Constant.CONTROL_Y,controlY)
                .put(Constant.END_X, endX)
                .put(Constant.END_Y,endY));
    }


}
