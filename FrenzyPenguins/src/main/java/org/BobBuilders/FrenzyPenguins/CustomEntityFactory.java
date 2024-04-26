package org.BobBuilders.FrenzyPenguins;

import com.almasb.fxgl.animation.Interpolators;
import com.almasb.fxgl.dsl.components.view.ChildViewComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
import com.almasb.fxgl.physics.box2d.dynamics.FixtureDef;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.util.Duration;
import org.BobBuilders.FrenzyPenguins.util.Constant;
import org.BobBuilders.FrenzyPenguins.util.EntitySpawner;
import  org.BobBuilders.FrenzyPenguins.FallingPenguinGame;

import java.sql.SQLOutput;
import java.util.ArrayList;

import static com.almasb.fxgl.dsl.FXGLForKtKt.*;
import static org.BobBuilders.FrenzyPenguins.EntityType.*;

public class CustomEntityFactory implements EntityFactory {
    Store store = Store.getInstance();
    Image penguinImage = new Image("file:"+fix_for_Mac()+"penguin.png");
    Image penguinJ = new Image("file:"+fix_for_Mac()+"penguin_and_jetpack.png");
    Image penguinJactive = new Image("file:"+fix_for_Mac()+"jetpack_active.gif");
    Image penguinG = new Image("file:"+fix_for_Mac()+"penguin_and_glider.png");
    Image penguinS = new Image("file:"+fix_for_Mac()+"penguin_and_sled.png");
    public static ImageView penguin;
    public static ImageView penguinJet;
    public static ImageView penguinJetActive;


    public ImageView getPenguin() {
        return penguin;
    }

    public void setPenguin(ImageView penguin) {
        this.penguin = penguin;
    }
    @Spawns(EntitySpawner.RECTANGLE)
    public Entity createRectangle(SpawnData data) {
        PhysicsComponent physics = new PhysicsComponent();
        double width = 1;
        double height = 1;
        try {
            width = Double.parseDouble(data.get(Constant.WIDTH).toString());
            height = Double.parseDouble(data.get(Constant.HEIGHT).toString());
        } catch (NumberFormatException | NullPointerException ex) {
            System.out.println("Width or Height formatting not defined correctly, defaulting to 1");
        }
        Rectangle rectangle = new Rectangle(width, height);
        rectangle.setFill(Color.WHITE);
        Entity entity = entityBuilder()
                .from(data)
                .type(GROUND)
                .viewWithBBox(rectangle)
                .with(physics)
                .build();
        FixtureDef fix = new FixtureDef();
        fix.setDensity(0.1f);
        physics.setFixtureDef(fix);
        return entity;
    }

    //Rectangle specifically designed with high friction to be placed along the track
    @Spawns("ground")
    public Entity createFloor(SpawnData data){
        PhysicsComponent physics = new PhysicsComponent();
        FixtureDef fix = new FixtureDef().density(0.1f).friction(10f);
        physics.setFixtureDef(fix);
        Rectangle rectangle = new Rectangle(150000, 500);
        rectangle.setFill(Color.WHITE);
        return entityBuilder()
                .from(data)
                .type(GROUND)
                .viewWithBBox(rectangle)
                .with(physics)
                .build();
    }
    @Spawns("water")
    public Entity createWater(SpawnData data){
        PhysicsComponent physics = new PhysicsComponent();
        FixtureDef fix = new FixtureDef().density(0.5f).friction(100);
        physics.setFixtureDef(fix);
        Rectangle rectangle = new Rectangle(100000, 500);
        rectangle.setFill(Color.MIDNIGHTBLUE);
        return entityBuilder()
                .from(data)
                .type(GROUND)
                .viewWithBBox(rectangle)
                .build();
    }

    @Spawns(EntitySpawner.CIRCLE)
    public Entity createCircle(SpawnData data) {
        PhysicsComponent physics = new PhysicsComponent();
//        double radius = Double.parseDouble(data.get("radius").toString());
        double radius = Double.parseDouble(data.get(Constant.RADIUS).toString());
        Circle circle = new Circle(radius);
        circle.setTranslateX(radius);
        circle.setTranslateY(radius);
        circle.setFill(Color.WHITE);
        Entity entity = entityBuilder()
                .from(data)
                .type(GROUND)
                .view(circle)
                .bbox(new HitBox(BoundingShape.circle(radius)))
                .with(physics)
                .collidable()
                .build();
        FixtureDef fix = new FixtureDef();
        fix.setDensity(0.1f);
        physics.setFixtureDef(fix);
        return entity;
    }

