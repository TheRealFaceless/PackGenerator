package dev.faceless.pack.asset.sound;

import com.google.gson.JsonObject;

public final class SoundEntry {
    private final String name;
    private boolean stream = false;

    public SoundEntry(String name) {
        this.name = name;
    }

    public SoundEntry stream(boolean stream) {
        this.stream = stream;
        return this;
    }

    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        json.addProperty("name", name);
        if (stream) {
            json.addProperty("stream", true);
        }
        return json;
    }
}