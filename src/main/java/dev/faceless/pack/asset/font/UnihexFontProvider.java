package dev.faceless.pack.asset.font;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.List;

/**
 * A Unihex font provider that reads glyph bitmaps from a GNU Unifont {@code .hex} archive.
 *
 * <p>The archive must be a {@code .zip} containing one or more {@code *.hex} files.</p>
 *
 * @param hexFile       resource location of the {@code .zip} archive
 * @param sizeOverrides per-codepoint-range glyph width overrides (may be empty)
 */
public record UnihexFontProvider(String hexFile, List<SizeOverride> sizeOverrides)
        implements FontProvider {

    @Override
    public JsonObject toJson() {
        JsonObject obj = new JsonObject();
        obj.addProperty("type", "unihex");
        obj.addProperty("hex_file", hexFile);

        if (!sizeOverrides.isEmpty()) {
            JsonArray arr = new JsonArray();
            for (SizeOverride o : sizeOverrides) {
                JsonObject entry = new JsonObject();
                entry.addProperty("from",  o.from());
                entry.addProperty("to",    o.to());
                entry.addProperty("left",  o.left());
                entry.addProperty("right", o.right());
                arr.add(entry);
            }
            obj.add("size_overrides", arr);
        }

        return obj;
    }

    /**
     * A left/right bearing override applied to a contiguous Unicode codepoint range.
     *
     * @param from  first character in the range (single-char string)
     * @param to    last character in the range (single-char string)
     * @param left  left bearing in pixels
     * @param right right bearing in pixels
     */
    public record SizeOverride(String from, String to, int left, int right) {}
}
