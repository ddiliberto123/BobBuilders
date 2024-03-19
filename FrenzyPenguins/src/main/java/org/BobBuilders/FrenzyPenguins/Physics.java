package org.BobBuilders.FrenzyPenguins;
import com.almasb.fxgl.core.math.Vec2;
import static org.BobBuilders.FrenzyPenguins.FallingPenguinGame.*;

public class Physics {

    final double air_density = 1.293; // Kg/m^3
    final double temp_wing_area = 1;
    public final double temp_lift_c = 1.5;



    public static Vec2 get_velocity(double x1, double x2, double y1, double y2){

        double vx = ((x2-x1)/2);

        double vy = ((y2-y1)/2);
        Vec2 v = new Vec2(vx,vy);
        return v;
    }

    Vec2 Lift(int p_angle){

        //calculating lift
        //double lift = (temp_lift_c*air_density*((Math.pow(get_velocity().length(),2))/2)*temp_wing_area);

        return null;
    }

    Vec2 Drag(){

        return null;
    }

}
