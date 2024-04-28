package org.BobBuilders.FrenzyPenguins;
import com.almasb.fxgl.core.math.Vec2;
import static org.BobBuilders.FrenzyPenguins.FallingPenguinGame.*;
import com.almasb.fxgl.core.math.Vec2;
import static org.BobBuilders.FrenzyPenguins.FallingPenguinGame.penguin_x_velocity;
import static org.BobBuilders.FrenzyPenguins.FallingPenguinGame.penguin_y_velocity;
import org.BobBuilders.FrenzyPenguins.Store;
public class Physics {

    static final double air_density = 1.293; // Kg/m^3
    static final double water_density = 1000; //Kg/m^3
    static final double gravity = -9.8;
    public static final double temp_lift_c = 1.5;
    public static final double temp_drag_c = 0.1;
    static Store store = Store.getInstance();



    public static double penguin_velocity(){
        double penguin_velocity = Math.sqrt((Math.pow(penguin_x_velocity(),2)+Math.pow(penguin_y_velocity(),2)));
        if(penguin_velocity > ((double)store.getGliderLevel()/10)*7){
            penguin_velocity = ((double)store.getGliderLevel()/10)*7;
        }
//        System.out.println("1: " + penguin_x_velocity() + " 2: " + penguin_y_velocity() + " 3: " + penguin_velocity);
        return Math.floor(penguin_velocity);
    }

    public static double penguin_velocity_angle(){
        return Math.asin(penguin_y_velocity()/penguin_x_velocity());
    }

    public static double angle_fix(double p_angle){
        p_angle = p_angle % 360;
        if (p_angle < 0) {
            p_angle += 360;
        }
        if(p_angle > 90 && p_angle <=270){
            p_angle = 180 - p_angle;
        }
        return p_angle;
    }
    public static Vec2 Lift(double p_angle){
        p_angle = angle_fix(p_angle);
        //calculating lift

        //Modifies lift_coefficient based on angle
        double lift_c;
        lift_c = p_angle/360;

        double lift = ((temp_lift_c*air_density*((Math.pow(penguin_velocity(),2))/2)*lift_c));

        System.out.println(p_angle);
        Vec2 lift_vector = new Vec2(Math.floor(lift*Math.sin((Math.toRadians(p_angle)))),Math.floor(lift*(Math.cos(Math.toRadians(p_angle)))));
        System.out.println("lift: "+lift);
        return lift_vector;
    }
    public static Vec2 Drag(double p_angle){
        double d_angle = angle_fix(p_angle);
        d_angle = d_angle + 180;
        d_angle = d_angle%360;
        if (d_angle < 0) {
            d_angle += 360;
        }
        double drag = (temp_drag_c*air_density*((Math.pow(penguin_velocity(),2))/2)*1);
        Vec2 drag_vector = new Vec2(Math.floor(drag*(Math.cos(Math.toRadians(d_angle)))),Math.floor(drag*(Math.sin(Math.toRadians(d_angle)))));
        System.out.println("drag:"+drag);
        return drag_vector;

    }

    public static Vec2 Flight_vectors(double angle){
        Vec2 lift = Lift(angle);
        Vec2 drag = Drag(angle);
        Vec2 final_flight_vector = new Vec2(0.016*(lift.x+0.5*drag.x),0.016*(lift.y+0.5*drag.y));
        return final_flight_vector;
    }


    //It might not be worth it to model buoyancy, considering that we still do not understand the scale idk...
    public static Vec2 Buoyancy(double p_angle){
        double Buoyancy = (water_density*gravity*get_penguin_area());
        Vec2 buoyancy_vector = new Vec2(Buoyancy*Math.sin(Math.toRadians(p_angle)), Buoyancy*Math.cos(Math.toRadians(p_angle)));
        System.out.println("x: "+buoyancy_vector.x + " y: " + buoyancy_vector.y);
        return buoyancy_vector;
    }

    //Very painless way of implementing "buoyancy"
    public static Vec2 B_mockup(double p_angle){
        double vx = Math.round(penguin_x_velocity());
        //System.out.println(vx);
        Vec2 fake_buoyancy = new Vec2();
        if(vx!=0){
            //double force = Math.random()*15;
            double force  = penguin_x_velocity()*0.2; //yes this is janky but i think it will work
            fake_buoyancy.set((float)(force*Math.sin(Math.toRadians(p_angle))),(float)(force*Math.cos(Math.toRadians(p_angle))));
            if(force > 20){
                fake_buoyancy.set((float)Math.sin(Math.toRadians(p_angle))*20,(float)Math.sin(Math.toRadians(p_angle))*20);
                return fake_buoyancy;
            }
            else {
                return fake_buoyancy;
            }
        }
        else{
            fake_buoyancy.set(0,0);
            return fake_buoyancy;
        }
    }
}

