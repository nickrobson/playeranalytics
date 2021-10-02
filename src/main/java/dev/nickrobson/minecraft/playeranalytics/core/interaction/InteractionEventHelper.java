package dev.nickrobson.minecraft.playeranalytics.core.interaction;

import dev.nickrobson.minecraft.playeranalytics.core.api.interaction.InteractionPerformerId;
import dev.nickrobson.minecraft.playeranalytics.core.api.InteractionController;
import dev.nickrobson.minecraft.playeranalytics.core.api.interaction.Interaction;
import dev.nickrobson.minecraft.playeranalytics.core.api.interaction.InteractionAttributes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static dev.nickrobson.minecraft.playeranalytics.core.interaction.InteractionAttributeHelper.getDimensionId;

public final class InteractionEventHelper {
    private InteractionEventHelper() {
    }

    public static void trackEntityInteractionWithActor(
            @Nullable final Entity actor,
            @Nonnull final Interaction interaction
    ) {
        trackEntityInteractionWithActor(actor, interaction, new InteractionAttributes());
    }

    public static void trackEntityInteractionWithActor(
            @Nullable final Entity actor,
            @Nonnull final Interaction interaction,
            @Nonnull final InteractionAttributes attributes
    ) {
        trackEntityInteraction(actor, null, interaction, attributes);
    }

    public static void trackEntityInteractionWithSubject(
            @Nullable final Entity subject,
            @Nonnull final Interaction interaction
    ) {
        trackEntityInteractionWithSubject(subject, interaction, new InteractionAttributes());
    }

    public static void trackEntityInteractionWithSubject(
            @Nullable final Entity subject,
            @Nonnull final Interaction interaction,
            @Nonnull final InteractionAttributes attributes
    ) {
        trackEntityInteraction(null, subject, interaction, attributes);
    }

    public static void trackEntityInteraction(
            @Nullable final Entity actor,
            @Nullable final Entity subject,
            @Nonnull final Interaction interaction
    ) {
        trackEntityInteraction(actor, subject, interaction, new InteractionAttributes());
    }

    public static void trackEntityInteraction(
            @Nullable final Entity actor,
            @Nullable final Entity subject,
            @Nonnull final Interaction interaction,
            @Nonnull final InteractionAttributes attributes
    ) {
        if ((actor == null && subject == null)) {
            return;
        }
        Entity existingEntity = subject != null ? subject : actor;
        if (existingEntity.level.isClientSide())
            return;

        if (actor != null) {
            populateEntityAttributes(actor, attributes, "actor");
        }
        if (subject != null) {
            populateEntityAttributes(subject, attributes, "subject");
        }
        populateLocationAttributes(existingEntity.level, existingEntity.blockPosition(), attributes);

        InteractionController.trackInteraction(
                actor != null ? getEntityPerformerId(actor) : InteractionPerformerId.SERVER_INTERACTION_PERFORMER_ID,
                interaction,
                attributes
        );
    }

    public static void trackServerInteraction(
            @Nonnull Interaction interaction,
            @Nullable InteractionAttributes attributes
    ) {
        InteractionController.trackInteraction(
                InteractionPerformerId.SERVER_INTERACTION_PERFORMER_ID,
                interaction,
                attributes
        );
    }

    public static InteractionPerformerId getEntityPerformerId(@Nonnull Entity entity) {
        return entity instanceof Player
                ? InteractionPerformerId.forPlayerUuid(entity.getStringUUID())
                : InteractionPerformerId.forEntityType(entity.getType().getDescriptionId());
    }

    public static void populateEntityAttributes(@Nonnull Entity entity, @Nonnull InteractionAttributes attributes, @Nonnull String prefix) {
        // Fill player name attribute with the player's name for all interactions involving players
        if (entity instanceof Player) {
            attributes.set(prefix + "PlayerName", InteractionAttributeHelper.getPlayerName((Player) entity));
        }
        attributes.set(prefix + "DisplayName", InteractionAttributeHelper.getEntityDisplayName(entity));
        attributes.set(prefix + "Name", InteractionAttributeHelper.getEntityName(entity));
        attributes.set(prefix + "Type", InteractionAttributeHelper.getEntityType(entity));
    }

    public static void populateLocationAttributes(@Nonnull Level level, @Nonnull BlockPos blockPos, @Nonnull InteractionAttributes attributes) {
        attributes.set("dimension", InteractionAttributeHelper.getDimensionId(level));

        attributes.set("biomeCategory", InteractionAttributeHelper.getBiomeCategory(level, blockPos));
        InteractionAttributeHelper.getBiomeId(level, blockPos).ifPresent(
                biomeId -> attributes.set("biome", biomeId)
        );
    }
}
