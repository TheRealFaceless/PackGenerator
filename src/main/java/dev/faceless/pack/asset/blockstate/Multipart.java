package dev.faceless.pack.asset.blockstate;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * A blockstate definition using the {@code multipart} format.
 *
 * <p>The final rendered block is assembled from multiple independent parts,
 * each applied unconditionally or when a {@code when} condition is satisfied.
 * This is ideal for connective blocks like fences, walls, and glass panes.</p>
 *
 * <pre>{@code
 * new Multipart()
 *     // Central post — always applied
 *     .part(new Part(new ModelRef("mymod:block/ruby_fence_post")))
 *     // Each side — applied only when that direction is connected
 *     .part(new Part(new ModelRef("mymod:block/ruby_fence_side"))
 *         .when("north", "true"))
 *     .part(new Part(new ModelRef("mymod:block/ruby_fence_side").y(90))
 *         .when("east", "true"))
 *     .part(new Part(new ModelRef("mymod:block/ruby_fence_side").y(180))
 *         .when("south", "true"))
 *     .part(new Part(new ModelRef("mymod:block/ruby_fence_side").y(270))
 *         .when("west", "true"))
 * }</pre>
 */
public final class Multipart implements BlockStateDefinition {

    private final List<Part> parts = new ArrayList<>();

    public Multipart part(Part part) {
        parts.add(part);
        return this;
    }

    @Override
    public JsonObject toJson() {
        JsonObject root = new JsonObject();
        JsonArray  arr  = new JsonArray();
        parts.forEach(p -> arr.add(p.toJson()));
        root.add("multipart", arr);
        return root;
    }

    // ─────────────────────────────────────────────────────────────────────────

    /**
     * A single part within a {@link Multipart} blockstate.
     *
     * <p>Call {@link #when(String, String)} to add conditions. Multiple calls
     * to {@code when} are ANDed together. For OR logic, use {@link #whenOr}.</p>
     */
    public static final class Part {

        private final List<ModelRef>         models;
        private final Map<String, String>    whenAnd = new LinkedHashMap<>();
        private       List<Map<String, String>> whenOr;

        /** Part with a single model. */
        public Part(ModelRef model) {
            this.models = List.of(model);
        }

        /** Part with multiple randomly chosen models. */
        public Part(List<ModelRef> models) {
            this.models = List.copyOf(models);
        }

        /**
         * Adds an AND condition — all added conditions must be true for this part to apply.
         *
         * @param property the block state property name, e.g. {@code "north"}
         * @param value    the required value, e.g. {@code "true"} or {@code "tall"}
         */
        public Part when(String property, String value) {
            whenAnd.put(property, value);
            return this;
        }

        /**
         * Sets an OR condition list — the part applies when any entry matches.
         * Each entry is a map of {@code property → value}.
         * Replaces any AND conditions already set.
         *
         * @param conditions list of property-value maps; any matching entry triggers the part
         */
        public Part whenOr(List<Map<String, String>> conditions) {
            this.whenOr = new ArrayList<>(conditions);
            this.whenAnd.clear();
            return this;
        }

        JsonObject toJson() {
            JsonObject obj = new JsonObject();

            // apply — single model or array
            if (models.size() == 1) {
                obj.add("apply", models.getFirst().toJson());
            } else {
                JsonArray arr = new JsonArray();
                models.forEach(m -> arr.add(m.toJson()));
                obj.add("apply", arr);
            }

            // when
            if (whenOr != null) {
                JsonObject when = new JsonObject();
                JsonArray  or   = new JsonArray();
                for (Map<String, String> entry : whenOr) {
                    JsonObject cond = new JsonObject();
                    entry.forEach(cond::addProperty);
                    or.add(cond);
                }
                when.add("OR", or);
                obj.add("when", when);
            } else if (!whenAnd.isEmpty()) {
                JsonObject when = new JsonObject();
                whenAnd.forEach(when::addProperty);
                obj.add("when", when);
            }

            return obj;
        }
    }
}
