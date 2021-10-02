package dev.nickrobson.minecraft.playeranalytics.core.api.interaction;

/**
 * An ID for tracking what thing performed an interaction.
 * <p>
 * Examples:
 * - a player's UUID
 * - the Minecraft server (see {@link #SERVER_INTERACTION_PERFORMER_ID})
 */
public record InteractionPerformerId(String value) {
    /**
     * A special ID representing the Minecraft server
     */
    public static final InteractionPerformerId SERVER_INTERACTION_PERFORMER_ID = new InteractionPerformerId("MINECRAFT_SERVER");

    public static InteractionPerformerId forPlayerUuid(String uuid) {
        return new InteractionPerformerId(uuid);
    }

    public static InteractionPerformerId forEntityType(String entityType) {
        return new InteractionPerformerId("entity:" + entityType);
    }
}
