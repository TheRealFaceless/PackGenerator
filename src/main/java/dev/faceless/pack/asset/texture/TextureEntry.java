package dev.faceless.pack.asset.texture;

import java.awt.image.BufferedImage;

/**
 * A texture entry pairing a {@link BufferedImage} with optional {@link AnimationMeta}.
 *
 * <p>Obtain instances from {@link dev.faceless.pack.asset.TextureRegistry#add} and chain
 * animation configuration if needed:</p>
 *
 * <pre>{@code
 * ns.textures().add("block/lava", lavaImage)
 *              .animation(new AnimationMeta().frametime(3).interpolate(true));
 * }</pre>
 */
public final class TextureEntry {

    private final BufferedImage image;
    private       AnimationMeta animation;

    public TextureEntry(BufferedImage image) {
        this.image = image;
    }

    public TextureEntry animation(AnimationMeta meta) {
        this.animation = meta;
        return this;
    }

    public BufferedImage getImage()     { return image; }
    public AnimationMeta getAnimation() { return animation; }
    public boolean       hasAnimation() { return animation != null; }
}
