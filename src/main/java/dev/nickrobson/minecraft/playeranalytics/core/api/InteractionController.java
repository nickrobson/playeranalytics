package dev.nickrobson.minecraft.playeranalytics.core.api;

import dev.nickrobson.minecraft.playeranalytics.core.api.interaction.Interaction;
import dev.nickrobson.minecraft.playeranalytics.core.api.interaction.InteractionAttributes;
import dev.nickrobson.minecraft.playeranalytics.core.api.interaction.InteractionPerformerId;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.message.ParameterizedMessage;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Top-level entrypoint for tracking interactions
 */
public class InteractionController {
    private static final Logger logger = LogManager.getLogger(InteractionController.class);
    private static final List<AnalyticsClient> analyticsClientList = new LinkedList<>();

    private InteractionController() {
    }

    /**
     * Registers a new analytics client
     *
     * @param analyticsClient The analytics client
     */
    public static void registerAnalyticsClient(
            AnalyticsClient analyticsClient
    ) {
        Objects.requireNonNull(analyticsClient, "Analytics client must not be null");

        Optional<AnalyticsClient> existingClientOfSameType = analyticsClientList.stream()
                .filter(client -> Objects.equals(client.getClass().getName(), analyticsClient.getClass().getName()))
                .findAny();
        if (existingClientOfSameType.isPresent()) {
            throw new IllegalArgumentException("Registering 2 analytics clients of the same class is disallowed as it indicates a bug or inefficiency");
        }

        analyticsClientList.add(analyticsClient);
    }

    public static void trackInteraction(
            InteractionPerformerId performerId,
            Interaction interaction,
            InteractionAttributes attributes
    ) {
        analyticsClientList.forEach(analyticsClient -> {
            try {
                analyticsClient.trackInteraction(performerId, interaction, attributes);
            } catch (Exception ex) {
                logger.error(
                        new ParameterizedMessage(
                                "Failed to capture interaction {} by performer (ID: {}) with analytics client {} (class: {})",
                                toEventName(interaction),
                                performerId.value(),
                                analyticsClient.toString(),
                                analyticsClient.getClass().getName()),
                        ex
                );
            }
        });
    }

    /**
     * Returns the interaction as an event name, in the form "&lt;subject&gt; &lt;action&gt;"
     *
     * @param interaction the interaction to format
     * @return the event name representation of the interaction
     */
    public static String toEventName(Interaction interaction) {
        return String.format(
                "%s %s",
                interaction.getSubject(),
                interaction.getAction()
        );
    }

}
