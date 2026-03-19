package dev.faceless.pack.asset.model;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.EnumMap;
import java.util.Map;

/**
 * A cuboid element within a {@link Model}, defined by two corners in model space (0–16 units).
 *
 * <pre>{@code
 * ModelElement cube = new ModelElement(new float[]{0, 0, 0}, new float[]{16, 16, 16})
 *     .allFaces("all")
 *     .face(Direction.UP, new Face("top").cullface(Direction.UP));
 * }</pre>
 */
public final class ModelElement {

    /** The six face directions of a cuboid. */
    public enum Direction { DOWN, UP, NORTH, SOUTH, WEST, EAST }

    private final float[]              from;
    private final float[]              to;
    private final Map<Direction, Face> faces = new EnumMap<>(Direction.class);
    private Rotation                   rotation;
    private boolean                    shade = true;

    /**
     * @param from origin corner [x, y, z] in model space (0–16)
     * @param to   opposite corner [x, y, z] in model space (0–16)
     */
    public ModelElement(float[] from, float[] to) {
        this.from = from;
        this.to   = to;
    }

    /** Adds or replaces a single face. */
    public ModelElement face(Direction dir, Face face) {
        faces.put(dir, face);
        return this;
    }

    /**
     * Applies the same texture reference to all six faces.
     *
     * @param textureVariable texture variable name without {@code #}, e.g. {@code "all"}
     */
    public ModelElement allFaces(String textureVariable) {
        for (Direction d : Direction.values()) face(d, new Face(textureVariable));
        return this;
    }

    public ModelElement rotation(Rotation rotation) {
        this.rotation = rotation;
        return this;
    }

    /** Whether this element receives directional shading. Default {@code true}. */
    public ModelElement shade(boolean shade) {
        this.shade = shade;
        return this;
    }

    JsonObject toJson() {
        JsonObject obj = new JsonObject();
        obj.add("from", vec3(from));
        obj.add("to",   vec3(to));
        if (!shade)           obj.addProperty("shade", false);
        if (rotation != null) obj.add("rotation", rotation.toJson());
        if (!faces.isEmpty()) {
            JsonObject faceMap = new JsonObject();
            faces.forEach((dir, face) -> faceMap.add(dir.name().toLowerCase(), face.toJson()));
            obj.add("faces", faceMap);
        }
        return obj;
    }

    private static JsonArray vec3(float[] v) {
        JsonArray a = new JsonArray();
        for (float f : v) a.add(f);
        return a;
    }
}
