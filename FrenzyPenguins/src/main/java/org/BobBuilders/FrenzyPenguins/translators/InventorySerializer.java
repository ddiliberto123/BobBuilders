package org.BobBuilders.FrenzyPenguins.translators;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.BobBuilders.FrenzyPenguins.Inventory;

import java.io.IOException;

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
        gen.writeStringField("hasJetpack", String.valueOf(inventory.isHasJetpack()));
        gen.writeStringField("hasSlide", String.valueOf(inventory.isHasSlide()));
        gen.writeStringField("hasGlider", String.valueOf(inventory.isHasGlider()));
        gen.writeStringField("pointsProperty", String.valueOf(inventory.getPointsPropertyValue()));
        gen.writeEndObject();
    }
}