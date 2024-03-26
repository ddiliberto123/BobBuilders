package org.BobBuilders.FrenzyPenguins;
import com.almasb.fxgl.core.math.Vec2;
import static org.BobBuilders.FrenzyPenguins.FallingPenguinGame.*;

public class Physics {

    static final double air_density = 1.293; // Kg/m^3
    static final double temp_wing_area = 1;
    public static final double temp_lift_c = 1.5;

    public static double penguin_velocity(){
        double penguin_velocity = Math.sqrt((Math.pow(penguin_x_velocity(),2)+Math.pow(penguin_y_velocity(),2)));
        return penguin_velocity;
        //System.out.println("1: " + penguin_x_velocity() + " 2: " + penguin_y_velocity() + " 3: " + penguin_velocity);
    }



    public static Vec2 Lift(double p_angle){
        //calculating lift
        double lift = ((temp_lift_c*air_density*((Math.pow(penguin_velocity(),2))/2)*wing_area()))*0.00000001;

        System.out.println("Angle: " + p_angle);
        System.out.println("Y-Pos: " + penguin_y_postition());
        Vec2 lift_vector = new Vec2(lift*Math.cos(p_angle+180),lift*Math.sin(p_angle+90));
        System.out.println("Lift y: " + lift_vector.x + "Lift x: " + lift_vector.y);
        return lift_vector;
    }
    Vec2 Drag(){
        return null;
    }
}



