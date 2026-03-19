package dev.faceless.pack.asset.item;

import com.google.gson.JsonObject;

/**
 * The model selection node inside an {@link ItemDefinition}.
 *
 * <p>Sealed — use the factory method or concrete subtypes:</p>
 * <ul>
 *   <li>{@link #simple(String)} — always renders one model, the most common case</li>
 *   <li>{@link SelectItemModel} — dispatches on a property value (e.g. custom model data)</li>
 *   <li>{@link ConditionItemModel} — switches between two models on a boolean property</li>
 *   <li>{@link CompositeItemModel} — layers multiple models on top of each other</li>
 * </ul>
 */
public sealed interface ItemModel
        permits SimpleItemModel, SelectItemModel, ConditionItemModel, CompositeItemModel {

    JsonObject toJson();

    /**
     * Renders a single model unconditionally.
     *
     * @param modelLocation resource location of the model in {@code models/},
     *                      e.g. {@code "mymod:item/my_sword"}
     */
    static SimpleItemModel simple(String modelLocation) {
        return new SimpleItemModel(modelLocation);
    }
}
