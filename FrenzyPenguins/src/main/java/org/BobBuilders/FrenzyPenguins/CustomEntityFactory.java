package org.BobBuilders.FrenzyPenguins;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
import com.almasb.fxgl.physics.box2d.dynamics.Fixture;
import com.almasb.fxgl.physics.box2d.dynamics.FixtureDef;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import org.BobBuilders.FrenzyPenguins.util.Constant;
import org.BobBuilders.FrenzyPenguins.util.EntityNames;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.almasb.fxgl.dsl.FXGLForKtKt.entityBuilder;
import static org.BobBuilders.FrenzyPenguins.EntityType.*;

public class CustomEntityFactory implements EntityFactory {

    @Spawns(EntityNames.RECTANGLE)
    public Entity createRectangle(SpawnData data) {
        PhysicsComponent physics = new PhysicsComponent();
        double width = Double.parseDouble(data.get(Constant.WIDTH).toString());
        double height = Double.parseDouble(data.get(Constant.HEIGHT).toString());
        Rectangle rectangle = new Rectangle(width, height);
        rectangle.setFill(Color.BLACK);
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

    @Spawns(EntityNames.CIRCLE)
    public Entity createCircle(SpawnData data) {
        PhysicsComponent physics = new PhysicsComponent();
//        double radius = Double.parseDouble(data.get("radius").toString());
        double radius = Double.parseDouble(data.get(Constant.RADIUS).toString());
        Circle circle = new Circle(radius);
        circle.setTranslateX(radius);
        circle.setTranslateY(radius);
        circle.setFill(Color.BLACK);
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

    @Spawns(EntityNames.TRIANGLE)
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

    @Spawns(EntityNames.CURVE)
    public Entity newCurve(SpawnData data) {
        PhysicsComponent physics = new PhysicsComponent();
        CollidableComponent collidableComponent = new CollidableComponent(true);
        double startX = data.getX();
        double startY = data.getY();
        double endX = Double.parseDouble(data.get(Constant.END_X).toString());
        double endY = Double.parseDouble(data.get(Constant.END_Y).toString());
        double degree = Double.parseDouble(data.get(Constant.FROM_ANGLE).toString());
        double endDegree = Double.parseDouble(data.get(Constant.TO_ANGLE).toString());

        QuadCurve curve = new QuadCurve(
                startX,
                startY,
                startX,
                endY,
                endX,
                endY
        );
        System.out.println(curve.getEndX());
        double radius = endX-startX;
        ArrayList<Point2D> points = new ArrayList<>();
        while (degree < endDegree){
            double rad = Math.toRadians(degree);
            System.out.println(radius);
            System.out.println();
            points.add(new Point2D(Math.cos(rad)*radius+startX,Math.sin(rad)*radius+endY));
            degree = Math.toDegrees(rad);
            degree++;
        }
        points.add(new Point2D(startX-radius,endY+radius));
        Point2D[] allPoints = new Point2D[points.size()];
        allPoints = points.toArray(allPoints);
        for(Point2D e : allPoints)
            System.out.println(e);
        /**
         * polygon test
         */
        Polygon poly = new Polygon();
        ArrayList<Double> polyPoints = new ArrayList<>();
        for (int i = 0; i < points.size(); i++){
            polyPoints.add(points.get(i).getX());
            polyPoints.add(points.get(i).getY());
        }
        poly.getPoints().addAll(polyPoints);
//        entityBuilder()
//                .from(data)
//                .type(GROUND)
//                .view(poly)
//                .buildAndAttach();
//        curve.setStroke(Color.BLACK);
//        curve.setFill(null);
//        curve.setStrokeWidth(4);
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

    @Spawns("penguin")
    public Entity newPenguin(SpawnData data) {
        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.DYNAMIC);
        physics.setFixtureDef(new FixtureDef().density(0.1f));
        Rectangle view = new Rectangle(30, 30, Color.BLACK);
        return entityBuilder(data)
                .type(PENGUIN)
                .viewWithBBox(view)
                .collidable()
                .with(physics)
                .build();
    }

}
