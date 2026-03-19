package dev.faceless.pack.asset;

import dev.faceless.pack.TexturePack;
import dev.faceless.pack.asset.model.Model;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;

/**
 * Registry for model JSON files under {@code assets/<ns>/models/}.
 *
 * <p>Models are split into two subdirectories matching Minecraft's layout:</p>
 * <ul>
 *   <li>{@code models/block/} — referenced by blockstate definition files</li>
 *   <li>{@code models/item/} — referenced by item definition files in {@code items/}</li>
 * </ul>
 *
 * <p>Both types share the same {@link Model} class; the distinction is only where the
 * file is written on export.</p>
 */
public final class ModelRegistry {

    private final Map<String, Model> blockModels = new LinkedHashMap<>();
    private final Map<String, Model> itemModels  = new LinkedHashMap<>();

    /**
     * Returns (or lazily creates) a block model.
     * Written to {@code models/block/<name>.json}.
     */
    public Model block(String name) {
        return blockModels.computeIfAbsent(name, k -> new Model());
    }

    /**
     * Registers a pre-built block model.
     *
     * @return the supplied model for chaining
     */
    public Model block(String name, Model model) {
        blockModels.put(name, model);
        return model;
    }

    /**
     * Returns (or lazily creates) an item model.
     * Written to {@code models/item/<name>.json}.
     */
    public Model item(String name) {
        return itemModels.computeIfAbsent(name, k -> new Model());
    }

    /**
     * Registers a pre-built item model.
     *
     * @return the supplied model for chaining
     */
    public Model item(String name, Model model) {
        itemModels.put(name, model);
        return model;
    }

    public void export(Path modelsRoot) throws IOException {
        writeAll(blockModels, modelsRoot.resolve("block"));
        writeAll(itemModels,  modelsRoot.resolve("item"));
    }

    private static void writeAll(Map<String, Model> map, Path dir) throws IOException {
        if (map.isEmpty()) return;
        Files.createDirectories(dir);
        for (var entry : map.entrySet()) {
            Files.writeString(
                    dir.resolve(entry.getKey() + ".json"),
                    TexturePack.GSON.toJson(entry.getValue().toJson())
            );
        }
    }
}
