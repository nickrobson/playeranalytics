package dev.nickrobson.minecraft.playeranalytics.core.api.interaction.minecraft;

import dev.nickrobson.minecraft.playeranalytics.core.api.interaction.Interaction;
import org.apache.logging.log4j.message.ParameterizedMessage;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Set;

import static dev.nickrobson.minecraft.playeranalytics.core.api.InteractionController.toEventName;

public enum MinecraftInteraction implements Interaction {
    // ------------------
    // Block interactions
    // ------------------

    /**
     * When a player places a block
     */
    BLOCK_PLACED(MinecraftInteractionSubject.BLOCK, "placed"),

    /**
     * When a player breaks a block
     */
    BLOCK_BROKEN(MinecraftInteractionSubject.BLOCK, "broken"),

    /**
     * When a player harvests a block (like a crop)
     */
    BLOCK_HARVESTED(MinecraftInteractionSubject.BLOCK, "harvested"),

    /**
     * When a player tramples a block (like farmland)
     */
    BLOCK_TRAMPLED(MinecraftInteractionSubject.BLOCK, "trampled"),

    /**
     * When a player bonemeals a block
     */
    BLOCK_BONEMEALED(MinecraftInteractionSubject.BLOCK, "bonemealed"),
    /**
     * When a player left-clicks on a block
     * <i>Note: Unused due to causing spam</i>
     */
    BLOCK_LEFT_CLICKED(MinecraftInteractionSubject.BLOCK, "leftClicked"),

    /**
     * When a player right-clicks on a block
     */
    BLOCK_RIGHT_CLICKED(MinecraftInteractionSubject.BLOCK, "rightClicked"),

    // ----------------------
    // Container interactions
    // ----------------------

    /**
     * When a player opens a container
     */
    CONTAINER_OPENED(MinecraftInteractionSubject.CONTAINER, "opened"),

    /**
     * When a player closes a container
     */
    CONTAINER_CLOSED(MinecraftInteractionSubject.CONTAINER, "closed"),

    // -----------------------
    // Note block interactions
    // -----------------------

    /**
     * When a player plays a block
     */
    NOTE_BLOCK_PLAYED(MinecraftInteractionSubject.NOTE_BLOCK, "played"),

    /**
     * When a player changes a note block
     */
    NOTE_BLOCK_CHANGED(MinecraftInteractionSubject.NOTE_BLOCK, "changed"),

    // -----------------
    // Item interactions
    // -----------------

    /**
     * When a player drops an item
     */
    ITEM_DROPPED(MinecraftInteractionSubject.ITEM, "dropped"),

    /**
     * When a player picks up an item
     */
    ITEM_PICKED_UP(MinecraftInteractionSubject.ITEM, "pickedUp"),

    /**
     * When a player crafts an item
     */
    ITEM_CRAFTED(MinecraftInteractionSubject.ITEM, "crafted"),

    /**
     * When a player smelts an item
     */
    ITEM_SMELTED(MinecraftInteractionSubject.ITEM, "smelted"),

    /**
     * When a player repairs an item
     */
    ITEM_REPAIRED(MinecraftInteractionSubject.ITEM, "repaired"),

    /**
     * When a player destroys an item (e.g. by using a tool to completion)
     */
    ITEM_DESTROYED(MinecraftInteractionSubject.ITEM, "destroyed"),

    /**
     * When a player gets an item from fishing
     */
    ITEM_FISHED(MinecraftInteractionSubject.ITEM, "fished"),

    /**
     * When an entity uses an item, such as drawing a bow, eating food, or drinking a potion
     */
    ITEM_USED(MinecraftInteractionSubject.ITEM, "used"),

    /**
     * When a player left-clicks while holding an item (or with an empty hand)
     */
    ITEM_LEFT_CLICKED(MinecraftInteractionSubject.ITEM, "leftClicked"),

    /**
     * When a player right-clicks while holding an item (or with an empty hand)
     */
    ITEM_RIGHT_CLICKED(MinecraftInteractionSubject.ITEM, "rightClicked"),

    // -------------------
    // Bucket interactions
    // -------------------

    /**
     * When a player fills a bucket
     */
    BUCKET_FILLED(MinecraftInteractionSubject.BUCKET, "filled"),

    /**
     * When a player empties a bucket
     */
    BUCKET_EMPTIED(MinecraftInteractionSubject.BUCKET, "emptied"),

    // -------------------
    // Potion interactions
    // -------------------

    /**
     * When a potion finishes brewing in the world
     */
    POTION_BREWED(MinecraftInteractionSubject.POTION, "brewed"),

    /**
     * When a potion is collected from a brewing stand by the player
     */
    POTION_COLLECTED(MinecraftInteractionSubject.POTION, "collected"),

    /**
     * When a potion effect is gained by an entity
     */
    POTION_EFFECT_ADDED(MinecraftInteractionSubject.POTION_EFFECT, "added"),

