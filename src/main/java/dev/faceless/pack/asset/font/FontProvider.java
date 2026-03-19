package dev.faceless.pack.asset.font;

import com.google.gson.JsonObject;

/**
 * A single provider entry within a {@link FontDefinition}.
 *
 * <p>Sealed — only the four built-in provider types are permitted:
 * {@link BitmapFontProvider}, {@link TrueTypeFontProvider},
 * {@link UnihexFontProvider}, {@link SpaceFontProvider}.</p>
 */
public sealed interface FontProvider
        permits BitmapFontProvider, TrueTypeFontProvider, UnihexFontProvider, SpaceFontProvider {

    JsonObject toJson();
}
