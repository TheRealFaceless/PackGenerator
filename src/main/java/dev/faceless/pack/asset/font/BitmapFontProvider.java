package dev.faceless.pack.asset.font;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.List;

/**
 * A bitmap font provider mapping characters to regions of a texture atlas PNG.
 *
 * <p>Each string in {@code chars} represents one horizontal row of glyphs in the texture,
 * read left-to-right. The texture is divided evenly by the number of characters per row.</p>
 *
 * <pre>{@code
 * new BitmapFontProvider("mymod:font/icons.png", 8, 7, List.of("\uE000\uE001\uE002"))
 * }</pre>
 *
 * @param file    resource location of the source PNG including extension, e.g. {@code "mymod:font/icons.png"}
 * @param height  rendered glyph height in pixels
 * @param ascent  vertical shift of the glyph relative to the text baseline
 * @param chars   rows of characters; each string maps to one horizontal row in the texture
 */
public record BitmapFontProvider(String file, int height, int ascent, List<String> chars)
        implements FontProvider {

    @Override
    public JsonObject toJson() {
        JsonObject obj = new JsonObject();
        obj.addProperty("type", "bitmap");
        obj.addProperty("file", file);
        obj.addProperty("height", height);
        obj.addProperty("ascent", ascent);
        JsonArray rows = new JsonArray();
        chars.forEach(rows::add);
        obj.add("chars", rows);
        return obj;
    }
}
