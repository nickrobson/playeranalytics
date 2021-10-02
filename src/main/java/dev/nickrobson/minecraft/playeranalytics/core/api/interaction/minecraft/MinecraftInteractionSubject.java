package dev.nickrobson.minecraft.playeranalytics.core.api.interaction.minecraft;

/**
 * Represents something from vanilla Minecraft that can be interacted with
 */
public final class MinecraftInteractionSubject {
    private MinecraftInteractionSubject() {}

    /**
     * A block. For example, when an entity places a block.
     */
    public static final String BLOCK = "block";

    /**
     * A container. For example, when a player opens a container.
     */
    public static final String CONTAINER = "container";

    /**
     * A note block. For example, when a note is played on a note block.
     */
    public static final String NOTE_BLOCK = "noteBlock";

    /**
     * An item. For example, when a player crafts an item.
     */
    public static final String ITEM = "item";

    /**
     * A bucket. For example, when a player fills a bucket.
     */
    public static final String BUCKET = "bucket";

    /**
     * A potion. For example, when a potion is brewed.
     */
    public static final String POTION = "potion";

    /**
     * A potion effect. For example, when an entity gaining a potion effect.
     */
    public static final String POTION_EFFECT = "potionEffect";

    /**
     * An entity. For example, when an entity kills another entity.
     */
    public static final String ENTITY = "entity";

    /**
     * A projectile. For example, when a projectile hits something.
     */
    public static final String PROJECTILE = "projectile";

    /**
     * An arrow. For example, when an arrow is fired from a bow.
     */
    public static final String ARROW = "arrow";

    /**
     * An animal. For example, when an animal is tamed.
     */
    public static final String ANIMAL = "animal";

    /**
     * A player. For example, when a player kills a player.
     */
    public static final String PLAYER = "player";

    /**
     * An advancement. For example, when a player gains an advancement.
     */
    public static final String ADVANCEMENT = "advancement";

    /**
     * A village siege. For example, when a player triggers a siege.
     */
    public static final String VILLAGE_SIEGE = "villageSiege";
}
