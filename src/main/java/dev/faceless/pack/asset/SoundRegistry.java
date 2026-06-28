package dev.faceless.pack.asset;

import com.google.gson.JsonObject;
import dev.faceless.pack.TexturePack;
import dev.faceless.pack.asset.sound.SoundEvent;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

public final class SoundRegistry {

    private final Map<String, SoundEvent> events = new LinkedHashMap<>();
    private final Map<String, Path> physicalFiles = new LinkedHashMap<>();

    /**
     * Define a sound event mappings identifier inside sounds.json.
     */
    public SoundEvent define(String eventId) {
        return events.computeIfAbsent(eventId, k -> new SoundEvent());
    }

    /**
     * Registers a physical sound file (.ogg) from disk to be copied into the pack.
     * @param path The relative target destination path inside assets/<ns>/sounds/ (e.g. "music/my_song")
     * @param fileOnDisk Path pointing to the source .ogg file.
     */
    public void addFile(String path, Path fileOnDisk) {
        physicalFiles.put(path, fileOnDisk);
    }

    public void export(Path namespaceRoot) throws IOException {
        // 1. Export the physical audio files (.ogg)
        if (!physicalFiles.isEmpty()) {
            Path soundsFolder = namespaceRoot.resolve("sounds");
            for (var entry : physicalFiles.entrySet()) {
                // Ensure target path ends with .ogg extension
                String relativePath = entry.getKey().endsWith(".ogg") ? entry.getKey() : entry.getKey() + ".ogg";
                Path targetFile = soundsFolder.resolve(relativePath);
                
                Files.createDirectories(targetFile.getParent());
                Files.copy(entry.getValue(), targetFile, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            }
        }

        // 2. Export the combined sounds.json file
        if (!events.isEmpty()) {
            JsonObject rootJson = new JsonObject();
            for (var entry : events.entrySet()) {
                rootJson.add(entry.getKey(), entry.getValue().toJson());
            }

            Files.createDirectories(namespaceRoot);
            Files.writeString(
                    namespaceRoot.resolve("sounds.json"),
                    TexturePack.GSON.toJson(rootJson)
            );
        }
    }
}