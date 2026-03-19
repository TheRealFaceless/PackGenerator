package dev.faceless.pack.asset.model;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.*;

/**
 * Represents a single model JSON file under {@code assets/<ns>/models/}.
 *
 * <p>Used for both block models (referenced by blockstate files) and item models
 * (referenced by item definition files in {@code assets/<ns>/items/}).
 * The {@link dev.faceless.pack.asset.ModelRegistry} stores these under
 * {@code models/block/} and {@code models/item/} respectively.</p>
 *
 * <p>Common built-in parents:</p>
 * <ul>
 *   <li>{@code "block/cube_all"} — full cube, same texture on all sides</li>
 *   <li>{@code "block/cube_column"} — cube with separate top and side textures</li>
 *   <li>{@code "item/generated"} — flat 2D item sprite (most items)</li>
 *   <li>{@code "item/handheld"} — flat 2D sprite with held-item transforms</li>
 *   <li>{@code "builtin/entity"} — entity-based rendering (chest, banner, etc.)</li>
 * </ul>
 *
 * <pre>{@code
 * // Simple block
 * ns.models().block("my_block").parent("block/cube_all").texture("all", "mymod:block/my_block");
 *
 * // Simple item
 * ns.models().item("my_sword").parent("item/handheld").texture("layer0", "mymod:item/my_sword");
 * }</pre>
 */
public final class Model {

    private String                     parent;
    private Boolean                    ambientOcclusion;
    private GuiLight                   guiLight;
    private final Map<String, String>  textures = new LinkedHashMap<>();
    private final List<ModelElement>   elements = new ArrayList<>();
    private final Map<String, Display> display  = new LinkedHashMap<>();

    /** Sets the parent model, e.g. {@code "block/cube_all"} or {@code "item/generated"}. */
    public Model parent(String parent) {
        this.parent = parent;
        return this;
    }

    /**
     * Assigns a texture variable.
     *
     * @param variable key used in face references (without {@code #}), e.g. {@code "all"}, {@code "layer0"}
     * @param location resource location of the texture, e.g. {@code "mymod:block/stone"}
     */
    public Model texture(String variable, String location) {
        textures.put(variable, location);
        return this;
    }

    public Model element(ModelElement element) {
        elements.add(element);
        return this;
    }

    public Model display(DisplaySlot slot, Display transform) {
        display.put(slot.key(), transform);
        return this;
    }

    public Model ambientOcclusion(boolean value) {
        this.ambientOcclusion = value;
        return this;
    }

    /** Controls directional lighting in the GUI ({@link GuiLight#FRONT} or {@link GuiLight#SIDE}). */
    public Model guiLight(GuiLight light) {
        this.guiLight = light;
        return this;
    }

    public JsonObject toJson() {
        JsonObject obj = new JsonObject();
        if (parent           != null) obj.addProperty("parent", parent);
        if (ambientOcclusion != null) obj.addProperty("ambientocclusion", ambientOcclusion);
        if (guiLight         != null) obj.addProperty("gui_light", guiLight.name().toLowerCase());

        if (!textures.isEmpty()) {
            JsonObject tex = new JsonObject();
            textures.forEach(tex::addProperty);
            obj.add("textures", tex);
        }
        if (!display.isEmpty()) {
            JsonObject disp = new JsonObject();
            display.forEach((k, v) -> disp.add(k, v.toJson()));
            obj.add("display", disp);
        }
        if (!elements.isEmpty()) {
            JsonArray arr = new JsonArray();
            elements.stream().map(ModelElement::toJson).forEach(arr::add);
            obj.add("elements", arr);
        }
        return obj;
    }

    /** Controls how the model is lit when rendered in a GUI. */
    public enum GuiLight { FRONT, SIDE }
}