    @Spawns(EntitySpawner.TRIANGLE)
    public Entity createTriangle(SpawnData data) {
        PhysicsComponent physics = new PhysicsComponent();
        double endX = Double.parseDouble(data.get(Constant.END_X).toString());
        double endY = Double.parseDouble(data.get(Constant.END_Y).toString());
        double controlX = Double.parseDouble(data.get(Constant.CONTROL_X).toString());
        double controlY = Double.parseDouble(data.get(Constant.CONTROL_Y).toString());
        Polygon triangle = new Polygon();
        triangle.getPoints().addAll(
                data.getX(), data.getY(),
                controlX, controlY,
                endX, endY);
        Point2D[] hitboxPoints = {
                new Point2D(data.getX(), data.getY()),
                new Point2D(controlX, controlY),
                new Point2D(endX, endY)
        };
        Entity entity = entityBuilder()
                .from(data)
                .type(GROUND)
                .view(triangle)
                .bbox(new HitBox(BoundingShape.polygon(hitboxPoints)))
                .with(physics)
                .collidable()
                .build();
        FixtureDef fix = new FixtureDef();
        fix.setDensity(0.1f);
        physics.setFixtureDef(fix);
        return entity;
    }

    @Spawns(EntitySpawner.CURVE)
    public Entity newCurve(SpawnData data) {
        PhysicsComponent physics = new PhysicsComponent();
        CollidableComponent collidableComponent = new CollidableComponent(true);
        double fromDegree = Double.parseDouble(data.get(Constant.FROM_ANGLE).toString());
        double endDegree = Double.parseDouble(data.get(Constant.TO_ANGLE).toString());
        double radius = Double.parseDouble(data.get(Constant.RADIUS).toString());
        //Creates points along the circumfrance of a circle
        ArrayList<Point2D> points = new ArrayList<>();
        double computedDegree = fromDegree;
        while (computedDegree < endDegree) {
            double rad = Math.toRadians(computedDegree);
            points.add(new Point2D(Math.cos(rad) * radius + radius, Math.sin(rad) * radius + radius));
            computedDegree = Math.toDegrees(rad);
            computedDegree++;
        }
        //Depending on the quadrant, changes the control point (used to fill in the missing area)
        switch ((int) (fromDegree / 90)) {
            case 0 -> {
                //Quad 3 - 0 at N
                points.add(new Point2D(2 * radius, 2 * radius));
            }
            case 1 -> {
                //Quad 2 - 0 at N
                points.add(new Point2D(0, 2 * radius));
            }
        }
        //Makes a polygon
        Point2D[] allPoints = new Point2D[points.size()];
        allPoints = points.toArray(allPoints);
        Polygon poly = new Polygon();
        ArrayList<Double> polyPoints = new ArrayList<>();
        for (Point2D point : points) {
            polyPoints.add(point.getX());
            polyPoints.add(point.getY());
        }
        //Makes a polygon as the view surface with a chain hitbox
        poly.getPoints().addAll(polyPoints);
        poly.setStroke(Color.WHITE);
        poly.setFill(Color.WHITE);
        Entity entity = entityBuilder()
                .from(data)
                .type(GROUND)
                .view(poly)
                .bbox(new HitBox(BoundingShape.chain(allPoints)))
                .with(collidableComponent)
                .with(physics)
                .build();
        FixtureDef fix = new FixtureDef();
        fix.setDensity(0.1f);
        physics.setFixtureDef(fix);
        return entity;
    }
    @Spawns("cloud1")
    public static Entity newCloud1(SpawnData data){
        PhysicsComponent cloudPhysics = new PhysicsComponent();
        cloudPhysics.setBodyType(BodyType.KINEMATIC);
        Image cloud1 = new Image("file:cloud_1.png");
        ImageView view1 = new ImageView(cloud1);
        return entityBuilder()
                .from(data)
                .type(CLOUD)
                .view(view1)
                .with(cloudPhysics)
                .build();
    }
    @Spawns("cloud2")
    public static Entity newCloud2(SpawnData data){
        PhysicsComponent cloudPhysics = new PhysicsComponent();
        cloudPhysics.setBodyType(BodyType.KINEMATIC);
        Image cloud1 = new Image("file:cloud_2.png");
        ImageView view1 = new ImageView(cloud1);
        return entityBuilder()
                .from(data)
                .type(CLOUD)
                .view(view1)
                .with(cloudPhysics)
                .build();
    }

