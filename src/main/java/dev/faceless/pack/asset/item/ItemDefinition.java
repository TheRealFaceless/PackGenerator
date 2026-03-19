package dev.faceless.pack.asset.item;

import com.google.gson.JsonObject;

/**
 * Represents a single file under {@code assets/<namespace>/items/} (introduced in 1.21.4).
 *
 * <p>Tells Minecraft which {@link ItemModel} to use when rendering a specific item.
 * The actual geometry and textures live in {@code models/item/} and {@code textures/item/}.</p>
 *
 * <p>Obtain instances from {@link dev.faceless.pack.asset.ItemRegistry#define}:</p>
 * <pre>{@code
 * // Always show one model
 * ns.items().define("my_sword", ItemModel.simple("mymod:item/my_sword"));
 *
 * // Custom model data switching
 * ns.items().define("iron_sword",
 *     new SelectItemModel("minecraft:custom_model_data",
 *                         ItemModel.simple("minecraft:item/iron_sword"))
 *         .addCase("my_skin", ItemModel.simple("mymod:item/my_skin")));
 * }</pre>
 */
public final class ItemDefinition {

    private final ItemModel model;
    private Boolean         handAnimationOnSwap;
    private Boolean         oversizedInGui;

    public ItemDefinition(ItemModel model) {
        this.model = model;
    }

    /**
     * Whether a swap animation plays in first-person when this item is selected.
     * Defaults to {@code true}; set to {@code false} to suppress it.
     */
    public ItemDefinition handAnimationOnSwap(boolean value) {
        this.handAnimationOnSwap = value;
        return this;
    }

    /** Whether the model may render larger than the inventory slot. Default {@code false}. */
    public ItemDefinition oversizedInGui(boolean value) {
        this.oversizedInGui = value;
        return this;
    }

    public JsonObject toJson() {
        JsonObject root = new JsonObject();
        root.add("model", model.toJson());
        if (Boolean.FALSE.equals(handAnimationOnSwap))
            root.addProperty("hand_animation_on_swap", false);
        if (Boolean.TRUE.equals(oversizedInGui))
            root.addProperty("oversized_in_gui", true);
        return root;
    }
}
