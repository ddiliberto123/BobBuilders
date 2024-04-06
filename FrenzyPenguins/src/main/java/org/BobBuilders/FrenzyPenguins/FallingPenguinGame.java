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
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import org.BobBuilders.FrenzyPenguins.ui.CustomGameMenu;
import org.BobBuilders.FrenzyPenguins.ui.CustomMainMenu;
import static org.BobBuilders.FrenzyPenguins.Physics.*;
import static com.almasb.fxgl.dsl.FXGL.*;
import static com.almasb.fxgl.dsl.FXGLForKtKt.getAppHeight;
import static org.BobBuilders.FrenzyPenguins.EntityType.EXIT;



public class FallingPenguinGame extends GameApplication {
    private static Entity penguin;
    private Entity bottom;
    private Text distanceText;

    public static void main(String[] args) {
        launch(args);
    }

//    @Override
//    protected void initUI(){
//        Text textpixels = new Text();
//        textpixels.setTranslateX(50);
//        textpixels.setTranslateY(150);
//        FXGL.getGameScene().addUINode(textpixels);
//    }
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
            public FXGLMenu newGameMenu(){
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
        penguin = FXGL.spawn("penguin",10,4);

        //Creating the ramp
        FXGL.spawn("begin",0,100);
//        addRectangle(90,250,45);
//        addRectangle(200,350,25);
//        addRectangle(280,400,0);
//        addRectangle(480,380,-25);
        createRamp(250,200);

        PhysicsComponent floor = new PhysicsComponent();
        floor.setBodyType(BodyType.STATIC);

        bottom = FXGL.entityBuilder()
                .at(300, 650)
                .type(EXIT)
                .viewWithBBox(new Rectangle(2000000000, 20))
                .collidable()
                .with(floor)
                .buildAndAttach();

        //Displays the horizontal distance traveled by penguin
        distanceText = getUIFactoryService().newText("",Color.PURPLE,16);
        distanceText.setTranslateX(20);
        distanceText.setTranslateY(20);
        getGameScene().addUINode(distanceText);


        //Applies a gravitational force onto the penguin
        PhysicsComponent physics = penguin.getComponent(PhysicsComponent.class);
        Vec2 forceful = new Vec2(0, -9.8);
        physics.applyBodyForceToCenter(forceful);
        //FXGL.getPhysicsWorld().setGravity(0, 98);


    }

    protected void onUpdate(double tpf){
        //Constantly updates the x coordinates displayed in distanceText
        distanceText.setText("Position x: (" + penguin.getX() + ") Position y: (" + penguin.getY() + ") " +
                "Velocity x: (" + penguin_x_velocity() + ") Velocity y: (" + penguin_y_velocity() + ")" +
                "Angle: (" + penguin.getRotation() + ")");



        if (penguin.getX()>200) {
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
        }

        //Lift(penguin.getRotation());
//        if(penguin.getX() > 225) {
            PhysicsComponent fligth_physics = penguin.getComponent(PhysicsComponent.class);
            fligth_physics.applyBodyForceToCenter(Lift(penguin.getRotation()));
//
        if(penguin_y_velocity() == -120 || penguin_y_velocity() == 120){
            System.out.println("HERE: " + penguin.getX());
            //adFXGL.getGameWorld().reset();
        }
//        }
    }

    @Override
    protected void initPhysics(){
        //Collision handler for penguin and bottom
        FXGL.getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.PENGUIN, EXIT) {
            //Closes application once penguin hits bottom
            //This is temporary for us to build upon the surface which the penguin will eventually collide with
            @Override
            protected void onCollisionBegin(Entity penguin, Entity bottom){
                getGameController().exit();
            }
        });
    }

    @Override
    protected void initInput() {
        //Gives penguin the ability to move left and right in ramp vicinity
        onKey(KeyCode.RIGHT, () -> {
            if (penguin.getX() < 800) {
                PhysicsComponent physics = penguin.getComponent(PhysicsComponent.class);
                Vec2 object = new Vec2(1, 0);
                physics.applyBodyForceToCenter(object);
            }
        });
        onKey(KeyCode.LEFT, () -> {
            if (penguin.getX() < 800) {
                PhysicsComponent physics = penguin.getComponent(PhysicsComponent.class);
                Vec2 object = new Vec2(-1, 0);
                physics.applyBodyForceToCenter(object);
            }
        });

        //Gives penguin the ability to change angle which it faces
        onKey(KeyCode.D, ()->{
            PhysicsComponent physics = penguin.getComponent(PhysicsComponent.class);
            physics.setAngularVelocity(60);
        });
        onKey(KeyCode.A, ()->{
            PhysicsComponent physics = penguin.getComponent(PhysicsComponent.class);
            physics.setAngularVelocity(-60);
        });

    }

    private void addRectangle(double x, double y, double rotation){
        FXGL.spawn("rectangle",new SpawnData(x,y).put("rotation",rotation));
    }

    private void createRamp(double spawnX, double spawnY){
        FXGL.spawn("ramp", new SpawnData(spawnX,spawnY));
    }

    public static double penguin_y_postition(){
        return penguin.getY();
    }

//    public static void p_velocity() throws InterruptedException {
//        double p1 = penguin_x_postition();
//        FallingPenguinGame.class.wait(10);
//        double p2 = penguin_x_postition();
//        System.out.println("1: " + p1 + " 2: " + p2);
//    }


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
