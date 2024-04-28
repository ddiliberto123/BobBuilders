package org.BobBuilders.FrenzyPenguins.translators;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.BobBuilders.FrenzyPenguins.Inventory;

import java.io.IOException;

/**
 * Overrides the serializaton of the {@code Inventory} class
 */
public class InventorySerializer extends StdSerializer<Inventory> {

    protected InventorySerializer() {
        this(null);
    }

    protected InventorySerializer(Class<Inventory> t) {
        super(t);
    }

    @Override
    public void serialize(Inventory inventory, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        gen.writeStringField("rampLevel", String.valueOf(inventory.getRampLevel()));
        gen.writeStringField("jetpackLevel", String.valueOf(inventory.getJetpackLevel()));
        gen.writeStringField("gliderLevel", String.valueOf(inventory.getGliderLevel()));
        gen.writeStringField("slideLevel", String.valueOf(inventory.getSlideLevel()));
        gen.writeStringField("pointsProperty", String.valueOf(inventory.getPointsPropertyValue()));
        gen.writeStringField("totalDistanceFlown", String.valueOf(inventory.getTotalDistanceFlown()));
        gen.writeStringField("maxDistanceFlown", String.valueOf(inventory.getMaxDistanceFlown()));
        gen.writeStringField("networth", String.valueOf(inventory.getNetworth()));
        gen.writeEndObject();
    }
}
