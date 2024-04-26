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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import org.BobBuilders.FrenzyPenguins.ui.CustomGameMenu;
import org.BobBuilders.FrenzyPenguins.ui.CustomMainMenu;
import org.BobBuilders.FrenzyPenguins.util.Database;
import org.BobBuilders.FrenzyPenguins.util.EntitySpawner;
import static org.BobBuilders.FrenzyPenguins.CustomEntityFactory.fix_for_Mac;
import static org.BobBuilders.FrenzyPenguins.Physics.*;
import static com.almasb.fxgl.dsl.FXGL.*;
import static com.almasb.fxgl.dsl.FXGLForKtKt.getAppHeight;
import static org.BobBuilders.FrenzyPenguins.EntityType.EXIT;
import static org.BobBuilders.FrenzyPenguins.EntityType.GROUND;


public class FallingPenguinGame extends GameApplication {
    private static Entity penguin;
    private static Entity speedometer;
    private static Entity speed_curve;
    private static Entity altimeter;
    private static Entity altimeter_circle;
    private static Entity background_1st;
    private static Entity background_2nd;
    private static Entity background_3rd;
    private static Entity background2_1st;
    private static Entity background2_2nd;

    private Entity bottom;
    private Text distanceText;
    private Text speedText;
    private Text altituteText;
    private Rectangle cluster = new Rectangle(310,20);
    private double beginPoints = 0;
    private double angle;
    Inventory inventory = Inventory.getInstance();
    Store store = Store.getInstance();
    private double jetpackTimeElapsed = 0.0;

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
        Rectangle rectangle = new Rectangle(getAppWidth(), getAppHeight(), Color.TRANSPARENT);
        Image back = new Image("file:"+fix_for_Mac()+"clouds.png");
        ImageView background = new ImageView(back);

        //Spawn moving background
        for (int i = 0; i < 10; i++) {
            background_1st = FXGL.spawn(EntitySpawner.BACKGROUND, -9200, 2500);
            background_2nd = FXGL.spawn(EntitySpawner.BACKGROUND, -9200, 2500);
            background_3rd = FXGL.spawn(EntitySpawner.BACKGROUND, -9200, 2500);
        }
        //Spawning the penguin entity
        penguin = FXGL.spawn("penguin", 10, 0);
        speed_curve = FXGL.spawn("speed_curve",-200,-300);
        speedometer = FXGL.spawn("speedometer", -200,-200);
        altimeter_circle = FXGL.spawn("altimeter_circle", -200,-300);
        altimeter = FXGL.spawn("altimeter",-200,-900);

        createEntireRamp(0, 150);

        PhysicsComponent floor = new PhysicsComponent();
        floor.setBodyType(BodyType.STATIC);


        //Circle trail to help keep track of penguin position and movement
        for(int i =0; i < 100; i++){
            Entity circle = FXGL.entityBuilder()
                    .at(300,300*i)
                    .type(EXIT)
                    .viewWithBBox(new Circle(20,20,20,Color.GREEN))
                    .buildAndAttach();
        }

        //Displays the horizontal distance traveled by penguin
        distanceText = getUIFactoryService().newText("", Color.PURPLE, 16);
        distanceText.setTranslateX(20);
        distanceText.setTranslateY(20);
        getGameScene().addUINode(distanceText);

        //Adds rectangle for looks
        cluster.setFill(Color.LIGHTGRAY);
        cluster.setStroke(Color.GRAY);
        cluster.setStrokeWidth(3);
        cluster.setTranslateX(865);cluster.setTranslateY(185);
        getGameScene().addUINode(cluster);

        //Gives us speed accurately
        speedText = getUIFactoryService().newText("", Color.GREEN, 16);
        speedText.setTranslateX(1055);
        speedText.setTranslateY(200);
        speedText.setStroke(Color.LIGHTSEAGREEN);
        speedText.setStrokeWidth(1);
        getGameScene().addUINode(speedText);
        //Gives us altitude accurately
        altituteText = getUIFactoryService().newText("", Color.GREEN, 16);
        altituteText.setTranslateX(875);
        altituteText.setTranslateY(200);
        altituteText.setStroke(Color.LIGHTSEAGREEN);
        altituteText.setStrokeWidth(1);
        getGameScene().addUINode(altituteText);





