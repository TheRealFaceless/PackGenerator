package dev.faceless.pack.asset.item;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

/**
 * An item model that renders multiple models layered on top of one another.
 *
 * <p>Useful for items composed of several independent visual layers,
 * such as a tool with a separate overlay texture.</p>
 *
 * <pre>{@code
 * new CompositeItemModel()
 *     .add(ItemModel.simple("mymod:item/sword_base"))
 *     .add(ItemModel.simple("mymod:item/sword_overlay"));
 * }</pre>
 */
public final class CompositeItemModel implements ItemModel {

    private final List<ItemModel> models = new ArrayList<>();

    public CompositeItemModel add(ItemModel model) {
        models.add(model);
        return this;
    }

    @Override
    public JsonObject toJson() {
        JsonObject obj = new JsonObject();
        obj.addProperty("type", "minecraft:composite");
        JsonArray arr = new JsonArray();
        models.stream().map(ItemModel::toJson).forEach(arr::add);
        obj.add("models", arr);
        return obj;
    }
}
