package dev.faceless.pack;

import dev.faceless.pack.asset.item.ItemModel;
import dev.faceless.pack.meta.PackMeta;
import dev.faceless.pack.util.TextureBuilder;

import java.awt.*;
import java.nio.file.Path;

public class Main {

    public static void main(String[] args) throws Exception {
        TexturePack pack = new TexturePack("Ruby Coal Ore", PackMeta.of("Ruby Coal Ore"));
        Namespace mc = pack.namespace("minecraft");

        mc.textures().add("block/coal_ore",
                TextureBuilder.of(16, 16)
                        .fill(new Color(125, 125, 125))
                        .valueNoise(0.4f, new Color(100, 100, 100), new Color(150, 150, 150), 1L)
                        .oreSpots(14, new Color(200, 20, 20), 2L)
                        .oreSpots(5,  new Color(255, 80, 80), 3L)
                        .border(1, new Color(80, 80, 80))
                        .build()
        );

        mc.textures().add("test/test_stuff",
                TextureBuilder.of(64, 5)
                        .drawRect(0, 0, 64, 1, Color.BLACK)
                        .drawRect(0, 1, 64, 1, Color.RED)
                        .drawRect(0, 2, 64, 1, Color.RED)
                        .drawRect(0, 3, 64, 1, Color.RED)
                        .drawRect(0, 4, 64, 1, Color.BLACK)
                        .build()
        );

        mc.models().block("coal_ore")
                .parent("block/cube_all")
                .texture("all", "minecraft:block/coal_ore");

        mc.blockstates().simple("coal_ore", "minecraft:block/coal_ore");

        mc.items().define("coal_ore", ItemModel.simple("minecraft:item/coal_ore"));
        mc.models().item("coal_ore").parent("minecraft:block/coal_ore");

        Path output = Path.of("output", "Ruby Coal Ore");
        pack.export(output);
    }
}