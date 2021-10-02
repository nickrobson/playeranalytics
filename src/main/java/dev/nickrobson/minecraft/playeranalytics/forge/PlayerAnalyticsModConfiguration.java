package dev.nickrobson.minecraft.playeranalytics.forge;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

import java.util.Optional;

public class PlayerAnalyticsModConfiguration {
    private static final String AMPLITUDE_TOKEN_PLACEHOLDER = "INSERT AMPLITUDE API TOKEN HERE";

    private final ForgeConfigSpec.ConfigValue<String> amplitudeToken;
    private final ForgeConfigSpec.BooleanValue debugModeEnabled;

    public PlayerAnalyticsModConfiguration() {
        ForgeConfigSpec.Builder configBuilder = new ForgeConfigSpec.Builder();

        configBuilder.push("amplitude");
        {
            amplitudeToken = configBuilder
                    .comment("The API token for your Amplitude project, given to you during project setup")
                    .define("API Token", AMPLITUDE_TOKEN_PLACEHOLDER);
        }
        configBuilder.pop();

        debugModeEnabled = configBuilder
                .comment("Whether to enable debug mode, causing all analytics events to be printed to console")
                .define("Enable Debug Mode", false);

        ForgeConfigSpec config = configBuilder.build();

        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, config);
    }

    public Optional<String> getAmplitudeToken() {
        return Optional.ofNullable(this.amplitudeToken.get())
                .filter(token -> !AMPLITUDE_TOKEN_PLACEHOLDER.equals(token));
    }

    public boolean isDebugModeEnabled() {
        return debugModeEnabled.get();
    }
}
