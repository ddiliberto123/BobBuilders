package org.BobBuilders.FrenzyPenguins.util;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.SpawnData;

/**
 * Spawns and creates all default and/or common entities in the game.
 */
public class EntitySpawner {
    /**
     * Name of rectangle entity
     */
    public static final String RECTANGLE = "rectangle";
    /**
     * Name of circle entity
     */
    public static final String CIRCLE = "circle";
    /**
     * Name of curve entity
     */
    public static final String CURVE = "curve";
    /**
     * Name of triangle entity
     */
    public static final String TRIANGLE = "triangle";
    /**
     * Name of background
     */
    public static final String BACKGROUND = "background";

    /**
     * Spawns a rectangle centered at the top left point
     *
     * @param startX {@code double} top left x position
     * @param startY {@code double} top left y position
     * @param width  {@code double} width of rectangle
     * @param height {@code double} height of rectangle
     */

    public static void spawnRectangle(double startX, double startY, double width, double height) {
        if (width == 0 || height == 0) {
            throw new RuntimeException("Width or height too small");
        }
        FXGL.spawn(RECTANGLE, new SpawnData(startX, startY)
                .put(Constant.WIDTH, width)
                .put(Constant.HEIGHT, height));
    }

    /**
     * Spawns a curve at a designated position and fills in the area around it to a control point.
     *
     * @param startX    {@code double} center x position
     * @param startY    {@code double} center y position
     * @param fromAngle {@code double} angle where the curve begins - east is 0 degrees
     * @param toAngle   {@code double} angle where the curve ends
     */
    public static void spawnCurve(double startX, double startY, double radius, double fromAngle, double toAngle) {
        if (fromAngle == toAngle) {
            throw new RuntimeException("Angle too small");
        } else if (radius == 0) {
            throw new RuntimeException("Radius too small");
        }
        FXGL.spawn(CURVE, new SpawnData(startX, startY)
                .put(Constant.FROM_ANGLE, fromAngle)
                .put(Constant.TO_ANGLE, toAngle)
                .put(Constant.RADIUS, radius));
    }

    /**
     * Spawns a circle at a designated position centered around an x,y coordinate
     *
     * @param startX {@code double} center x position
     * @param startY {@code double} center y position
     * @param radius {@code double} radius
     */
    public static void spawnCircle(double startX, double startY, double radius) {
        if (radius == 0) {
            throw new RuntimeException("Radius too small");
        }
        FXGL.spawn(CIRCLE, new SpawnData(startX, startY)
                .put(Constant.RADIUS, radius));
    }
}
