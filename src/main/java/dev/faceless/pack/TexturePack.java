package dev.faceless.pack;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.faceless.pack.meta.PackMeta;
import dev.faceless.pack.util.ZipUtil;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;

/**
 * The root object representing a Minecraft resource pack.
 *
 * <p>Create a pack, populate namespaces, then call {@link #export(Path)} to write the
 * full directory structure to disk. The pack name is automatically used as the
 * output directory name inside the given root path.</p>
 *
 * <pre>{@code
 * TexturePack pack = new TexturePack("my_pack", PackMeta.of("My Pack"));
 *
 * Namespace ns = pack.namespace("mymod");
 *
 * // Block: blockstates → models/block + textures/block
 * ns.models().block("my_block").parent("block/cube_all").texture("all", "mymod:block/my_block");
 * ns.textures().add("block/my_block", myBlockImage);
 *
 * // Item: items/ definition + models/item + textures/item
 * ns.items().define("my_sword", ItemModel.simple("mymod:item/my_sword"));
 * ns.models().item("my_sword").parent("item/handheld").texture("layer0", "mymod:item/my_sword");
 * ns.textures().add("item/my_sword", mySwordImage);
 *
 * // Export as folder
 * pack.export(Path.of("./output"));
 *
 * // Export as zip, keeping the folder
 * pack.export(Path.of("./output"), true, false);
 *
 * // Export as zip, deleting the folder afterwards
 * pack.export(Path.of("./output"), true, true);
 * }</pre>
 */
public final class TexturePack {

    /** Shared Gson instance used by all serialisers in this library. */
    public static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .disableHtmlEscaping()
            .create();

    private final String name;
    private final PackMeta meta;
    private final Map<String, Namespace> namespaces = new LinkedHashMap<>();

    public TexturePack(String name, PackMeta meta) {
        this.name = name;
        this.meta = meta;
    }

    /**
     * Returns (or lazily creates) the namespace with the given id.
     *
     * @param id namespace identifier, e.g. {@code "minecraft"} or {@code "mymod"}
     */
    public Namespace namespace(String id) {
        return namespaces.computeIfAbsent(id, Namespace::new);
    }

    public String getName()   { return name; }
    public PackMeta getMeta() { return meta; }

    public Collection<Namespace> getNamespaces() {
        return Collections.unmodifiableCollection(namespaces.values());
    }

    /**
     * Writes the complete resource pack directory structure to {@code root/name}.
     * The directory is created if it does not exist.
     *
     * <p>Equivalent to {@code export(root, false, false)}.</p>
     *
     * @param root parent directory the pack folder will be created inside,
     *             e.g. {@code Path.of("./output")} produces {@code ./output/my_pack/}
     * @throws IOException if the directory cannot be created or any file cannot be written
     */
    public void export(Path root) throws IOException {
        export(root, false, false);
    }

    /**
     * Writes the complete resource pack directory structure to {@code root/name},
     * optionally zipping the result.
     *
     * @param root            parent directory the pack folder will be created inside,
     *                        e.g. {@code Path.of("./output")} produces {@code ./output/my_pack/}
     * @param zip             if {@code true}, the exported folder is compressed into
     *                        {@code root/name.zip} after writing
     * @param deleteAfterZip  if {@code true} (and {@code zip} is {@code true}), the
     *                        unzipped folder is deleted once the ZIP is created;
     *                        ignored when {@code zip} is {@code false}
     * @throws IOException if the directory cannot be created, any file cannot be
     *                     written, or zipping fails
     */
    public void export(Path root, boolean zip, boolean deleteAfterZip) throws IOException {
        Path packDir = root.resolve(name);
        Files.createDirectories(packDir);
        Files.writeString(packDir.resolve("pack.mcmeta"), GSON.toJson(meta.toJson()));

        Path assets = packDir.resolve("assets");
        for (Namespace ns : namespaces.values()) {
            ns.export(assets.resolve(ns.getId()));
        }

        if (zip) {
            Path zipFile = root.resolve(name + ".zip");
            ZipUtil.zipDirectory(packDir.toString(), zipFile.toString(), deleteAfterZip);
        }
    }
}