package com.example.integrativeproject;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.ExpireCleanComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.entity.components.TypeComponent;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
import com.almasb.fxgl.physics.box2d.dynamics.FixtureDef;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGLForKtKt.entityBuilder;
import static com.example.integrativeproject.EntityType.*;

public class CustomEntityFactory implements EntityFactory {

    @Spawns("rectangle")
    public Entity newRectangle(SpawnData data){
        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.STATIC);
        Rectangle view = new Rectangle(300,80);
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
    @Spawns("penguin")
    public Entity newPenguin(SpawnData data){
        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.DYNAMIC);
        physics.setFixtureDef(new FixtureDef().density(0.1f));
        Rectangle view = new Rectangle(30,30,Color.BLACK);
        return entityBuilder(data)
                .type(PENGUIN)
                .viewWithBBox(view)
                .collidable()
                .with(physics)
                .build();
    }


    @Spawns("begin")
    public Entity newBegin(SpawnData data){
        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.STATIC);
        Rectangle view = new Rectangle(200,500);
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
