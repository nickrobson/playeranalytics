package dev.nickrobson.minecraft.playeranalytics.forge;

import dev.nickrobson.minecraft.playeranalytics.core.analyticsclient.DebugLoggingAnalyticsClient;
import dev.nickrobson.minecraft.playeranalytics.core.analyticsclient.amplitude.AmplitudeAnalyticsClient;
import dev.nickrobson.minecraft.playeranalytics.core.api.InteractionController;
import dev.nickrobson.minecraft.playeranalytics.forge.listener.MinecraftForgeBlockListener;
import dev.nickrobson.minecraft.playeranalytics.forge.listener.MinecraftForgeEntityListener;
import dev.nickrobson.minecraft.playeranalytics.forge.listener.MinecraftForgeItemListener;
import dev.nickrobson.minecraft.playeranalytics.forge.listener.MinecraftForgePlayerListener;
import dev.nickrobson.minecraft.playeranalytics.forge.listener.MinecraftForgePotionListener;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fmllegacy.network.FMLNetworkConstants;
import net.minecraftforge.fmlserverevents.FMLServerStartingEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("playeranalytics")
public class PlayerAnalyticsMod {
    public static final Logger logger = LogManager.getLogger(PlayerAnalyticsMod.class);

    private final PlayerAnalyticsModConfiguration configuration;

    private final AmplitudeAnalyticsClient amplitudeAnalyticsClient;
    private final DebugLoggingAnalyticsClient debugLoggingAnalyticsClient;

    public PlayerAnalyticsMod() {
        logger.info("PlayerAnalytics starting...");

        this.configuration = new PlayerAnalyticsModConfiguration();

        registerEventListeners();

        amplitudeAnalyticsClient = new AmplitudeAnalyticsClient();
        debugLoggingAnalyticsClient = new DebugLoggingAnalyticsClient();
        InteractionController.registerAnalyticsClient(amplitudeAnalyticsClient);
        InteractionController.registerAnalyticsClient(debugLoggingAnalyticsClient);

        // This is intended to work as running only on one side
        ModLoadingContext.get().registerExtensionPoint(
                IExtensionPoint.DisplayTest.class,
                () -> new IExtensionPoint.DisplayTest(
                        () -> FMLNetworkConstants.IGNORESERVERONLY,
                        (a, b) -> true
                )
        );
    }

    private void registerEventListeners() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onModConfigEvent);
        MinecraftForge.EVENT_BUS.addListener(this::onServerStarting);
        MinecraftForge.EVENT_BUS.register(MinecraftForgeBlockListener.class);
        MinecraftForge.EVENT_BUS.register(MinecraftForgeEntityListener.class);
        MinecraftForge.EVENT_BUS.register(MinecraftForgeItemListener.class);
        MinecraftForge.EVENT_BUS.register(MinecraftForgePlayerListener.class);
        MinecraftForge.EVENT_BUS.register(MinecraftForgePotionListener.class);
    }

    public void onModConfigEvent(ModConfigEvent event) {
        if (event.getConfig().getType() == ModConfig.Type.SERVER) {
            amplitudeAnalyticsClient.updateAmplitudeToken(configuration.getAmplitudeToken());
            debugLoggingAnalyticsClient.updateDebugMode(configuration.isDebugModeEnabled());
        }
    }

    public void onServerStarting(FMLServerStartingEvent event) {
        logger.info("PlayerAnalytics started!");
    }
}