    @Spawns("penguin")
    public Entity newPenguin(SpawnData data) {
        float slideFriction = 0.055f-store.getSlideLevel()/200f;
        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.DYNAMIC);
        physics.setFixtureDef(new FixtureDef().density(0.1f).friction(0.055f));
        if (store.isEquipSlide()) {
            physics.setFixtureDef(new FixtureDef().density(0.1f).friction(slideFriction));
        }

        Image penguinView = penguinImage;

        if (store.isEquipJetpack() && !FallingPenguinGame.isSpaceKeyPressed()) {
            penguinView = penguinJ;
        }
        if (store.isEquipJetpack() && FallingPenguinGame.isSpaceKeyPressed()){
            penguinView = penguinJactive;
        }
        if (store.isEquipGlider()) {
            penguinView = penguinG;
        }
        if (store.isEquipSlide()) {
            penguinView = penguinS;
        }

        penguin = new ImageView(penguinView);
        penguinJet = new ImageView(penguinJ);
        penguinJetActive = new ImageView(penguinJactive);

        penguin.setFitHeight(125);
        penguin.setPreserveRatio(true);
        penguin.setTranslateX(-40);
        penguin.setTranslateY(-50);

        penguinJet.setFitHeight(125);
        penguinJet.setPreserveRatio(true);
        penguinJet.setTranslateX(-40);
        penguinJet.setTranslateY(-50);
        penguinJet.setVisible(false);

        penguinJetActive.setFitHeight(125);
        penguinJetActive.setPreserveRatio(true);
        penguinJetActive.setTranslateX(-40);
        penguinJetActive.setTranslateY(-50);
        penguinJetActive.setVisible(false);

        Rectangle view = new Rectangle(50, 25, Color.TRANSPARENT);

        return entityBuilder(data)
                .type(PENGUIN)
                .viewWithBBox(view)
                .view(penguin)
                .view(penguinJet)
                .view(penguinJetActive)
                .collidable()
                .with(physics)
                .build();
    }
    @Spawns("snowflake")
    public Entity newSnowflake(SpawnData data){
        PhysicsComponent snowflakePhysics = new PhysicsComponent();
        snowflakePhysics.setBodyType(BodyType.KINEMATIC);
        ImageView snowflakeView = new ImageView("file:snowflake.png");
        snowflakeView.setFitWidth(30);
        snowflakeView.setPreserveRatio(true);
        return entityBuilder()
                .from(data)
                .type(SNOWFLAKE)
                .view(snowflakeView)
                .build();
    }

    @Spawns(EntitySpawner.BACKGROUND)
    public Entity createBackground(SpawnData data) {
        // Create an entity with the image as background
        Rectangle rectangle = new Rectangle(getAppWidth(), getAppHeight(), Color.TRANSPARENT);
        Image image = new Image("file:"+fix_for_Mac()+"mountains.png");
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(getAppWidth());
        imageView.setFitHeight(getAppHeight());
        StackPane stackPane = new StackPane(rectangle, imageView);

        return entityBuilder()
                .from(data)
                .viewWithBBox(stackPane)
                .build();
    }

    public static String fix_for_Mac() {
        if ((System.getProperty("os.name").toLowerCase()).contains("mac")) {
            String home = System.getProperty("user.home");
            String mac_directory = String.format("%s%s", home, "/documents/BobBuilders/FrenzyPenguins/");
            return mac_directory;
        } else {
            return "";
        }
    }
}
