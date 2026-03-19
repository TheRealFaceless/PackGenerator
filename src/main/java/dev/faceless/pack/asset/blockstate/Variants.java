package dev.faceless.pack.asset.blockstate;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * A blockstate definition using the {@code variants} format.
 *
 * <p>Each variant key is a comma-separated list of {@code property=value} pairs,
 * or {@code ""} for a block with no meaningful states (the most common case).</p>
 *
 * <pre>{@code
 * // Block with no states (e.g. a plain full cube)
 * new Variants().variant("", new ModelRef("mymod:block/ruby_block"))
 *
 * // Block with facing states (e.g. a directional torch)
 * new Variants()
 *     .variant("facing=north", new ModelRef("mymod:block/ruby_torch").y(0))
 *     .variant("facing=east",  new ModelRef("mymod:block/ruby_torch").y(90))
 *     .variant("facing=south", new ModelRef("mymod:block/ruby_torch").y(180))
 *     .variant("facing=west",  new ModelRef("mymod:block/ruby_torch").y(270))
 *
 * // Random rotation (multiple ModelRefs on one variant — equal probability)
 * new Variants()
 *     .variant("", List.of(
 *         new ModelRef("mymod:block/ruby_block"),
 *         new ModelRef("mymod:block/ruby_block").y(90),
 *         new ModelRef("mymod:block/ruby_block").y(180),
 *         new ModelRef("mymod:block/ruby_block").y(270)
 *     ))
 * }</pre>
 */
public final class Variants implements BlockStateDefinition {

    private final Map<String, List<ModelRef>> variants = new LinkedHashMap<>();

    /** Maps a single state string to one model. */
    public Variants variant(String state, ModelRef model) {
        variants.computeIfAbsent(state, k -> new ArrayList<>()).add(model);
        return this;
    }

    /** Maps a single state string to a list of randomly chosen models. */
    public Variants variant(String state, List<ModelRef> models) {
        variants.put(state, new ArrayList<>(models));
        return this;
    }

    @Override
    public JsonObject toJson() {
        JsonObject root = new JsonObject();
        JsonObject vars = new JsonObject();

        for (var entry : variants.entrySet()) {
            List<ModelRef> refs = entry.getValue();
            JsonElement value;
            if (refs.size() == 1) {
                value = refs.getFirst().toJson();
            } else {
                JsonArray arr = new JsonArray();
                refs.forEach(r -> arr.add(r.toJson()));
                value = arr;
            }
            vars.add(entry.getKey(), value);
        }

        root.add("variants", vars);
        return root;
    }
}
