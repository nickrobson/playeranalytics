package dev.nickrobson.minecraft.playeranalytics.core.analyticsclient.amplitude;

import dev.nickrobson.minecraft.playeranalytics.core.api.interaction.Interaction;
import dev.nickrobson.minecraft.playeranalytics.core.api.interaction.InteractionAttributes;
import dev.nickrobson.minecraft.playeranalytics.core.api.interaction.InteractionPerformerId;
import dev.nickrobson.minecraft.playeranalytics.core.api.AnalyticsClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Instant;
import java.util.Optional;

public class AmplitudeAnalyticsClient implements AnalyticsClient {
    private static final Logger LOGGER = LogManager.getLogger(AmplitudeAnalyticsClient.class);

    private final AmplitudeClient amplitudeClient;
    private boolean enabled = false;

    public AmplitudeAnalyticsClient() {
        this.amplitudeClient = new AmplitudeClient();
    }

    public void updateAmplitudeToken(Optional<String> amplitudeTokenOpt) {
        if (amplitudeTokenOpt.isPresent()) {
            this.amplitudeClient.init(amplitudeTokenOpt.get());
            if (enabled) {
                LOGGER.info("Updating Amplitude client with API token from changed config");
            } else {
                LOGGER.info("Enabling Amplitude client as an API token has been set");
            }
            this.enabled = true;
        } else {
            LOGGER.info("Disabling Amplitude client as no API token has been set");
            this.enabled = false;
        }
    }

    @Override
    public void trackInteraction(InteractionPerformerId performerId, Interaction interaction, InteractionAttributes attributes) {
        if (!enabled) {
            return;
        }

        this.amplitudeClient.logEvent(
                interaction,
                performerId,
                Instant.now(),
                attributes
        );
    }
}
