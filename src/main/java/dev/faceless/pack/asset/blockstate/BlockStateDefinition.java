package dev.faceless.pack.asset.blockstate;

import com.google.gson.JsonObject;

/**
 * The root of a blockstate JSON file. There are two mutually exclusive formats:
 *
 * <ul>
 *   <li>{@link Variants} — maps state strings like {@code "facing=north"} to models</li>
 *   <li>{@link Multipart} — assembles a block from layered conditional model parts</li>
 * </ul>
 *
 * <p>Use {@link Variants} for simple blocks with a finite set of named states.
 * Use {@link Multipart} for connective blocks like fences, walls, and glass panes
 * where models are additive.</p>
 */
public sealed interface BlockStateDefinition permits Variants, Multipart {

    JsonObject toJson();
}
