package org.BobBuilders.FrenzyPenguins;
import com.almasb.fxgl.core.math.Vec2;
import static org.BobBuilders.FrenzyPenguins.FallingPenguinGame.*;

public class Physics {

    static final double air_density = 1.293; // Kg/m^3
    static final double temp_wing_area = 1;
    public static final double temp_lift_c = 1.5;

    public static void x_velocity(){

            double x_pos1 = penguin_x_postition();
            double x_pos2 = penguin_x_postition();
        System.out.println("1: " + x_pos1 + " 2: " + x_pos2);

    }
//    public static Vec2 get_velocity_at_ramp(){
//
//        double y_velocity = penguin_y_postition();
//        Vec2 p_velocity = new Vec2(x_velocity,y_velocity);
//        System.out.println();
//        return p_velocity;
//    }
    public static Vec2 Lift(double p_angle){
        //calculating lift
        //double lift = (temp_lift_c*air_density*((Math.pow(get_velocity_at_ramp().length(),2))/2)*temp_wing_area);
        //System.out.println(lift);
        return null;
    }
    Vec2 Drag(){
        return null;
    }
}



