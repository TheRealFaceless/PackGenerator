package dev.faceless.pack.asset.model;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * A single face on a {@link ModelElement}.
 *
 * <p>The texture reference resolves against the model's {@code textures} map.
 * Supply the variable name with or without the leading {@code #}:</p>
 * <pre>{@code
 * new Face("all")   // → "#all"
 * new Face("#all")  // → "#all"
 * }</pre>
 */
public final class Face {

    private final String               texture;
    private float[]                    uv;
    private int                        rotation;
    private Integer                    tintindex;
    private ModelElement.Direction     cullface;

    public Face(String texture) {
        this.texture = texture.startsWith("#") ? texture : "#" + texture;
    }

    /**
     * Explicit UV mapping in texture space (0–16).
     *
     * @param x1 left
     * @param y1 top
     * @param x2 right
     * @param y2 bottom
     */
    public Face uv(float x1, float y1, float x2, float y2) {
        this.uv = new float[]{x1, y1, x2, y2};
        return this;
    }

    /** Texture rotation in 90° increments (0, 90, 180, 270). */
    public Face rotation(int degrees) {
        this.rotation = degrees;
        return this;
    }

    /**
     * Tint index for biome-recoloured faces (e.g. grass uses {@code 0}).
     * {@code -1} disables tinting.
     */
    public Face tintindex(int index) {
        this.tintindex = index;
        return this;
    }

    /** Adjacent face that must be solid for this face to be culled (render optimisation). */
    public Face cullface(ModelElement.Direction dir) {
        this.cullface = dir;
        return this;
    }

    JsonObject toJson() {
        JsonObject obj = new JsonObject();
        if (uv != null) {
            JsonArray arr = new JsonArray();
            for (float v : uv) arr.add(v);
            obj.add("uv", arr);
        }
        obj.addProperty("texture", texture);
        if (cullface  != null) obj.addProperty("cullface",  cullface.name().toLowerCase());
        if (rotation  != 0)    obj.addProperty("rotation",  rotation);
        if (tintindex != null) obj.addProperty("tintindex", tintindex);
        return obj;
    }
}
