package org.BobBuilders.FrenzyPenguins;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.almasb.fxgl.dsl.FXGLForKtKt.entityBuilder;
import static org.BobBuilders.FrenzyPenguins.EntityType.*;

public class CustomEntityFactory implements EntityFactory {

    //    @Spawns("rectangle")
//    public Entity newRectangle(SpawnData data) {
//        PhysicsComponent physics = new PhysicsComponent();
//        physics.setBodyType(BodyType.STATIC);
//        Rectangle view = new Rectangle(300, 80);
//        view.setFill(Color.BLUEVIOLET);
//        double rotation = data.get("rotation");
//        Entity entity = entityBuilder()
//                .from(data)
//                .type(GROUND)
//                .viewWithBBox(view)
//                .rotate(rotation)
//                .collidable()
//                .with(physics)
//                .build();
//        FixtureDef fix = new FixtureDef();
//        fix.setDensity(0.1f);
//        physics.setFixtureDef(fix);
//        return entity;
//    }
    @Spawns("rectangle")
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

    @Spawns("circle")
    public Entity createCircle(SpawnData data) {
        PhysicsComponent physics = new PhysicsComponent();
//        double radius = Double.parseDouble(data.get("radius").toString());
        double radius = 20;
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

    @Spawns("triangle")
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


    /*

        Left temporarily incase of reintroduction into code

     */
    @Spawns("ramp")
    public Entity newRamp(SpawnData data) {
        int horizontalRampLength = 300;
        int verticalRampLength = 400;
        int lowerRampLength = 50;
        int borderLength = 50;
        final int curveOffset = 50;

        PhysicsComponent physics = new PhysicsComponent();
        MoveTo moveTo = new MoveTo(0,
                100);
        Path path = new Path();
        LineTo horizontalLine = new LineTo(moveTo.getX() + horizontalRampLength, moveTo.getY());
        QuadCurveTo curveTo = new QuadCurveTo();
        curveTo.setControlX(horizontalLine.getX() + curveOffset);
        curveTo.setControlY(horizontalLine.getY());
        curveTo.setX(horizontalLine.getX() + curveOffset);
        curveTo.setY(horizontalLine.getY() + curveOffset);
        LineTo verticalLine = new LineTo(curveTo.getX(), curveTo.getY() + verticalRampLength);

        QuadCurveTo leftCurveTo = new QuadCurveTo();
        leftCurveTo.setControlX(verticalLine.getX());
        leftCurveTo.setControlY(verticalLine.getY() + curveOffset);
        leftCurveTo.setX(verticalLine.getX() + curveOffset);
        leftCurveTo.setY(verticalLine.getY() + curveOffset);

        LineTo lowerRampLine = new LineTo(leftCurveTo.getX() + lowerRampLength, leftCurveTo.getY());

        QuadCurveTo rightCurveTo = new QuadCurveTo();
        rightCurveTo.setControlX(lowerRampLine.getX() + curveOffset);
        rightCurveTo.setControlY(lowerRampLine.getY());
        rightCurveTo.setX(lowerRampLine.getX() + curveOffset);
        rightCurveTo.setY(lowerRampLine.getY() - curveOffset);

        LineTo rightBorder = new LineTo(rightCurveTo.getX(), rightCurveTo.getY() + curveOffset + borderLength);
        LineTo bottomBorder = new LineTo(rightBorder.getX() - horizontalRampLength - curveOffset * 3 - borderLength, rightBorder.getY());
        LineTo leftBorder = new LineTo(bottomBorder.getX(), bottomBorder.getY() - curveOffset * 2 - borderLength - verticalRampLength);

        path.getElements().addAll(moveTo, horizontalLine, curveTo, verticalLine, leftCurveTo,
                lowerRampLine, rightCurveTo, rightBorder, bottomBorder, leftBorder);
        path.setFill(Color.RED);
        path.setStroke(Color.RED);
        Point2D[] hitboxPoints = {
                new Point2D(0, 100),
                new Point2D(horizontalLine.getX(), horizontalLine.getY()),
                new Point2D(curveTo.getX(), curveTo.getY()),
                new Point2D(verticalLine.getX(), verticalLine.getY()),
                new Point2D(leftCurveTo.getX(), leftCurveTo.getY()),
                new Point2D(lowerRampLine.getX(), lowerRampLine.getY()),
                new Point2D(rightCurveTo.getX(), rightCurveTo.getY()),
                new Point2D(rightBorder.getX(), rightBorder.getY()),
                new Point2D(bottomBorder.getX(), bottomBorder.getY()),
                new Point2D(leftBorder.getX(), leftBorder.getY())
        };
        for (Point2D e : hitboxPoints) {
            int i = 0;
            System.out.println(i + ":" + e.getX() + "," + e.getY());
            i = i + 1;
        }
        Entity entity = entityBuilder()
                .type(GROUND)
                .bbox(new HitBox(BoundingShape.polygon(hitboxPoints)))
                .view(path)
                .collidable()
                .with(physics)
                .build();

//        final int offset = 300;
//
//        PhysicsComponent physics = new PhysicsComponent();
//        physics.setBodyType(BodyType.STATIC);
//        QuadCurve ramp = new QuadCurve();
//        ramp.setStartX(data.getX());
//        ramp.setStartY(data.getY());
//        ramp.setEndX(data.getX()+offset);
//        ramp.setEndY(data.getX()+offset);
//        ramp.setControlX(data.getX());
//        ramp.setControlY(data.getY()+offset);
//        ramp.setStroke(Color.BLACK);
//        ramp.setStrokeWidth(2);
//        Entity entity = entityBuilder()
//                .from(data)
//                .type(GROUND)
//                .view(ramp)
//                .collidable()
//                .with(physics)
//                .build();
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


    @Spawns("begin")
    public Entity newBegin(SpawnData data) {
        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.STATIC);
        Rectangle view = new Rectangle(200, 500);
        view.setFill(Color.BLUEVIOLET);
        Entity entity = entityBuilder()
                .from(data)
                .type(GROUND)
                .viewWithBBox(view)
                .collidable()
                .with(physics)
                .build();
        FixtureDef fix = new FixtureDef();
        fix.setDensity(0.1f);
        physics.setFixtureDef(fix);
        return entity;
    }

}
