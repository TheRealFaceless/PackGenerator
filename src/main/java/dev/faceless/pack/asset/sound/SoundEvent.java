package dev.faceless.pack.asset.sound;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.List;

public final class SoundEvent {
    private String category = "record"; // Default to record for music streams
    private final List<SoundEntry> sounds = new ArrayList<>();

    public SoundEvent category(String category) {
        this.category = category;
        return this;
    }

    public SoundEvent addSound(SoundEntry sound) {
        this.sounds.add(sound);
        return this;
    }

    public SoundEvent addSound(String name, boolean stream) {
        this.sounds.add(new SoundEntry(name).stream(stream));
        return this;
    }

    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        json.addProperty("category", category);
        
        JsonArray array = new JsonArray();
        for (SoundEntry sound : sounds) {
            array.add(sound.toJson());
        }
        json.add("sounds", array);
        
        return json;
    }
}