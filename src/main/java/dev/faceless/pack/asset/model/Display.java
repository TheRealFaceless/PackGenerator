package dev.faceless.pack.asset.model;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * A display transform (rotation, translation, scale) applied in a specific {@link DisplaySlot}.
 *
 * <pre>{@code
 * model.display(DisplaySlot.GUI,
 *     new Display().rotation(30, 225, 0).scale(0.625f, 0.625f, 0.625f));
 * }</pre>
 */
public final class Display {

    private float[] rotation;
    private float[] translation;
    private float[] scale;

    public Display rotation(float x, float y, float z) {
        this.rotation = new float[]{x, y, z};
        return this;
    }

    public Display translation(float x, float y, float z) {
        this.translation = new float[]{x, y, z};
        return this;
    }

    public Display scale(float x, float y, float z) {
        this.scale = new float[]{x, y, z};
        return this;
    }

    JsonObject toJson() {
        JsonObject obj = new JsonObject();
        if (rotation    != null) obj.add("rotation",    vec3(rotation));
        if (translation != null) obj.add("translation", vec3(translation));
        if (scale       != null) obj.add("scale",       vec3(scale));
        return obj;
    }

    private static JsonArray vec3(float[] v) {
        JsonArray a = new JsonArray();
        for (float f : v) a.add(f);
        return a;
    }
}
