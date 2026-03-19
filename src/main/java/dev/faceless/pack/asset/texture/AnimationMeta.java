package dev.faceless.pack.asset.texture;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Animation metadata written alongside an animated texture as a {@code .png.mcmeta} file.
 *
 * <p>Frames are stacked vertically in the PNG. When no explicit frames are added,
 * Minecraft cycles through all frames in order at the configured {@link #frametime}.</p>
 *
 * <pre>{@code
 * textures.add("block/fire", fireImage)
 *         .animation(new AnimationMeta().frametime(2).interpolate(true));
 * }</pre>
 */
public final class AnimationMeta {

    private boolean           interpolate = false;
    private int               frametime   = 1;
    private int               width       = -1;
    private int               height      = -1;
    private final List<Frame> frames      = new ArrayList<>();

    /** Whether to smoothly interpolate between frames. */
    public AnimationMeta interpolate(boolean value) {
        this.interpolate = value;
        return this;
    }

    /** Default ticks displayed per frame (default: {@code 1}). */
    public AnimationMeta frametime(int ticks) {
        this.frametime = ticks;
        return this;
    }

    /** Override frame width in pixels (optional). */
    public AnimationMeta width(int pixels) {
        this.width = pixels;
        return this;
    }

    /** Override frame height in pixels (optional). */
    public AnimationMeta height(int pixels) {
        this.height = pixels;
        return this;
    }

    /** Appends a frame by zero-based index using the default frametime. */
    public AnimationMeta frame(int index) {
        frames.add(new Frame(index, -1));
        return this;
    }

    /**
     * Appends a frame by index with an explicit duration.
     *
     * @param index zero-based frame index in the stacked PNG
     * @param time  duration in ticks
     */
    public AnimationMeta frame(int index, int time) {
        frames.add(new Frame(index, time));
        return this;
    }

    public JsonObject toJson() {
        JsonObject root = new JsonObject();
        JsonObject anim = new JsonObject();

        if (interpolate)    anim.addProperty("interpolate", true);
        if (frametime != 1) anim.addProperty("frametime", frametime);
        if (width  > 0)     anim.addProperty("width",  width);
        if (height > 0)     anim.addProperty("height", height);

        if (!frames.isEmpty()) {
            JsonArray arr = new JsonArray();
            for (Frame f : frames) {
                if (f.time() < 0) {
                    arr.add(f.index());
                } else {
                    JsonObject fo = new JsonObject();
                    fo.addProperty("index", f.index());
                    fo.addProperty("time",  f.time());
                    arr.add(fo);
                }
            }
            anim.add("frames", arr);
        }

        root.add("animation", anim);
        return root;
    }

    private record Frame(int index, int time) {}
}
