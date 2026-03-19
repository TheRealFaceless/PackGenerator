package dev.faceless.pack.meta;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * Represents the {@code pack.mcmeta} file at the root of a resource pack.
 *
 * <p>Since Minecraft 1.21.9 (snapshot 25w31a), {@code pack.mcmeta} uses {@code min_format}
 * and {@code max_format} instead of the old single {@code pack_format} field.
 * Use {@link #FORMAT_1_21_11} for the current latest version.</p>
 *
 * <p>Quick construction for a single-version pack targeting 1.21.11:</p>
 * <pre>{@code
 * PackMeta meta = PackMeta.of("My Pack");
 * }</pre>
 *
 * <p>To support a range of versions:</p>
 * <pre>{@code
 * PackMeta meta = new PackMeta("My Pack", FORMAT_1_21_9, FORMAT_1_21_11);
 * }</pre>
 */
public final class PackMeta {

    /** Resource pack format number for Minecraft 1.21.11. */
    public static final int FORMAT_1_21_11 = 75;

    /** Resource pack format number for Minecraft 1.21.9. */
    public static final int FORMAT_1_21_9 = 68;

    /** Resource pack format number for Minecraft 1.21.4 (last version using old pack_format). */
    public static final int FORMAT_1_21_4 = 46;

    private final String description;
    private final int    minFormat;
    private final int    maxFormat;

    /**
     * Creates a {@code pack.mcmeta} targeting a range of pack format versions.
     *
     * @param description human-readable description shown in the pack menu
     * @param minFormat   minimum supported pack format (inclusive)
     * @param maxFormat   maximum supported pack format (inclusive)
     */
    public PackMeta(String description, int minFormat, int maxFormat) {
        this.description = description;
        this.minFormat   = minFormat;
        this.maxFormat   = maxFormat;
    }

    /**
     * Convenience factory — creates a {@code pack.mcmeta} targeting only 1.21.11.
     *
     * @param description human-readable description shown in the pack menu
     */
    public static PackMeta of(String description) {
        return new PackMeta(description, FORMAT_1_21_11, FORMAT_1_21_11);
    }

    public String getDescription() { return description; }
    public int    getMinFormat()   { return minFormat; }
    public int    getMaxFormat()   { return maxFormat; }

    /**
     * Serialises to the correct JSON structure.
     * Packs targeting only 1.21.9+ use {@code min_format}/{@code max_format}.
     * Packs that also need to support 1.21.8 and below additionally write the
     * legacy {@code pack_format} and {@code supported_formats} fields.
     */
    public JsonObject toJson() {
        JsonObject root = new JsonObject();
        JsonObject pack = new JsonObject();

        pack.addProperty("description", description);

        if (minFormat >= FORMAT_1_21_9) {
            // Modern format (1.21.9+): min_format / max_format only
            pack.addProperty("min_format", minFormat);
            pack.addProperty("max_format", maxFormat);
        } else {
            // Legacy + modern: include pack_format for old clients and min/max for new ones
            pack.addProperty("pack_format", minFormat);
            JsonArray supported = new JsonArray();
            supported.add(minFormat);
            supported.add(maxFormat);
            pack.add("supported_formats", supported);
            pack.addProperty("min_format", minFormat);
            pack.addProperty("max_format", maxFormat);
        }

        root.add("pack", pack);
        return root;
    }
}
