package dev.faceless.pack.asset.font;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.List;

/**
 * A TrueType/OpenType font provider rendering glyphs from a {@code .ttf} or {@code .otf} file.
 *
 * <pre>{@code
 * new TrueTypeFontProvider("mymod:font/myfont.ttf", new float[]{0f, 0f}, 11f, 4f, List.of())
 * }</pre>
 *
 * @param file       resource location of the font file including extension, e.g. {@code "mymod:font/myfont.ttf"}
 * @param shift      [x, y] pixel shift applied to every glyph
 * @param size       point size at which the font is rendered
 * @param oversample supersampling multiplier — higher values improve quality at large sizes
 * @param skip       characters or unicode ranges to exclude from this provider
 */
public record TrueTypeFontProvider(
        String       file,
        float[]      shift,
        float        size,
        float        oversample,
        List<String> skip
) implements FontProvider {

    @Override
    public JsonObject toJson() {
        JsonObject obj = new JsonObject();
        obj.addProperty("type", "ttf");
        obj.addProperty("file", file);

        JsonArray shiftArr = new JsonArray();
        shiftArr.add(shift[0]);
        shiftArr.add(shift[1]);
        obj.add("shift", shiftArr);

        obj.addProperty("size", size);
        obj.addProperty("oversample", oversample);

        if (!skip.isEmpty()) {
            JsonArray skipArr = new JsonArray();
            skip.forEach(skipArr::add);
            obj.add("skip", skipArr);
        }

        return obj;
    }
}
