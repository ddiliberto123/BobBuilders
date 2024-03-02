package com.example.integrativeproject;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.core.math.Vec2;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
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
        gameSettings.setWidth(1200);
        gameSettings.setHeight(800);
        gameSettings.setMainMenuEnabled(true);
        gameSettings.setTitle("Game");
        gameSettings.setVersion("1.0");
    }
    @Override
    protected void initGame() {

        FXGL.getGameWorld().addEntityFactory(new CustomEntityFactory());
        penguin = FXGL.spawn("penguin",10,4);

        FXGL.spawn("begin",0,100);

        addRectangle(90,250,45);
        addRectangle(200,350,25);
        addRectangle(280,400,0);
        addRectangle(480,380,-25);

        distanceText = getUIFactoryService().newText("",Color.PURPLE,16);
        distanceText.setTranslateX(20);
        distanceText.setTranslateY(20);
        getGameScene().addUINode(distanceText);
        PhysicsComponent physics = penguin.getComponent(PhysicsComponent.class);
        Vec2 forceful = new Vec2(0, -1.8);
        physics.applyBodyForceToCenter(forceful);

    }
    protected void onUpdate(double tpf){
        distanceText.setText("Position: (" + penguin.getX() + ")");
        if(penguin.getX()>700) {
            double penguinX = penguin.getX();

            // Get the width of the game window
            double windowWidth = getAppWidth();

            // Calculate the X position for the camera so that the penguin remains in the center
            double cameraX = penguinX - windowWidth / 2;

            // Set the X position of the viewport to keep the penguin centered
            getGameScene().getViewport().setX(cameraX);
        }
    }

    @Override
    protected void initInput() {

        onKey(KeyCode.RIGHT, ()->{
            if(penguin.getX() < 800) {
                PhysicsComponent physics = penguin.getComponent(PhysicsComponent.class);
                Vec2 forceful = new Vec2(0, -1.8);
                Vec2 object = new Vec2(3, 0);
                physics.applyBodyForceToCenter(object);
                physics.applyBodyForceToCenter(forceful);
            }
        });
        onKey(KeyCode.LEFT, ()->{
            if(penguin.getX() < 800) {
                PhysicsComponent physics = penguin.getComponent(PhysicsComponent.class);
                Vec2 forceful = new Vec2(0, -1.8);
                Vec2 object = new Vec2(-3, 0);
                physics.applyBodyForceToCenter(object);

            }
        });
        onKey(KeyCode.D, ()->{
            PhysicsComponent physics = penguin.getComponent(PhysicsComponent.class);
            physics.setAngularVelocity(120);
        });
        onKey(KeyCode.A, ()->{
            PhysicsComponent physics = penguin.getComponent(PhysicsComponent.class);
            physics.setAngularVelocity(-120);
        });
    }

    private void addRectangle(double x, double y, double rotation){
        Entity rec = FXGL.spawn("rectangle",new SpawnData(x,y).put("rotation",rotation));

    }
}