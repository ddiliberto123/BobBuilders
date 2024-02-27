package com.example.integrativeproject;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
import com.almasb.fxgl.physics.box2d.dynamics.FixtureDef;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.util.Map;

import static com.almasb.fxgl.dsl.FXGL.*;
import static com.almasb.fxgl.dsl.FXGLForKtKt.entityBuilder;
import static com.almasb.fxgl.dsl.FXGLForKtKt.getAppHeight;
import static com.almasb.fxgl.dsl.FXGLForKtKt.getGameWorld;
import static com.example.integrativeproject.EntityType.*;


public class FallingPenguinGame extends GameApplication {
    private Entity penguin;
    private Text distanceText;

    public static void main(String[] args) {
        launch(args);
    }
    @Override
    protected void initSettings(GameSettings gameSettings) {
        gameSettings.setWidth(800);
        gameSettings.setHeight(600);
        gameSettings.setTitle("Game");
        gameSettings.setVersion("1.0");
    }
    @Override
    protected void initGame() {

        FXGL.getGameWorld().addEntityFactory(new CustomEntityFactory());
//        FXGL.spawn("arc", 200, 100);
        penguin = FXGL.spawn("penguin",10,4);
        FXGL.spawn("ground",120,300);
        FXGL.spawn("begin",0,100);
        FXGL.spawn("track1",200,350);
        FXGL.spawn("track2", 280,400);
        FXGL.spawn("track3", 480,380);
//        FXGL.spawn("line", 400,300);
        distanceText = getUIFactoryService().newText("",Color.PURPLE,16);
        distanceText.setTranslateX(20);
        distanceText.setTranslateY(20);
        getGameScene().addUINode(distanceText);
        if(penguin.getY() == getAppHeight()){
            penguin.removeFromWorld();
            penguin = FXGL.spawn("penguin",10,4);
        }
    }
    protected void onUpdate(double tpf){
        distanceText.setText("Position: (" + penguin.getX() + ")");
    }

    @Override
    protected void initInput() {
        getInput().addAction(new UserAction("Spawn Block"){
             @Override
             protected void onActionBegin() {
                 spawn("block", getInput().getMouseXWorld(),getInput().getMouseYWorld());
             }
         }, MouseButton.PRIMARY);
        onKey(KeyCode.RIGHT, ()->{
            PhysicsComponent physics = penguin.getComponent(PhysicsComponent.class);
            physics.setVelocityX(100);
        });
        onKey(KeyCode.LEFT, ()->{
            PhysicsComponent physics = penguin.getComponent(PhysicsComponent.class);
            physics.setVelocityX(-100);
        });
    }

    @Override
    protected void initPhysics() {
        FXGL.getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.PENGUIN, EntityType.ARC) {
            @Override
            protected void onCollisionBegin(Entity block, Entity arc) {
                // Add custom logic here for what happens when a block collides with the arc
                System.out.println("Block collided with the arc!");
            }
        });
    }
}