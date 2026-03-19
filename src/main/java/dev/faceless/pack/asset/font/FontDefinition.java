package dev.faceless.pack.asset.font;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a single font JSON file under {@code assets/<ns>/font/}.
 *
 * <p>A font definition is an ordered list of {@link FontProvider} entries.
 * Minecraft uses the first provider that can supply a glyph for a given character.</p>
 *
 * <pre>{@code
 * ns.fonts().define("default")
 *     .provider(new BitmapFontProvider("mymod:font/icons.png", 8, 7, List.of("\uE000\uE001")))
 *     .provider(new SpaceFontProvider(Map.of("\uF001", -8)));
 * }</pre>
 */
public final class FontDefinition {

    private final List<FontProvider> providers = new ArrayList<>();

    /** Appends a provider to this font definition. */
    public FontDefinition provider(FontProvider provider) {
        providers.add(provider);
        return this;
    }

    public JsonObject toJson() {
        JsonObject root = new JsonObject();
        JsonArray  arr  = new JsonArray();
        providers.stream().map(FontProvider::toJson).forEach(arr::add);
        root.add("providers", arr);
        return root;
    }
}
