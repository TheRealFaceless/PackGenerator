package dev.faceless.pack.asset.font;

import com.google.gson.JsonObject;

import java.util.Map;

/**
 * A space font provider assigning custom advance widths (in pixels) to specific characters.
 *
 * <p>Negative advances are valid and commonly used to create invisible offset characters
 * for HUD/GUI alignment tricks.</p>
 *
 * <pre>{@code
 * new SpaceFontProvider(Map.of("\uF001", -8, "\uF002", -16))
 * }</pre>
 *
 * @param advances map of single-character string → pixel advance (may be negative)
 */
public record SpaceFontProvider(Map<String, Integer> advances) implements FontProvider {

    @Override
    public JsonObject toJson() {
        JsonObject obj = new JsonObject();
        obj.addProperty("type", "space");
        JsonObject adv = new JsonObject();
        advances.forEach(adv::addProperty);
        obj.add("advances", adv);
        return obj;
    }
}
