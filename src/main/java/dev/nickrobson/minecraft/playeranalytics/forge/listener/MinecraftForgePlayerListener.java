package dev.nickrobson.minecraft.playeranalytics.forge.listener;

import dev.nickrobson.minecraft.playeranalytics.forge.core.listener.MinecraftPlayerInteractions;
import net.minecraft.world.InteractionResult;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.player.AdvancementEvent;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import net.minecraftforge.event.entity.player.ArrowNockEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.event.entity.player.PlayerXpEvent;
import net.minecraftforge.event.village.VillageSiegeEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class MinecraftForgePlayerListener {

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onPlayerLogIn(PlayerEvent.PlayerLoggedInEvent event) {
        MinecraftPlayerInteractions.onLogIn(
                event.getPlayer()
        );
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onPlayerLogOut(PlayerEvent.PlayerLoggedOutEvent event) {
        MinecraftPlayerInteractions.onLogOut(
                event.getPlayer()
        );
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onPlayerChat(ServerChatEvent event) {
        MinecraftPlayerInteractions.onChat(
                event.getPlayer(),
                event.getMessage()
        );
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onPlayerChangeDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        MinecraftPlayerInteractions.onChangeDimension(
                event.getPlayer(),
                event.getFrom(),
                event.getTo()
        );
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onTriggerVillageSiege(VillageSiegeEvent event) {
        MinecraftPlayerInteractions.onTriggerVillageSiege(
                event.getPlayer()
        );
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onRespawn(PlayerEvent.PlayerRespawnEvent event) {
        MinecraftPlayerInteractions.onRespawn(
                event.getPlayer()
        );
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onSleep(PlayerSleepInBedEvent event) {
        MinecraftPlayerInteractions.onSleep(
                event.getPlayer()
        );
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onWakeUp(PlayerWakeUpEvent event) {
        MinecraftPlayerInteractions.onWakeUp(
                event.getPlayer()
        );
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onXpLevelChange(PlayerXpEvent.LevelChange event) {
        MinecraftPlayerInteractions.onXpLevelChange(
                event.getPlayer(),
                event.getLevels()
        );
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onXpChange(PlayerXpEvent.XpChange event) {
        MinecraftPlayerInteractions.onXpChange(
                event.getPlayer(),
                event.getAmount()
        );
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onGainAdvancement(AdvancementEvent event) {
        MinecraftPlayerInteractions.onGainAdvancement(
                event.getPlayer(),
                event.getAdvancement()
        );
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onArrowLoose(ArrowLooseEvent event) {
        MinecraftPlayerInteractions.onArrowLoose(
                event.getPlayer(),
                event.getBow(),
                event.getCharge()
        );
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onArrowNock(ArrowNockEvent event) {
        if (event.getAction().getResult() == InteractionResult.FAIL)
            return;

        MinecraftPlayerInteractions.onArrowNock(
                event.getPlayer(),
                event.getBow()
        );
    }

}