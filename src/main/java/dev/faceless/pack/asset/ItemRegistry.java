package dev.faceless.pack.asset;

import dev.faceless.pack.TexturePack;
import dev.faceless.pack.asset.item.ItemDefinition;
import dev.faceless.pack.asset.item.ItemModel;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;

/**
 * Registry for item definition JSON files under {@code assets/<namespace>/items/}.
 *
 * <p>Each entry tells Minecraft how to render one item. For a fully custom item texture,
 * you need matching entries in all three registries:</p>
 *
 * <pre>{@code
 * // 1. assets/mymod/items/my_sword.json  ← item definition (this registry)
 * ns.items().define("my_sword", ItemModel.simple("mymod:item/my_sword"));
 *
 * // 2. assets/mymod/models/item/my_sword.json  ← model geometry
 * ns.models().item("my_sword").parent("item/handheld").texture("layer0", "mymod:item/my_sword");
 *
 * // 3. assets/mymod/textures/item/my_sword.png  ← texture
 * ns.textures().add("item/my_sword", myImage);
 * }</pre>
 *
 * <p>To override a vanilla item's texture, use the {@code minecraft} namespace:</p>
 * <pre>{@code
 * Namespace mc = pack.namespace("minecraft");
 * mc.items().define("iron_sword",
 *     new SelectItemModel("minecraft:custom_model_data",
 *                         ItemModel.simple("minecraft:item/iron_sword"))
 *         .addCase("my_skin", ItemModel.simple("mymod:item/my_skin")));
 * }</pre>
 */
public final class ItemRegistry {

    private final Map<String, ItemDefinition> definitions = new LinkedHashMap<>();

    /**
     * Creates and registers an item definition from an {@link ItemModel}.
     *
     * @param name  item name, e.g. {@code "iron_sword"} or {@code "my_item"}
     * @param model the model selection tree
     * @return the created {@link ItemDefinition} for optional further configuration
     */
    public ItemDefinition define(String name, ItemModel model) {
        ItemDefinition def = new ItemDefinition(model);
        definitions.put(name, def);
        return def;
    }

    /**
     * Registers a pre-built {@link ItemDefinition}.
     *
     * @return the supplied definition for chaining
     */
    public ItemDefinition define(String name, ItemDefinition definition) {
        definitions.put(name, definition);
        return definition;
    }

    public void export(Path itemsRoot) throws IOException {
        if (definitions.isEmpty()) return;
        Files.createDirectories(itemsRoot);
        for (var entry : definitions.entrySet()) {
            Files.writeString(
                    itemsRoot.resolve(entry.getKey() + ".json"),
                    TexturePack.GSON.toJson(entry.getValue().toJson())
            );
        }
    }
}
