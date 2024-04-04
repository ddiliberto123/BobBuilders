package org.BobBuilders.FrenzyPenguins;
import com.almasb.fxgl.core.math.Vec2;
import static org.BobBuilders.FrenzyPenguins.FallingPenguinGame.*;

public class Physics {

    static final double air_density = 1.293; // Kg/m^3
    static final double temp_wing_area = 1;
    public static final double temp_lift_c = 1.5;
    public static final double temp_drag_c = 0.1;


    public static double penguin_velocity(){
        double penguin_velocity = Math.sqrt((Math.pow(penguin_x_velocity(),2)+Math.pow(penguin_y_velocity(),2)));
        System.out.println("1: " + penguin_x_velocity() + " 2: " + penguin_y_velocity() + " 3: " + penguin_velocity);
        return Math.floor(penguin_velocity);

    }



    public static Vec2 Lift(double p_angle){
        //calculating lift
        double lift = ((temp_lift_c*air_density*((Math.pow(penguin_velocity(),2))/2)*wing_area()));
        System.out.println("Lift: " + lift);

        System.out.println("Angle: " + p_angle);
        System.out.println("Y-Pos: " + penguin_y_postition());
        Vec2 lift_vector = new Vec2(Math.floor(lift*Math.cos((Math.toRadians(90+p_angle)))),Math.floor(lift*(Math.sin(Math.toRadians(p_angle)))));
        System.out.println("Lift x: " + lift_vector.x + " Lift y: " + lift_vector.y);

        double drag = (temp_drag_c*air_density*(Math.pow(penguin_velocity(),2)/2)*wing_area());
        System.out.println("Drag: " +  drag);
        Vec2 drag_vector = new Vec2(Math.floor(drag*(Math.toRadians(Math.cos(p_angle+270)))),Math.floor(drag*(Math.toRadians(Math.sin(p_angle+270)))));
        Vec2 final_flight_vector = new Vec2((lift_vector.x+drag_vector.x),(lift_vector.y+drag_vector.y));
        System.out.println("drag x: " + drag_vector.x + " drag y: " + drag_vector.y);
        System.out.println(final_flight_vector);
        System.out.println("-------------------------------------------------------------");
        return final_flight_vector;
    }


    // issue with the angle taken to calculate the magnitude of the vector, the current angle taken is that of the object,
    // must fix and take the angle



}