        //Applies a gravitational force onto the penguin
        PhysicsComponent physics = penguin.getComponent(PhysicsComponent.class);
        Vec2 forceful = new Vec2(0, -9.8);
        physics.applyBodyForceToCenter(forceful);
    }


    protected void onUpdate(double tpf) {
        PhysicsComponent physics = penguin.getComponent(PhysicsComponent.class);
        Inventory inventory = Inventory.getInstance();

        //Constantly updates the x coordinates displayed in distanceText
        //Constantly updates the x coordinates displayed in distanceText
        distanceText.setText("Position x: (" + penguin.getX() + ") Position y: (" + penguin.getY() + ") " +
                "Velocity x: (" + penguin_x_velocity() + ") Velocity y: (" + penguin_y_velocity() + ")" +
                "Angle: (" + Math.round(get_penguin_angle()) + ")" +
                "FPS: (" + 1 / tpf() + ")");

        speedText.setVisible(false);altituteText.setVisible(false);cluster.setVisible(false);

        //background_1st.setY(penguin.getY());

        if (penguin.getX() >= 200) {
            double penguinX = penguin.getX();
            double penguinY = penguin.getY();
            speedometer.setX(penguinX+425);altimeter.setX(penguinX+260);
            speedometer.setY(penguinY-235);altimeter.setY(penguinY-297);
            speed_curve.setX(penguinX+425);altimeter_circle.setX(penguinX+250);
            speed_curve.setY(penguinY-380);altimeter_circle.setY(penguinY-375);
            speedText.setVisible(true);altituteText.setVisible(true);cluster.setVisible(true);

            // Get the width and height  of the game window
            double windowWidth = getAppWidth();
            double windowHeight = getAppHeight();

            // Calculate the X and Y position for the camera so that the penguin remains in the center
            double cameraX = penguinX - windowWidth / 2;
            double cameraY = penguinY - windowHeight / 2;

            // Set the X and Y position of the viewport to keep the penguin centered
            getGameScene().getViewport().setX(cameraX);
            getGameScene().getViewport().setY(cameraY);



            //At this configuration, the penguin is at the middle of the image
            //background_1st.setX(penguin.getX()-getAppWidth()/2);

            //If statement checks if the penguin is halfway done through the length of the image
//            System.out.println(penguin.getX()%getAppWidth()/2);
//            if(penguin.getX()%(getAppWidth()/2) < 10) {
//
//                //bring the sencond IDENTICAL background half a image width from the penguin
//                background_2nd.setX(penguin.getX()+getAppWidth()/2);
//                //background2_2nd.setX(50+penguin.getX()+getAppWidth()/2);
//                //GOOD UNTIL HERE
//                if(penguin.getX()%getAppWidth() < 15) {
//                    background_3rd.setX(penguin.getX());
//                    //background2_1st.setX(penguin.getX());
//                    background_2nd.setX(penguin.getX() - getAppWidth());
//                    background_1st.setX(penguin.getX() + getAppWidth());
//                    //background2_2nd.setX(penguin.getX() - getAppWidth());
//                }
//            }

        } else {
            getGameScene().getViewport().setX(0);
            getGameScene().getViewport().setY(0);
        }


        //Speedometer
        speedometer.rotateBy(((Math.sqrt((Math.pow(penguin_x_velocity(),2))+Math.pow(penguin_y_velocity(),2)))*90)/120);
        speedText.setText("speed:" + Math.round(penguin_velocity())+" km/h");
        //Altimeter
        altimeter.rotateBy(((altimeter_height()*360)/6000)+90);
        altituteText.setText("altitude:" + (-1*penguin.getY()+2974)/10+" m");


        //Restarts game when penguin reaches the bottom
        if (penguin.getY() >= 2970) {
            // Update the points based on the distance traveled
            inventory.addPoints((int) penguin.getX());
            //goToMenu();
            jetpackTimeElapsed = 0;
        }
        if(penguin.getX() >= 500 && penguin.getY() >= 2974){
            physics.applyBodyForceToCenter(B_mockup(get_penguin_angle()));
        }

        if (penguin.getX() < 230) {
            physics.setVelocityX(40);
        }
        if (penguin.getX() == 250) {
            Vec2 downtime = new Vec2(-10, -50);
            physics.applyBodyForceToCenter(downtime);
        }

        //Temporary until full floor is constructed
        if (penguin.getX() > 1000) {
            if (!physics.isMoving()) {
                inventory.addPoints((int) (penguin.getX()/100));
                goToMenu();
                jetpackTimeElapsed = 0;
                //Applies Drag without having a glider equiped
                physics.applyBodyForceToCenter(Drag(angle));
            }
        }

        angle = penguin.getRotation();
        angle = angle % 360;
        if (angle < 0) {
            angle += 360;
        }
        //System.out.println(angle);
        //System.out.println(penguin_velocity());
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
    }

        @Override
        protected void initPhysics () {
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
        FXGL.spawn("ground",1000,3000);
        FXGL.spawn("water",151000,3000);
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
    public static double penguin_velocity(){
        return (Math.sqrt(Math.pow(penguin_x_velocity(),2)+Math.pow(penguin_y_velocity(),2)));
    }
    public static double wing_area(){
        //This is temporary, the wing_area should be taken from the area of the gliders
        //System.out.println("width: " + penguin.getWidth()*penguin.getHeight());
        return (penguin.getWidth()*penguin.getHeight())/10;
    }
    public static double get_penguin_angle(){
        double angle = penguin.getRotation();
        if(angle > 360){
            angle = angle % 360;
        }
        return Math.round(angle);
    }
    //Get the area of the penguin
    public static double get_penguin_area(){
        double p_area = getPhysicsWorld().toMeters(penguin.getWidth()*penguin.getHeight());
        return p_area;
    }

    public static double altimeter_height(){
        return (-1*penguin.getY()+2974);
    }
}


