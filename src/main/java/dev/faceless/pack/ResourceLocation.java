package dev.faceless.pack;

import java.util.Objects;

/**
 * An immutable Minecraft resource location in the form {@code namespace:path}.
 *
 * <p>Parsing a plain {@code "path"} with no colon defaults the namespace to {@code "minecraft"}.</p>
 */
public final class ResourceLocation {

    private final String namespace;
    private final String path;

    public ResourceLocation(String namespace, String path) {
        this.namespace = Objects.requireNonNull(namespace, "namespace");
        this.path      = Objects.requireNonNull(path,      "path");
    }

    /**
     * Parses {@code "namespace:path"} or a bare {@code "path"} (defaults to {@code minecraft}).
     */
    public static ResourceLocation of(String location) {
        int colon = location.indexOf(':');
        if (colon < 0) return new ResourceLocation("minecraft", location);
        return new ResourceLocation(location.substring(0, colon), location.substring(colon + 1));
    }

    public String namespace() { return namespace; }
    public String path()      { return path; }

    @Override public String toString()  { return namespace + ":" + path; }
    @Override public int    hashCode()  { return Objects.hash(namespace, path); }

    @Override
    public boolean equals(Object o) {
        return o instanceof ResourceLocation r
                && namespace.equals(r.namespace)
                && path.equals(r.path);
    }
}
