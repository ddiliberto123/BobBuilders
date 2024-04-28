package org.BobBuilders.FrenzyPenguins;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.Effect;
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
import com.almasb.fxgl.ui.FXGLScrollPane;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.chart.PieChart;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.layout.Background;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
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
import java.util.List;

import static com.almasb.fxgl.dsl.FXGL.getGameScene;
import static com.almasb.fxgl.dsl.FXGLForKtKt.*;
import static org.BobBuilders.FrenzyPenguins.EntityType.*;

public class CustomEntityFactory implements EntityFactory {
    Store store = Store.getInstance();
    Image penguinImage = new Image("file:"+fix_for_Mac()+"penguin.png");
    Image penguinJ = new Image("file:"+fix_for_Mac()+"penguin_and_jetpack.png");
    Image penguinJactive = new Image("file:"+fix_for_Mac()+"jetpack_active.gif");
    Image penguinG = new Image("file:"+fix_for_Mac()+"penguin_and_glider.png");
    Image penguinS = new Image("file:"+fix_for_Mac()+"penguin_and_sled.png");
    Image penguinSGJ = new Image("file:"+fix_for_Mac()+"fully_equipped.png");
    Image penguinJG = new Image("file:"+fix_for_Mac()+"jetpack_and_umbrella.png");
    Image penguinJS = new Image("file:"+fix_for_Mac()+"jetpack_sled.png");
    Image penguinSG = new Image("file:"+fix_for_Mac()+"sled_and_glider.png");
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
        Image cloud1 = new Image("file:"+fix_for_Mac()+"cloud_1.png");
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
        Image cloud1 = new Image("file:"+fix_for_Mac()+"cloud_2.png");
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

        if (store.isEquipJetpack() && !store.isEquipGlider() && !store.isEquipSlide()) {
            penguinView = penguinJ;
        }
        if (store.isEquipGlider() && !store.isEquipJetpack() && !store.isEquipSlide()) {
            penguinView = penguinG;
        }
        if (store.isEquipSlide() && !store.isEquipJetpack() && !store.isEquipGlider()) {
            penguinView = penguinS;
        }
        if (store.isEquipJetpack() && store.isEquipGlider() && !store.isEquipSlide()) {
            penguinView = penguinJG;
        }
        if (store.isEquipJetpack() && !store.isEquipGlider() && store.isEquipSlide()) {
            penguinView = penguinJS;
        }
        if (store.isEquipJetpack() && store.isEquipGlider() && store.isEquipSlide()) {
            penguinView = penguinSGJ;
        }
        if (!store.isEquipJetpack() && store.isEquipGlider() && store.isEquipSlide()) {
            penguinView = penguinSG;
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
        ImageView snowflakeView = new ImageView("file:"+fix_for_Mac()+"snowflake.png");
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
        Image layer1 = new Image("file:"+fix_for_Mac()+"glacialmountains.png");
        Image layer2 = new Image("file:"+fix_for_Mac()+"glacialmountainslightened.png");

        ImageView imageView = new ImageView(layer1);
        imageView.setFitWidth(getAppWidth());
        imageView.setFitHeight(getAppHeight());

        StackPane stackPane = new StackPane(rectangle, imageView);

        return entityBuilder()
                .from(data)
                .viewWithBBox(stackPane)
                .build();
    }


