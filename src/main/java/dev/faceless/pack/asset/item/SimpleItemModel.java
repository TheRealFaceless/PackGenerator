package dev.faceless.pack.asset.item;

import com.google.gson.JsonObject;

/**
 * An item model that unconditionally renders a single model file.
 *
 * <p>This is the correct type for any item that always looks the same regardless of NBT or state.
 * Prefer the factory {@link ItemModel#simple(String)} over constructing this directly.</p>
 *
 * @param modelLocation resource location of the model, e.g. {@code "mymod:item/my_sword"}
 */
public record SimpleItemModel(String modelLocation) implements ItemModel {

    @Override
    public JsonObject toJson() {
        JsonObject obj = new JsonObject();
        obj.addProperty("type", "minecraft:model");
        obj.addProperty("model", modelLocation);
        return obj;
    }
}
