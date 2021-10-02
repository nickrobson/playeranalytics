package dev.nickrobson.minecraft.playeranalytics.core.analyticsclient.amplitude;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.nickrobson.minecraft.playeranalytics.core.PlayerAnalyticsConstants;
import dev.nickrobson.minecraft.playeranalytics.core.api.interaction.Interaction;
import dev.nickrobson.minecraft.playeranalytics.core.api.interaction.InteractionAttributes;
import dev.nickrobson.minecraft.playeranalytics.core.api.interaction.InteractionPerformerId;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

import static dev.nickrobson.minecraft.playeranalytics.core.api.InteractionController.toEventName;

public class AmplitudeClient {
    public static final Logger logger = LogManager.getLogger(AmplitudeClient.class);

    private static final String API_URL = "https://api2.amplitude.com/2/httpapi";
    private static final String APP_VERSION = PlayerAnalyticsConstants.MOD_NAME + " " + PlayerAnalyticsConstants.MOD_VERSION;
    private static final String CLIENT_NAME = "Minecraft/" + PlayerAnalyticsConstants.MOD_NAME + "/" + PlayerAnalyticsConstants.MOD_VERSION;
    private static final int MAX_PROPERTY_KEYS = 1024;
    private static final int MAX_STRING_LENGTH = 1000;

    private final HttpClient httpClient;
    private String apiKey;

    public AmplitudeClient() {
        this.httpClient = HttpClient.newBuilder()
                .executor(Executors.newSingleThreadExecutor(runnable -> {
                    Thread t = Executors.defaultThreadFactory().newThread(runnable);
                    t.setDaemon(true);
                    return t;
                }))
                .build();
    }

    public void init(String key) {
        this.apiKey = key;
    }

    public CompletableFuture<Void> logEvent(Interaction interaction, InteractionPerformerId performerId, Instant timestamp, InteractionAttributes attributes) {
        PlayerAnalyticsAmplitudeEvent event = new PlayerAnalyticsAmplitudeEvent(
                toEventName(interaction),
                performerId.value(),
                timestamp,
                attributes
        );

        JsonObject bodyJson = new JsonObject();
        bodyJson.addProperty("api_key", apiKey);
        bodyJson.add("events", event.toJsonObject());

        CompletableFuture<HttpResponse<String>> responseFuture = httpClient.sendAsync(
                HttpRequest.newBuilder()
                        .uri(URI.create(API_URL))
                        .header("Accept", "application/json")
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(bodyJson.toString()))
                        .build(),
                HttpResponse.BodyHandlers.ofString());

        return responseFuture.thenAccept(response -> {
            if (response.statusCode() < 400) {
                logger.debug("Amplitude returned success {}; message: {}", response.statusCode(), response.body());
            } else {
                logger.warn("Amplitude returned error {}; message: {}", response.statusCode(), response.body());
            }
        });
    }

    private final static class PlayerAnalyticsAmplitudeEvent {
        private final String eventType;
        private final String userId;
        private final Long timestamp;
        private final InteractionAttributes interactionAttributes;

        public PlayerAnalyticsAmplitudeEvent(String eventType, String userId, Instant timestamp, InteractionAttributes interactionAttributes) {
            this.eventType = eventType;
            this.userId = userId;
            this.timestamp = timestamp.toEpochMilli();
            this.interactionAttributes = interactionAttributes;
            this.validateInteractionAttributes();
        }

        public JsonObject toJsonObject() {
            JsonObject event = new JsonObject();

            event.addProperty("app_version", APP_VERSION);
            event.addProperty("library", CLIENT_NAME);
            event.addProperty("platform", "Minecraft");

            event.addProperty("event_type", eventType);
            event.addProperty("user_id", userId);
            event.addProperty("device_id", (String) null);
            event.addProperty("time", timestamp);

            event.add(
                    "event_properties",
                    interactionAttributes == null
                            ? null
                            : interactionAttributes.toJsonObject());

            return event;
        }

        private void validateInteractionAttributes() {
            if (this.interactionAttributes == null) {
                return;
            }

            JsonObject eventProperties = interactionAttributes.toJsonObject();

            if (eventProperties.size() > MAX_PROPERTY_KEYS) {
                throw new IllegalStateException("More than the maximum (" + MAX_PROPERTY_KEYS + ") event properties were defined (" + eventProperties.size() + ")");
            }

            eventProperties.entrySet()
                    .forEach(eventProperty -> validateInteractionAttribute(eventProperty.getKey(), eventProperty.getValue()));
        }

        private void validateInteractionAttribute(String key, JsonElement jsonElement) {
            Objects.requireNonNull(key, "key cannot be null");
            Objects.requireNonNull(jsonElement, "value cannot be null");

            if (key.length() > MAX_STRING_LENGTH) {
                throw new IllegalArgumentException("Key ('" + key + "') cannot be longer than " + MAX_STRING_LENGTH + " characters");
            }

            if (jsonElement.isJsonPrimitive() && jsonElement.getAsJsonPrimitive().isString()) {
                String value = jsonElement.getAsString();
                if (value.length() > MAX_STRING_LENGTH) {
                    throw new IllegalArgumentException("Value string ('" + value +  "') cannot be longer than " + MAX_STRING_LENGTH + " characters");
                }
            }

            if (jsonElement.isJsonObject()) {
                JsonObject jsonObject = jsonElement.getAsJsonObject();

                if (jsonObject.size() > MAX_PROPERTY_KEYS) {
                    throw new IllegalStateException("More than the maximum (" + MAX_PROPERTY_KEYS + ") event sub-properties were defined (" + jsonObject.size() + ") at key '" + key + "'");
                }

                jsonObject.entrySet().forEach(property ->
                        validateInteractionAttribute(property.getKey(), property.getValue()));
            }

            if (jsonElement.isJsonArray()) {
                JsonArray jsonArray = jsonElement.getAsJsonArray();

                if (jsonArray.size() > MAX_PROPERTY_KEYS) {
                    throw new IllegalStateException("More than the maximum (" + MAX_PROPERTY_KEYS + ") event property values were defined (" + jsonArray.size() + ") in array at key '" + key + "'");
                }

                jsonArray.forEach(element ->
                        validateInteractionAttribute("DummyKeyForArrayItem", element));
            }
        }
    }
}
