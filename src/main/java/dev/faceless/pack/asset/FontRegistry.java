package dev.faceless.pack.asset;

import dev.faceless.pack.TexturePack;
import dev.faceless.pack.asset.font.FontDefinition;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;

/**
 * Registry for font definition JSON files under {@code assets/<ns>/font/}.
 *
 * <p>The most common target is {@code "default"} (the in-game chat/UI font),
 * but packs can register fonts under any name and reference them via text components.</p>
 */
public final class FontRegistry {

    private final Map<String, FontDefinition> definitions = new LinkedHashMap<>();

    /**
     * Returns (or lazily creates) a font definition by name.
     *
     * @param name font file name without extension, e.g. {@code "default"} or {@code "alt"}
     */
    public FontDefinition define(String name) {
        return definitions.computeIfAbsent(name, k -> new FontDefinition());
    }

    /**
     * Registers a pre-built font definition.
     *
     * @return the supplied definition for chaining
     */
    public FontDefinition define(String name, FontDefinition definition) {
        definitions.put(name, definition);
        return definition;
    }

    public void export(Path fontRoot) throws IOException {
        if (definitions.isEmpty()) return;
        Files.createDirectories(fontRoot);
        for (var entry : definitions.entrySet()) {
            Files.writeString(
                    fontRoot.resolve(entry.getKey() + ".json"),
                    TexturePack.GSON.toJson(entry.getValue().toJson())
            );
        }
    }
}