    /**
     * When a potion effect is removed from an entity
     */
    POTION_EFFECT_REMOVED(MinecraftInteractionSubject.POTION_EFFECT, "removed"),

    /**
     * When a potion effect on an entity expires
     */
    POTION_EFFECT_EXPIRED(MinecraftInteractionSubject.POTION_EFFECT, "expired"),

    // -------------------
    // Entity interactions
    // -------------------

    /**
     * When a player attacks an entity
     */
    ENTITY_ATTACKED(MinecraftInteractionSubject.ENTITY, "attacked"),

    /**
     * When an entity dies
     */
    ENTITY_DIED(MinecraftInteractionSubject.ENTITY, "died"),

    /**
     * When an entity teleports
     */
    ENTITY_TELEPORTED(MinecraftInteractionSubject.ENTITY, "teleported"),

    /**
     * When an entity puts on equipment
     */
    ENTITY_EQUIPPED(MinecraftInteractionSubject.ENTITY, "equipped"),

    /**
     * When an entity heals
     */
    ENTITY_HEALED(MinecraftInteractionSubject.ENTITY, "healed"),

    /**
     * When an entity is damaged
     */
    ENTITY_DAMAGED(MinecraftInteractionSubject.ENTITY, "damaged"),

    /**
     * When an entity is struck by lightning
     */
    ENTITY_STRUCK_BY_LIGHTNING(MinecraftInteractionSubject.ENTITY, "struckByLightning"),

    // -----------------------
    // Projectile interactions
    // -----------------------

    /**
     * When a projectile hits something
     */
    PROJECTILE_IMPACTED(MinecraftInteractionSubject.PROJECTILE, "impacted"),

    /**
     * When an arrow is shot from a bow
     */
    ARROW_LOOSED(MinecraftInteractionSubject.ARROW, "loosed"),

    /**
     * When an arrow is being loaded into a bow
     */
    ARROW_NOCKED(MinecraftInteractionSubject.ARROW, "nocked"),

    // -------------------
    // Animal interactions
    // -------------------

    /**
     * When a player tames an animal
     */
    ANIMAL_TAMED(MinecraftInteractionSubject.ANIMAL, "tamed"),

    /**
     * When a player breeds two animals
     */
    ANIMAL_BRED(MinecraftInteractionSubject.ANIMAL, "bred"),

    // -------------------
    // Player interactions
    // -------------------

    /**
     * When a player connects to the server
     */
    PLAYER_CONNECTED(MinecraftInteractionSubject.PLAYER, "connected"),

    /**
     * When a player disconnects from the server
     */
    PLAYER_DISCONNECTED(MinecraftInteractionSubject.PLAYER, "disconnected"),

    /**
     * When a player sends a chat message on the server
     */
    PLAYER_CHATTED(MinecraftInteractionSubject.PLAYER, "chatted"),

    /**
     * When a player respawns
     */
    PLAYER_RESPAWNED(MinecraftInteractionSubject.PLAYER, "respawned"),

    /**
     * When a player teleports
     */
    PLAYER_CHANGED_DIMENSION(MinecraftInteractionSubject.PLAYER, "changedDimension"),

    /**
     * When a player sleeps
     */
    PLAYER_SLEPT(MinecraftInteractionSubject.PLAYER, "slept"),

    /**
     * When a player wakes up
     */
    PLAYER_WOKE_UP(MinecraftInteractionSubject.PLAYER, "wokeUp"),

    /**
     * When a player's xp changes
     */
    PLAYER_XP_CHANGED(MinecraftInteractionSubject.PLAYER, "xpChanged"),

    /**
     * When a player's xp level changes
     */
    PLAYER_XP_LEVEL_CHANGED(MinecraftInteractionSubject.PLAYER, "xpLevelChanged"),

    /**
     * When a player gains an advancement
     */
    ADVANCEMENT_GAINED(MinecraftInteractionSubject.ADVANCEMENT, "gained"),

    // --------------------------
    // Village siege interactions
    // --------------------------

    /**
     * When a player triggers a village siege
     */
    VILLAGE_SIEGE_TRIGGERED(MinecraftInteractionSubject.VILLAGE_SIEGE, "triggered");

    private final String subject;
    private final String action;

    MinecraftInteraction(String subject, String action) {
        this.subject = subject;
        this.action = action;
    }

    @Override
    @Nonnull
    public String getSubject() {
        return subject;
    }

    @Override
    @Nonnull
    public String getAction() {
        return action;
    }

    // Catch & report duplicate actions in this file at startup
    static {
        Set<String> seenEventNames = new HashSet<>();
        Set<String> duplicates = new HashSet<>();
        for (MinecraftInteraction interaction : values()) {
            String eventName = toEventName(interaction);
            if (!seenEventNames.add(eventName)) {
                duplicates.add(eventName);
            }
        }

        if (!duplicates.isEmpty()) {
            throw new IllegalStateException(
                    ParameterizedMessage.format(
                            "Found duplicates: {}. Please fix them!",
                            duplicates.toArray()
                    )

            );
        }
    }
}
