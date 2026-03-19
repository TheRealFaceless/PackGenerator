package dev.faceless.pack.asset.item;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

/**
 * An item model that selects which model to render based on a property value.
 *
 * <p>Commonly used with {@code "minecraft:custom_model_data"} to switch textures
 * based on an item's custom model data string tag. Each {@link Case} maps one or more
 * values to a model. The {@code fallback} is rendered when no case matches.</p>
 *
 * <pre>{@code
 * new SelectItemModel("minecraft:custom_model_data",
 *                     ItemModel.simple("minecraft:item/iron_sword"))
 *     .addCase("my_skin", ItemModel.simple("mymod:item/my_skin"));
 * }</pre>
 *
 */
public final class SelectItemModel implements ItemModel {
    private final String property;
    private final ItemModel fallback;
    private final List<Case> cases = new ArrayList<>();

    /**
     *
     * @param property the item property to dispatch on, e.g. {@code "minecraft:custom_model_data"}
     * @param fallback the model rendered when no case matches
     */
    public SelectItemModel(String property, ItemModel fallback) {
        this.property = property;
        this.fallback = fallback;
    }

    /**
     * Adds a case mapping a single value to a model.
     *
     * @param when  the property value to match
     * @param model the model to render when matched
     */
    public SelectItemModel addCase(String when, ItemModel model) {
        cases.add(new Case(List.of(when), model));
        return this;
    }

    /**
     * Adds a case where any of the provided values maps to the same model.
     *
     * @param when  list of values that all map to {@code model}
     * @param model the model to render when any value matches
     */
    public SelectItemModel addCase(List<String> when, ItemModel model) {
        cases.add(new Case(List.copyOf(when), model));
        return this;
    }

    @Override
    public JsonObject toJson() {
        JsonObject obj = new JsonObject();
        obj.addProperty("type", "minecraft:select");
        obj.addProperty("property", property);

        JsonArray casesArr = new JsonArray();
        for (Case c : cases) {
            JsonObject entry = new JsonObject();
            if (c.when().size() == 1) {
                entry.addProperty("when", c.when().getFirst());
            } else {
                JsonArray whenArr = new JsonArray();
                c.when().forEach(whenArr::add);
                entry.add("when", whenArr);
            }
            entry.add("model", c.model().toJson());
            casesArr.add(entry);
        }
        obj.add("cases", casesArr);
        obj.add("fallback", fallback.toJson());
        return obj;
    }

    private record Case(List<String> when, ItemModel model) {}
}
