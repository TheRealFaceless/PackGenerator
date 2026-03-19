package dev.faceless.pack.asset.model;

/** All valid slots where a model's {@link Display} transform can be overridden. */
public enum DisplaySlot {

    THIRDPERSON_RIGHTHAND("thirdperson_righthand"),
    THIRDPERSON_LEFTHAND ("thirdperson_lefthand"),
    FIRSTPERSON_RIGHTHAND("firstperson_righthand"),
    FIRSTPERSON_LEFTHAND ("firstperson_lefthand"),
    GUI                  ("gui"),
    HEAD                 ("head"),
    GROUND               ("ground"),
    FIXED                ("fixed"),
    /** Introduced in 1.21.4 — used when an item is displayed on a shelf. */
    ON_SHELF             ("on_shelf");

    private final String key;

    DisplaySlot(String key) { this.key = key; }

    public String key() { return key; }
}
