package dev.nickrobson.minecraft.playeranalytics.core.api;

import dev.nickrobson.minecraft.playeranalytics.core.api.interaction.Interaction;
import dev.nickrobson.minecraft.playeranalytics.core.api.interaction.InteractionAttributes;
import dev.nickrobson.minecraft.playeranalytics.core.api.interaction.InteractionPerformerId;

/**
 * Represents a client for an analytics service
 */
public interface AnalyticsClient {

    /**
     * Tracks an interaction that has occurred
     *
     * @param performerId the id of what performed the interaction
     * @param interaction the interaction that was performed
     * @param attributes attributes giving more detail about what occurred
     */
    void trackInteraction(InteractionPerformerId performerId, Interaction interaction, InteractionAttributes attributes);

}
