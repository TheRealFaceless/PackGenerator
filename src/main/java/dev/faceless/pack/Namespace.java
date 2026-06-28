package dev.faceless.pack;

import dev.faceless.pack.asset.*;

import java.io.IOException;
import java.nio.file.Path;

/**
 * A namespace within the {@code assets/} directory (e.g. {@code minecraft}, {@code mymod}).
 *
 * <p>Each namespace owns independent registries for blockstates, models, textures,
 * fonts, and item definitions.</p>
 */
public final class Namespace {

    private final String               id;
    private final BlockStateRegistry   blockstates;
    private final ModelRegistry        models;
    private final TextureRegistry      textures;
    private final FontRegistry         fonts;
    private final ItemRegistry         items;
    private final SoundRegistry        sounds;

    Namespace(String id) {
        this.id          = id;
        this.blockstates = new BlockStateRegistry();
        this.models      = new ModelRegistry();
        this.textures    = new TextureRegistry();
        this.fonts       = new FontRegistry();
        this.items       = new ItemRegistry();
        this.sounds      = new SoundRegistry();
    }

    public String getId() { return id; }

    /**
     * Blockstate definitions under {@code assets/<ns>/blockstates/}.
     * Required for every block that renders in the world.
     */
    public BlockStateRegistry blockstates() { return blockstates; }

    /** Block and item model JSON files under {@code assets/<ns>/models/}. */
    public ModelRegistry models()           { return models; }

    /** PNG textures (and optional animation mcmeta) under {@code assets/<ns>/textures/}. */
    public TextureRegistry textures()       { return textures; }

    /** Font provider definitions under {@code assets/<ns>/font/}. */
    public FontRegistry fonts()             { return fonts; }

    /**
     * Item model definitions under {@code assets/<ns>/items/}.
     * Required for every item whose texture you want to override (1.21.4+).
     */
    public ItemRegistry items()             { return items; }

    /** Sound definitions under assets/<ns>/sounds.json and audio assets inside assets/<ns>/sounds/ */
    public SoundRegistry sounds() { return sounds; }

    void export(Path namespaceRoot) throws IOException {
        blockstates.export(namespaceRoot.resolve("blockstates"));
        models.export(namespaceRoot.resolve("models"));
        textures.export(namespaceRoot.resolve("textures"));
        fonts.export(namespaceRoot.resolve("font"));
        items.export(namespaceRoot.resolve("items"));
        sounds.export(namespaceRoot);
    }
}
