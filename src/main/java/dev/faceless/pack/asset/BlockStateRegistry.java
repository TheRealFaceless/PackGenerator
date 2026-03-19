package dev.faceless.pack.asset;

import dev.faceless.pack.TexturePack;
import dev.faceless.pack.asset.blockstate.BlockStateDefinition;
import dev.faceless.pack.asset.blockstate.ModelRef;
import dev.faceless.pack.asset.blockstate.Variants;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;

/**
 * Registry for blockstate JSON files under {@code assets/<namespace>/blockstates/}.
 *
 * <p>Every block that renders in the world needs a blockstate file. It maps the block's
 * current state (e.g. {@code "facing=north,powered=true"}) to a {@link ModelRef}.</p>
 *
 * <p>For a simple block with no meaningful states:</p>
 * <pre>{@code
 * ns.blockstates().simple("ruby_block", "mymod:block/ruby_block");
 * }</pre>
 *
 * <p>For a block with directional variants:</p>
 * <pre>{@code
 * ns.blockstates().define("ruby_torch",
 *     new Variants()
 *         .variant("facing=north", new ModelRef("mymod:block/ruby_torch"))
 *         .variant("facing=east",  new ModelRef("mymod:block/ruby_torch").y(90))
 *         .variant("facing=south", new ModelRef("mymod:block/ruby_torch").y(180))
 *         .variant("facing=west",  new ModelRef("mymod:block/ruby_torch").y(270))
 * );
 * }</pre>
 */
public final class BlockStateRegistry {

    private final Map<String, BlockStateDefinition> definitions = new LinkedHashMap<>();

    /**
     * Convenience method for blocks with a single state (no properties affecting visuals).
     * Equivalent to {@code define(name, new Variants().variant("", new ModelRef(modelLocation)))}.
     *
     * @param name          block name, e.g. {@code "ruby_block"}
     * @param modelLocation model resource location, e.g. {@code "mymod:block/ruby_block"}
     */
    public void simple(String name, String modelLocation) {
        define(name, new Variants().variant("", new ModelRef(modelLocation)));
    }

    /**
     * Registers a fully configured blockstate definition.
     *
     * @param name       block name matching the block's registry ID, e.g. {@code "ruby_slab"}
     * @param definition the {@link Variants} or {@link dev.faceless.pack.asset.blockstate.Multipart} definition
     */
    public void define(String name, BlockStateDefinition definition) {
        definitions.put(name, definition);
    }

    public void export(Path blockstatesRoot) throws IOException {
        if (definitions.isEmpty()) return;
        Files.createDirectories(blockstatesRoot);
        for (var entry : definitions.entrySet()) {
            Files.writeString(
                    blockstatesRoot.resolve(entry.getKey() + ".json"),
                    TexturePack.GSON.toJson(entry.getValue().toJson())
            );
        }
    }
}
