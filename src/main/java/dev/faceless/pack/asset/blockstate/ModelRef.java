package dev.faceless.pack.asset.blockstate;

import com.google.gson.JsonObject;

/**
 * A reference to a model file within a blockstate definition, with optional
 * rotation, uvlock, and random weight properties.
 *
 * <pre>{@code
 * // Plain reference
 * new ModelRef("mymod:block/ruby_block")
 *
 * // Rotated, texture locked
 * new ModelRef("mymod:block/ruby_slab").y(90).uvlock(true)
 *
 * // Weighted random variant
 * new ModelRef("mymod:block/ruby_block").weight(2)
 * }</pre>
 *
 */
public final class ModelRef {

    private final String model;
    private int     x;
    private int     y;
    private boolean uvlock;
    private int     weight = 1;

    /**
     * @param model resource location of the model, e.g. {@code "mymod:block/ruby_block"}
     */
    public ModelRef(String model) {
        this.model = model;
    }

    /** Rotation around the X axis in increments of 90°. */
    public ModelRef x(int degrees) {
        this.x = degrees;
        return this;
    }

    /** Rotation around the Y axis in increments of 90°. */
    public ModelRef y(int degrees) {
        this.y = degrees;
        return this;
    }

    /**
     * Locks the texture rotation so it does not rotate with the block.
     * Only relevant when {@link #x} or {@link #y} is non-zero.
     */
    public ModelRef uvlock(boolean value) {
        this.uvlock = value;
        return this;
    }

    /**
     * Relative probability weight for random variant selection.
     * Defaults to {@code 1}. Only relevant when multiple {@link ModelRef}s
     * are used for the same variant.
     */
    public ModelRef weight(int weight) {
        this.weight = weight;
        return this;
    }

    public JsonObject toJson() {
        JsonObject obj = new JsonObject();
        obj.addProperty("model", model);
        if (x != 0)      obj.addProperty("x", x);
        if (y != 0)      obj.addProperty("y", y);
        if (uvlock)      obj.addProperty("uvlock", true);
        if (weight != 1) obj.addProperty("weight", weight);
        return obj;
    }
}
