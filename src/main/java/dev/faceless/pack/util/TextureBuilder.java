package dev.faceless.pack.util;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * Fluent utility for generating {@link BufferedImage} textures programmatically.
 *
 * <p>Every method mutates and returns {@code this} so calls can be chained.
 * Call {@link #build()} to retrieve the finished image.</p>
 *
 * <pre>{@code
 * // Simple solid colour
 * BufferedImage red = TextureBuilder.of(16, 16).fill(Color.RED).build();
 *
 * // Stone-like noisy grey with a dark border
 * BufferedImage stone = TextureBuilder.of(16, 16)
 *     .fill(new Color(130, 130, 130))
 *     .noise(0.18f, new Color(100, 100, 100), new Color(160, 160, 160), 42L)
 *     .border(1, new Color(80, 80, 80))
 *     .build();
 *
 * // Glowing blue gem
 * BufferedImage gem = TextureBuilder.of(16, 16)
 *     .fill(Color.BLACK)
 *     .ellipse(2, 2, 12, 12, new Color(0, 80, 200), true)
 *     .ellipse(4, 4, 8,  8,  new Color(80, 160, 255), true)
 *     .ellipse(5, 5, 3,  3,  new Color(200, 230, 255), true)
 *     .build();
 * }</pre>
 */
public final class TextureBuilder {

    private final BufferedImage img;
    private final Graphics2D    g;

    // ── Construction ──────────────────────────────────────────────────────────

    private TextureBuilder(int width, int height) {
        img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        g   = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        // Start fully transparent
        g.setColor(new Color(0, 0, 0, 0));
        g.fillRect(0, 0, width, height);
    }

    /** Creates a new builder for a texture of the given dimensions. */
    public static TextureBuilder of(int width, int height) {
        return new TextureBuilder(width, height);
    }

    /** Creates a new builder pre-filled with a solid colour. */
    public static TextureBuilder of(int width, int height, Color fill) {
        return new TextureBuilder(width, height).fill(fill);
    }

    // ── Simple fills ──────────────────────────────────────────────────────────

    /** Fills the entire texture with a solid colour. */
    public TextureBuilder fill(Color color) {
        g.setColor(color);
        g.fillRect(0, 0, img.getWidth(), img.getHeight());
        return this;
    }

    /** Fills a rectangular region with a solid colour. */
    public TextureBuilder fillRect(int x, int y, int w, int h, Color color) {
        g.setColor(color);
        g.fillRect(x, y, w, h);
        return this;
    }

    /** Draws a 1-px outline rectangle (no fill). */
    public TextureBuilder drawRect(int x, int y, int w, int h, Color color) {
        g.setColor(color);
        g.drawRect(x, y, w - 1, h - 1);
        return this;
    }

    // ── Border ────────────────────────────────────────────────────────────────

    /**
     * Draws a solid border around the entire texture.
     *
     * @param thickness border width in pixels
     * @param color     border colour
     */
    public TextureBuilder border(int thickness, Color color) {
        int W = img.getWidth(), H = img.getHeight();
        g.setColor(color);
        // top, bottom, left, right
        g.fillRect(0,           0,           W,         thickness);
        g.fillRect(0,           H - thickness, W,       thickness);
        g.fillRect(0,           0,           thickness, H);
        g.fillRect(W - thickness, 0,         thickness, H);
        return this;
    }

    // ── Gradients ─────────────────────────────────────────────────────────────

    /**
     * Fills the texture with a horizontal gradient from {@code left} to {@code right}.
     */
    public TextureBuilder gradientH(Color left, Color right) {
        int W = img.getWidth(), H = img.getHeight();
        g.setPaint(new GradientPaint(0, 0, left, W, 0, right));
        g.fillRect(0, 0, W, H);
        return this;
    }

    /**
     * Fills the texture with a vertical gradient from {@code top} to {@code bottom}.
     */
    public TextureBuilder gradientV(Color top, Color bottom) {
        int W = img.getWidth(), H = img.getHeight();
        g.setPaint(new GradientPaint(0, 0, top, 0, H, bottom));
        g.fillRect(0, 0, W, H);
        return this;
    }

    /**
     * Fills the texture with a radial gradient from {@code inner} at the centre
     * to {@code outer} at the edges.
     */
    public TextureBuilder gradientRadial(Color inner, Color outer) {
        int W = img.getWidth(), H = img.getHeight();
        float cx = W / 2f, cy = H / 2f;
        float radius = Math.min(cx, cy);
        g.setPaint(new RadialGradientPaint(cx, cy, radius,
                new float[]{0f, 1f}, new Color[]{inner, outer}));
        g.fillRect(0, 0, W, H);
        return this;
    }

    // ── Patterns ──────────────────────────────────────────────────────────────

    /**
     * Draws a checkerboard pattern.
     *
     * @param cellSize size of each square in pixels
     * @param a        colour of even cells
     * @param b        colour of odd cells
     */
    public TextureBuilder checkerboard(int cellSize, Color a, Color b) {
        int W = img.getWidth(), H = img.getHeight();
        for (int y = 0; y < H; y += cellSize) {
            for (int x = 0; x < W; x += cellSize) {
                g.setColor(((x / cellSize + y / cellSize) % 2 == 0) ? a : b);
                g.fillRect(x, y, cellSize, cellSize);
            }
        }
        return this;
    }

    /**
     * Draws evenly-spaced horizontal stripes.
     *
     * @param stripeHeight height of each stripe in pixels
     * @param a            colour of even stripes
     * @param b            colour of odd stripes
     */
    public TextureBuilder stripesH(int stripeHeight, Color a, Color b) {
        int W = img.getWidth(), H = img.getHeight();
        for (int y = 0; y < H; y += stripeHeight) {
            g.setColor(((y / stripeHeight) % 2 == 0) ? a : b);
            g.fillRect(0, y, W, stripeHeight);
        }
        return this;
    }

    /**
     * Draws evenly-spaced vertical stripes.
     *
     * @param stripeWidth width of each stripe in pixels
     * @param a           colour of even stripes
     * @param b           colour of odd stripes
     */
    public TextureBuilder stripesV(int stripeWidth, Color a, Color b) {
        int W = img.getWidth(), H = img.getHeight();
        for (int x = 0; x < W; x += stripeWidth) {
            g.setColor(((x / stripeWidth) % 2 == 0) ? a : b);
            g.fillRect(x, 0, stripeWidth, H);
        }
        return this;
    }

    /**
     * Draws a pixel grid overlay — 1px lines at every {@code cellSize} interval.
     *
     * @param cellSize grid spacing in pixels
     * @param color    grid line colour (use alpha for subtlety)
     */
    public TextureBuilder grid(int cellSize, Color color) {
        int W = img.getWidth(), H = img.getHeight();
        g.setColor(color);
        for (int x = 0; x < W; x += cellSize) g.drawLine(x, 0, x, H - 1);
        for (int y = 0; y < H; y += cellSize) g.drawLine(0, y, W - 1, y);
        return this;
    }

    // ── Shapes ────────────────────────────────────────────────────────────────

    /**
     * Draws a filled or outlined ellipse.
     *
     * @param x      left edge
     * @param y      top edge
     * @param w      width
     * @param h      height
     * @param color  shape colour
     * @param filled {@code true} to fill, {@code false} to outline only
     */
    public TextureBuilder ellipse(int x, int y, int w, int h, Color color, boolean filled) {
        g.setColor(color);
        if (filled) g.fill(new Ellipse2D.Float(x, y, w, h));
        else        g.draw(new Ellipse2D.Float(x, y, w - 1, h - 1));
        return this;
    }

    /**
     * Draws a filled or outlined rounded rectangle.
     *
     * @param x       left edge
     * @param y       top edge
     * @param w       width
     * @param h       height
     * @param arcSize corner arc diameter
     * @param color   shape colour
     * @param filled  {@code true} to fill, {@code false} to outline only
     */
    public TextureBuilder roundedRect(int x, int y, int w, int h, int arcSize, Color color, boolean filled) {
        g.setColor(color);
        RoundRectangle2D rr = new RoundRectangle2D.Float(x, y, w, h, arcSize, arcSize);
        if (filled) g.fill(rr);
        else        g.draw(rr);
        return this;
    }

    /**
     * Draws a straight line between two points.
     */
    public TextureBuilder line(int x1, int y1, int x2, int y2, Color color) {
        g.setColor(color);
        g.drawLine(x1, y1, x2, y2);
        return this;
    }

    // ── Noise & texture effects ───────────────────────────────────────────────

    /**
     * Overlays random per-pixel noise, randomly choosing between {@code dark} and
     * {@code light} at each pixel with the given probability.
     *
     * @param density fraction of pixels to affect (0.0–1.0)
     * @param dark    colour used for dark noise pixels
     * @param light   colour used for light noise pixels
     * @param seed    random seed for reproducibility
     */
    public TextureBuilder noise(float density, Color dark, Color light, long seed) {
        Random rng = new Random(seed);
        int W = img.getWidth(), H = img.getHeight();
        for (int y = 0; y < H; y++) {
            for (int x = 0; x < W; x++) {
                if (rng.nextFloat() < density) {
                    g.setColor(rng.nextBoolean() ? dark : light);
                    g.fillRect(x, y, 1, 1);
                }
            }
        }
        return this;
    }

    /**
     * Overlays a simple value-noise pattern (smoother than {@link #noise}).
     * Produces organic blotchy variation suitable for stone, dirt, etc.
     *
     * @param scale  frequency of the noise (higher = more fine-grained)
     * @param dark   colour at noise minima
     * @param light  colour at noise maxima
     * @param seed   random seed
     */
    public TextureBuilder valueNoise(float scale, Color dark, Color light, long seed) {
        int W = img.getWidth(), H = img.getHeight();
        // Build a small lattice and interpolate
        int grid = Math.max(2, (int)(1f / scale));
        Random rng = new Random(seed);
        float[][] lattice = new float[grid + 2][grid + 2];
        for (float[] row : lattice) for (int i = 0; i < row.length; i++) row[i] = rng.nextFloat();

        for (int y = 0; y < H; y++) {
            for (int x = 0; x < W; x++) {
                float fx = (x / (float) W) * (grid);
                float fy = (y / (float) H) * (grid);
                int ix = (int) fx, iy = (int) fy;
                float tx = fx - ix, ty = fy - iy;
                // Bilinear interpolation
                float v = lerp(
                        lerp(lattice[iy][ix], lattice[iy][ix + 1], tx),
                        lerp(lattice[iy + 1][ix], lattice[iy + 1][ix + 1], tx),
                        ty
                );
                Color blended = blend(dark, light, v);
                img.setRGB(x, y, alphaComposite(img.getRGB(x, y), blended.getRGB()));
            }
        }
        return this;
    }

    // ── Ore-style overlay ─────────────────────────────────────────────────────

    /**
     * Paints random ore-vein pixels on top of the existing texture.
     * Mimics the look of Minecraft ore blocks.
     *
     * @param count  number of ore pixels to place
     * @param color  ore pixel colour
     * @param seed   random seed
     */
    public TextureBuilder oreSpots(int count, Color color, long seed) {
        Random rng = new Random(seed);
        int W = img.getWidth(), H = img.getHeight();
        g.setColor(color);
        for (int i = 0; i < count; i++) {
            int x = rng.nextInt(W - 1);
            int y = rng.nextInt(H - 1);
            // Place a small 1–2px cluster
            int size = rng.nextInt(2) + 1;
            g.fillRect(x, y, size, size);
        }
        return this;
    }

    // ── Compositing ───────────────────────────────────────────────────────────

    /**
     * Draws another image on top of this texture at the given offset.
     *
     * @param src source image
     * @param x   destination x
     * @param y   destination y
     */
    public TextureBuilder overlay(BufferedImage src, int x, int y) {
        g.drawImage(src, x, y, null);
        return this;
    }

    /**
     * Tints every non-transparent pixel of the current texture towards {@code tint}.
     *
     * @param tint   the colour to blend towards
     * @param amount blend factor 0.0 (no change) → 1.0 (full tint colour)
     */
    public TextureBuilder tint(Color tint, float amount) {
        int W = img.getWidth(), H = img.getHeight();
        for (int y = 0; y < H; y++) {
            for (int x = 0; x < W; x++) {
                int argb  = img.getRGB(x, y);
                int alpha = (argb >> 24) & 0xFF;
                if (alpha == 0) continue;
                Color src = new Color(argb, true);
                img.setRGB(x, y, blend(src, tint, amount).getRGB());
            }
        }
        return this;
    }

    /**
     * Multiplies every pixel's alpha by {@code factor}, making the texture
     * more or less transparent.
     *
     * @param factor 0.0 = fully transparent, 1.0 = unchanged
     */
    public TextureBuilder opacity(float factor) {
        int W = img.getWidth(), H = img.getHeight();
        for (int y = 0; y < H; y++) {
            for (int x = 0; x < W; x++) {
                int argb  = img.getRGB(x, y);
                int alpha = (int)(((argb >> 24) & 0xFF) * factor);
                img.setRGB(x, y, (argb & 0x00FFFFFF) | (alpha << 24));
            }
        }
        return this;
    }

    // ── Common presets ────────────────────────────────────────────────────────

    /**
     * Produces a placeholder "missing texture" style magenta/black checkerboard.
     */
    public static BufferedImage missing(int size) {
        return of(size, size)
                .checkerboard(size / 2, new Color(255, 0, 255), Color.BLACK)
                .build();
    }

    /**
     * Produces a flat dummy texture — fully transparent, useful as a placeholder.
     */
    public static BufferedImage transparent(int width, int height) {
        return of(width, height).build();
    }

    /**
     * Produces a simple solid-colour texture — the most common dummy.
     */
    public static BufferedImage solid(int width, int height, Color color) {
        return of(width, height, color).build();
    }

    /**
     * Produces a stone-like grey texture with value noise variation.
     */
    public static BufferedImage stone(int size, long seed) {
        return of(size, size)
                .fill(new Color(125, 125, 125))
                .valueNoise(0.4f, new Color(100, 100, 100), new Color(150, 150, 150), seed)
                .border(1, new Color(80, 80, 80))
                .build();
    }

    /**
     * Produces a dirt-like brown texture with noise variation.
     */
    public static BufferedImage dirt(int size, long seed) {
        return of(size, size)
                .fill(new Color(134, 96, 67))
                .valueNoise(0.5f, new Color(110, 75, 50), new Color(155, 115, 80), seed)
                .noise(0.1f, new Color(90, 60, 40), new Color(160, 120, 85), seed + 1)
                .build();
    }

    /**
     * Produces a simple ore texture — a stone base with coloured ore spots.
     *
     * @param oreColor the colour of the ore pixels
     * @param seed     random seed
     */
    public static BufferedImage ore(int size, Color oreColor, long seed) {
        return of(size, size)
                .fill(new Color(125, 125, 125))
                .valueNoise(0.4f, new Color(100, 100, 100), new Color(150, 150, 150), seed)
                .oreSpots(12, oreColor, seed)
                .border(1, new Color(80, 80, 80))
                .build();
    }

    /**
     * Produces a plank-style texture with horizontal grain lines.
     *
     * @param baseColor  the wood base colour
     * @param grainColor the grain line colour
     * @param seed       random seed
     */
    public static BufferedImage planks(int size, Color baseColor, Color grainColor, long seed) {
        Random rng = new Random(seed);
        TextureBuilder b = of(size, size).fill(baseColor);
        // Horizontal grain lines at random intervals
        for (int y = 1; y < size - 1; y++) {
            if (rng.nextFloat() < 0.25f) b.line(0, y, size - 1, y, grainColor);
        }
        return b.noise(0.08f,
                        new Color(Math.max(0, baseColor.getRed() - 20),
                                  Math.max(0, baseColor.getGreen() - 20),
                                  Math.max(0, baseColor.getBlue() - 20)),
                        new Color(Math.min(255, baseColor.getRed() + 20),
                                  Math.min(255, baseColor.getGreen() + 20),
                                  Math.min(255, baseColor.getBlue() + 20)),
                        seed + 2)
                .build();
    }

    // ── Terminal operation ────────────────────────────────────────────────────

    /**
     * Finalises drawing and returns the finished {@link BufferedImage}.
     * The builder should not be used after calling this.
     */
    public BufferedImage build() {
        g.dispose();
        return img;
    }

    // ── Internal helpers ──────────────────────────────────────────────────────

    private static float lerp(float a, float b, float t) {
        return a + t * (b - a);
    }

    private static Color blend(Color a, Color b, float t) {
        return new Color(
                (int) lerp(a.getRed(),   b.getRed(),   t),
                (int) lerp(a.getGreen(), b.getGreen(), t),
                (int) lerp(a.getBlue(),  b.getBlue(),  t),
                (int) lerp(a.getAlpha(), b.getAlpha(), t)
        );
    }

    /** Software alpha-composite {@code src} over {@code dst} (both ARGB ints). */
    private static int alphaComposite(int dst, int src) {
        float sa = ((src >> 24) & 0xFF) / 255f;
        float da = ((dst >> 24) & 0xFF) / 255f;
        float oa = sa + da * (1 - sa);
        if (oa == 0) return 0;
        float r = (((src >> 16) & 0xFF) * sa + ((dst >> 16) & 0xFF) * da * (1 - sa)) / oa;
        float gr= (((src >>  8) & 0xFF) * sa + ((dst >>  8) & 0xFF) * da * (1 - sa)) / oa;
        float b = (( src        & 0xFF) * sa + ( dst        & 0xFF) * da * (1 - sa)) / oa;
        return ((int)(oa * 255) << 24) | ((int) r << 16) | ((int) gr << 8) | (int) b;
    }
}
