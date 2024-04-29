package org.BobBuilders.FrenzyPenguins;
import com.almasb.fxgl.core.math.Vec2;
import static org.BobBuilders.FrenzyPenguins.FallingPenguinGame.*;
import static org.BobBuilders.FrenzyPenguins.FallingPenguinGame.penguin_x_velocity;
import static org.BobBuilders.FrenzyPenguins.FallingPenguinGame.penguin_y_velocity;

public class Physics {

    static final double air_density = 1.293;
    public static final double temp_lift_c = 1.5;
    public static final double temp_drag_c = 0.1;
    static Inventory inventory = Inventory.getInstance();

    //Gets the magnitude of the penguin_velocity
    public static double penguin_velocity(){
        double penguin_velocity = Math.sqrt((Math.pow(penguin_x_velocity(),2)+Math.pow(penguin_y_velocity(),2)));
        if(penguin_velocity > ((double)inventory.getGliderLevel()/10)*7){
            penguin_velocity = ((double)inventory.getGliderLevel()/10)*7;
        }
        return Math.floor(penguin_velocity);
    }

    public static Vec2 Lift(double p_angle){
        //These angle calculations allow us to have the correct angles for lift and drag (they're different)
        //Calculating the angle for lift
        p_angle = p_angle % 360;
        if (p_angle < 0) {
            p_angle += 360;
        }
        if(p_angle > 90 && p_angle <=270){
            p_angle = 180 - p_angle;
        }
        //Calculating the angle for drag
        double d_angle = p_angle + 180;
        d_angle = d_angle%360;
        if (d_angle < 0) {
            d_angle += 360;
        }

        //Calculating Lift
        double lift = ((temp_lift_c*air_density*((Math.pow(penguin_velocity(),2))/2)*1));
        //Creating a lift vector and calculating the values for each component of the vector
        Vec2 lift_vector = new Vec2(Math.floor(lift*Math.sin((Math.toRadians(p_angle)))),Math.floor(lift*(Math.cos(Math.toRadians(p_angle)))));
        //Calculating Drag
        double drag = (temp_drag_c*air_density*((Math.pow(penguin_velocity(),2))/2)*1);
        //Creating a drag vector and calculating the values for each component of the vector
        Vec2 drag_vector = new Vec2(Math.floor(drag*(Math.cos(Math.toRadians(d_angle)))),Math.floor(drag*(Math.sin(Math.toRadians(d_angle)))));
        //Calculating the final flight_vector
        Vec2 final_flight_vector = new Vec2(0.016*(lift_vector.x+drag_vector.x),0.016*(lift_vector.y+drag_vector.y));
        return final_flight_vector;
    }

    public static Vec2 B_mockup(double p_angle){
        //Buoyancy is calculated based on the x_velocity of the penguin
        double vx = Math.round(penguin_x_velocity());
        Vec2 buoyancy = new Vec2();
        //This method uses velocity to mimic Buoyancy
        if(vx!=0){
            double force  = penguin_x_velocity()*0.2;
            buoyancy.set((float)(force*Math.sin(Math.toRadians(p_angle))),(float)(force*Math.cos(Math.toRadians(p_angle))));
            //In case the buoyancy is too big this is the first cap in place to prevent glitches
            if(buoyancy.length() > 20){
                buoyancy.set((float)Math.sin(Math.toRadians(p_angle))*10,(float)Math.sin(Math.toRadians(p_angle))*10);
                return buoyancy;
            }
            else {
                return buoyancy;
            }
        }
        else{
            buoyancy.set(0,0);
            return buoyancy;
        }
    }
}