    @Spawns("speedometer")
    public Entity speedometer(SpawnData data) {
        PhysicsComponent physics = new PhysicsComponent();
        double width = 150;
        double height = 5;
        Rectangle rectangle = new Rectangle(width, height);

        rectangle.setFill(Color.RED);
        rectangle.setOpacity(0.75);
        Entity entity = entityBuilder()
                .from(data)
                .type(GROUND)
                .viewWithBBox(rectangle)
                .rotationOrigin(150,5)
                .with(physics)
                .build();
        FixtureDef fix = new FixtureDef();
        fix.setDensity(0.1f);
        physics.setFixtureDef(fix);
        return entity;
    }
    @Spawns("speed_curve")
    public Entity speed_curve(SpawnData data) {
//        try {
//            width = Double.parseDouble(data.get(Constant.WIDTH).toString());
//            height = Double.parseDouble(data.get(Constant.HEIGHT).toString());
//        } catch (NumberFormatException | NullPointerException ex) {
//            System.out.println("Width or Height formatting not defined correctly, defaulting to 1");

        Arc arc = new Arc();
        arc.setCenterX(300.0f);
        arc.setCenterY(50.0f);
        arc.setRadiusX(150.0f);
        arc.setRadiusY(150.0f);
        arc.setStartAngle(-180.0f);
        arc.setLength(-90.0f);
        arc.setType(ArcType.ROUND);
        arc.setStroke(Color.GRAY);
        arc.setStrokeWidth(5);

        StackPane sp = new StackPane(arc);
        Rectangle indicator1 = new Rectangle(30,5);
        indicator1.setFill(Color.WHITESMOKE);
        Rectangle indicator2 = new Rectangle(30,5);
        indicator2.setFill(Color.WHITESMOKE);
        Rectangle indicator3 = new Rectangle(30,5);
        indicator3.setFill(Color.WHITESMOKE);
        sp.getChildren().add(indicator1);
        sp.getChildren().get(1).setTranslateY(70);
        sp.getChildren().get(1).setTranslateX(-58);
        sp.getChildren().add(indicator2);
        sp.getChildren().get(2).setTranslateY(-15);
        sp.getChildren().get(2).setTranslateX(-25);
        sp.getChildren().get(2).setRotate(40);
        sp.getChildren().add(indicator3);
        sp.getChildren().get(3).setTranslateY(-58);
        sp.getChildren().get(3).setTranslateX(69);
        sp.getChildren().get(3).setRotate(90);

//        Group g = new Group();
//        for(int i=0; i<90; i+=15){
//            System.out.println("hello");
//            Rectangle r = new Rectangle(20,5);
//            r.setFill(Color.HOTPINK);
//            g.getChildren().add(r);
//            System.out.println(g.getChildren().size());
//        }
//        int j=-20;
//        int z=0;
//        for (int i = 0; i < 90; i+=15) {
//            sp.getChildren().add(g.getChildren().get(z));
//            System.out.println(sp.getChildren().size());
//            sp.getChildren().get(z).setRotate(i);
//            sp.getChildren().get(z).setTranslateX(j);
//            j+=20;
//            z+=1;
//        }


        Entity entity = entityBuilder()
                .from(data)
                .type(GROUND)
                .viewWithBBox(sp)
                .build();
        FixtureDef fix = new FixtureDef();
        fix.setDensity(0.1f);
        return entity;
    }


    @Spawns("altimeter")
    public Entity altimeter(SpawnData data) {
        PhysicsComponent physics = new PhysicsComponent();
        double width = 70;
        double height = 5;
        Rectangle rectangle = new Rectangle(width, height);

        rectangle.setFill(Color.RED);
        rectangle.setOpacity(0.75);
        Entity entity = entityBuilder()
                .from(data)
                .type(GROUND)
                .viewWithBBox(rectangle)
                .rotationOrigin(70,0)
                .with(physics)
                .build();
        FixtureDef fix = new FixtureDef();
        fix.setDensity(0.1f);
        physics.setFixtureDef(fix);
        return entity;
    }

    @Spawns("altimeter_circle")
    public Entity altimeter_circle(SpawnData data) {
        PhysicsComponent physics = new PhysicsComponent();
        Circle altimeter = new Circle(75);
        altimeter.setOpacity(0.90);
        altimeter.setFill(Color.BLACK);
        altimeter.setStroke(Color.GRAY);
        altimeter.setStrokeWidth(5);
        StackPane sp = new StackPane(altimeter);
        Rectangle indicator1 = new Rectangle(30,5); Rectangle indicator2 = new Rectangle(30,5); Rectangle indicator3 = new Rectangle(30,5);Rectangle indicator4 = new Rectangle(30,5);
        indicator1.setFill(Color.WHITESMOKE);indicator2.setFill(Color.WHITESMOKE);indicator3.setFill(Color.WHITESMOKE);indicator4.setFill(Color.WHITESMOKE);
        indicator1.setOpacity(0.85);indicator2.setOpacity(0.85);indicator3.setOpacity(0.85);indicator4.setOpacity(0.85);
        sp.getChildren().addAll(indicator1,indicator2,indicator3,indicator4);

        //sets the horizontal indicators in place
        sp.getChildren().get(1).setTranslateX(-58);sp.getChildren().get(2).setTranslateX(58);
        //sets the vertical indicators in place
        sp.getChildren().get(3).setRotate(90);sp.getChildren().get(4).setRotate(90);
        sp.getChildren().get(3).setTranslateY(58);sp.getChildren().get(4).setTranslateY(-58);


        Entity entity = entityBuilder()
                .from(data)
                .type(GROUND)
                .viewWithBBox(sp)
                .rotationOrigin(75,5)
                .with(physics)
                .build();
        FixtureDef fix = new FixtureDef();
        fix.setDensity(0.1f);
        physics.setFixtureDef(fix);
        return entity;
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
