package dev.faceless.pack.asset;

import dev.faceless.pack.TexturePack;
import dev.faceless.pack.asset.texture.TextureEntry;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;

/**
 * Registry for texture PNG files (and optional animation mcmeta) under {@code assets/<ns>/textures/}.
 *
 * <p>Keys are paths relative to {@code textures/}, e.g. {@code "block/stone"} or {@code "item/sword"}.</p>
 */
public final class TextureRegistry {

    private final Map<String, TextureEntry> entries = new LinkedHashMap<>();

    /**
     * Registers a texture from a {@link BufferedImage}.
     *
     * @param path  path relative to {@code textures/}, e.g. {@code "block/stone"}
     * @param image source image; written as PNG
     * @return the {@link TextureEntry} for optional animation chaining
     */
    public TextureEntry add(String path, BufferedImage image) {
        TextureEntry entry = new TextureEntry(image);
        entries.put(path, entry);
        return entry;
    }

    /**
     * Registers a texture by reading a PNG from disk.
     *
     * @param path      path relative to {@code textures/}
     * @param imagePath source PNG file on disk
     */
    public TextureEntry add(String path, Path imagePath) throws IOException {
        return add(path, ImageIO.read(imagePath.toFile()));
    }

    public void export(Path texturesRoot) throws IOException {
        for (var entry : entries.entrySet()) {
            Path target = texturesRoot.resolve(entry.getKey() + ".png");
            Files.createDirectories(target.getParent());
            ImageIO.write(entry.getValue().getImage(), "PNG", target.toFile());
            if (entry.getValue().hasAnimation()) {
                Files.writeString(
                        Path.of(target + ".mcmeta"),
                        TexturePack.GSON.toJson(entry.getValue().getAnimation().toJson())
                );
            }
        }
    }
}
