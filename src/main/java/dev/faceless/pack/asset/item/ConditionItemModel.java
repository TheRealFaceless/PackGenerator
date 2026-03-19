package dev.faceless.pack.asset.item;

import com.google.gson.JsonObject;

/**
 * An item model that chooses between two models based on a boolean property.
 *
 * <pre>{@code
 * new ConditionItemModel(
 *     "minecraft:using_item",
 *     ItemModel.simple("mymod:item/bow_pulling"),
 *     ItemModel.simple("mymod:item/bow")
 * )
 * }</pre>
 *
 * @param property the boolean property to test, e.g. {@code "minecraft:using_item"},
 *                 {@code "minecraft:selected"}, {@code "minecraft:broken"}
 * @param onTrue   model rendered when the property is {@code true}
 * @param onFalse  model rendered when the property is {@code false}
 */
public record ConditionItemModel(String property, ItemModel onTrue, ItemModel onFalse)
        implements ItemModel {

    @Override
    public JsonObject toJson() {
        JsonObject obj = new JsonObject();
        obj.addProperty("type", "minecraft:condition");
        obj.addProperty("property", property);
        obj.add("on_true",  onTrue.toJson());
        obj.add("on_false", onFalse.toJson());
        return obj;
    }
}
