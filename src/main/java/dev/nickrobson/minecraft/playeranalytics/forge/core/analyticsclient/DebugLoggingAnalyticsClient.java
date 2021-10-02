package dev.nickrobson.minecraft.playeranalytics.forge.core.analyticsclient;

import dev.nickrobson.minecraft.playeranalytics.forge.core.api.AnalyticsClient;
import dev.nickrobson.minecraft.playeranalytics.forge.core.api.interaction.Interaction;
import dev.nickrobson.minecraft.playeranalytics.forge.core.api.interaction.InteractionAttributes;
import dev.nickrobson.minecraft.playeranalytics.forge.core.api.interaction.InteractionPerformerId;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static dev.nickrobson.minecraft.playeranalytics.forge.core.api.InteractionController.toEventName;

/**
 * Logs interactions to the console
 */
public class DebugLoggingAnalyticsClient implements AnalyticsClient {
    private static final Logger LOGGER = LogManager.getLogger(DebugLoggingAnalyticsClient.class);

    private boolean enabled = false;

    @Override
    public void trackInteraction(InteractionPerformerId performerId, Interaction interaction, InteractionAttributes attributes) {
        if (!enabled) {
            return;
        }

        StringBuilder stringBuilder = new StringBuilder();
        attributes.toMap().forEach((key, value) ->
                stringBuilder
                        .append('\n')
                        .append("    ")
                        .append(key)
                        .append(" = ")
                        .append(value));
        String attributesString = stringBuilder.length() == 0
                ? "no attributes"
                : "attributes:" + stringBuilder;
        LOGGER.info("Interaction (name: {}) by performer (ID: {}) with {}", toEventName(interaction), performerId.value(), attributesString);
    }

    public void updateDebugMode(boolean isDebugModeEnabled) {
        if (enabled != isDebugModeEnabled) {
            if (isDebugModeEnabled) {
                LOGGER.info("Enabling debug logging client");
            } else {
                LOGGER.info("Disabling debug logging client");
            }
        }
        this.enabled = isDebugModeEnabled;
    }

}
