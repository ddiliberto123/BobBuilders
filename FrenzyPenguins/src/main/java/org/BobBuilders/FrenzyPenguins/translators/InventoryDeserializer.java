package org.BobBuilders.FrenzyPenguins.translators;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.BobBuilders.FrenzyPenguins.Inventory;

import java.io.IOException;

/**
 * Overrides the deserializer of {@code Inventory} class
 */
public class InventoryDeserializer extends StdDeserializer<Inventory> {

    protected InventoryDeserializer() {
        this(null);
    }

    protected InventoryDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Inventory deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        if (p == null | ctxt == null) {
            throw new RuntimeException("Parser is broken");
        }
        Inventory inventory = Inventory.createInstance();
        ObjectCodec codec = p.getCodec();
        JsonNode node = codec.readTree(p);

        JsonNode rampLevelNode = node.get("rampLevel");
        int rampLevel = rampLevelNode.asInt();
        inventory.setRampLevel(rampLevel);

        JsonNode jetpackLevelNode = node.get("jetpackLevel");
        int jetpackLevel = jetpackLevelNode.asInt();
        inventory.setJetpackLevel(jetpackLevel);

        JsonNode gliderLevelNode = node.get("gliderLevel");
        int gliderlevel = gliderLevelNode.asInt();
        inventory.setGliderLevel(gliderlevel);

        JsonNode slideLevelNode = node.get("slideLevel");
        int slideLevel = slideLevelNode.asInt();
        inventory.setSlideLevel(slideLevel);

        JsonNode pointsPropertyNode = node.get("pointsProperty");
        int pointsPropertyValue = pointsPropertyNode.asInt();
        inventory.setPointsPropertyValue(pointsPropertyValue);

        JsonNode totalDistanceFlownNode = node.get("totalDistanceFlown");
        int totalDistanceFlown = totalDistanceFlownNode.asInt();
        inventory.setTotalDistanceFlown(totalDistanceFlown);

        JsonNode maxDistanceFlownNode = node.get("maxDistanceFlown");
        int maxDistanceFlown = maxDistanceFlownNode.asInt();
        inventory.setMaxDistanceFlown(maxDistanceFlown);

        JsonNode networthNode = node.get("networth");
        int networth = networthNode.asInt();
        inventory.setNetworth(networth);
        return inventory;
    }
}
