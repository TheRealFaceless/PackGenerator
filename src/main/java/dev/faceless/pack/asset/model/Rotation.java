package dev.faceless.pack.asset.model;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * Rotation applied to a {@link ModelElement} around a single axis.
 *
 * <p>Valid angles are {@code -45, -22.5, 0, 22.5, 45}.</p>
 */
public final class Rotation {

    public enum Axis { X, Y, Z }

    private final float[] origin;
    private final Axis    axis;
    private final float   angle;
    private final boolean rescale;

    /**
     * @param origin  pivot point [x, y, z] in model space (0–16)
     * @param axis    axis to rotate around
     * @param angle   degrees; must be one of -45, -22.5, 0, 22.5, 45
     * @param rescale whether to scale faces to fill gaps introduced by rotation
     */
    public Rotation(float[] origin, Axis axis, float angle, boolean rescale) {
        this.origin  = origin;
        this.axis    = axis;
        this.angle   = angle;
        this.rescale = rescale;
    }

    public Rotation(float[] origin, Axis axis, float angle) {
        this(origin, axis, angle, false);
    }

    JsonObject toJson() {
        JsonObject obj  = new JsonObject();
        JsonArray  orig = new JsonArray();
        for (float v : origin) orig.add(v);
        obj.add("origin", orig);
        obj.addProperty("axis", axis.name().toLowerCase());
        obj.addProperty("angle", angle);
        if (rescale) obj.addProperty("rescale", true);
        return obj;
    }
}
