package org.BobBuilders.FrenzyPenguins;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
import com.almasb.fxgl.physics.box2d.dynamics.FixtureDef;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;

import static com.almasb.fxgl.dsl.FXGLForKtKt.entityBuilder;
import static org.BobBuilders.FrenzyPenguins.EntityType.*;

public class CustomEntityFactory implements EntityFactory {

    @Spawns("rectangle")
    public Entity newRectangle(SpawnData data) {
        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.STATIC);
        Rectangle view = new Rectangle(300, 80);
        view.setFill(Color.BLUEVIOLET);
        double rotation = data.get("rotation");
        Entity entity = entityBuilder()
                .from(data)
                .type(GROUND)
                .viewWithBBox(view)
                .rotate(rotation)
                .collidable()
                .with(physics)
                .build();
        FixtureDef fix = new FixtureDef();
        fix.setDensity(0.1f);
        physics.setFixtureDef(fix);
        return entity;
    }

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
        Entity entity = entityBuilder()
                .type(GROUND)
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
