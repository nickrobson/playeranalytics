package dev.nickrobson.minecraft.playeranalytics.forge.listener;

import dev.nickrobson.minecraft.playeranalytics.core.listener.MinecraftPlayerInteractions;
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

import static dev.nickrobson.minecraft.playeranalytics.forge.listener.ForgeEventUtil.isEventCancelled;

public class MinecraftForgePlayerListener {

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onPlayerLogIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (isEventCancelled(event))
            return;

        MinecraftPlayerInteractions.onLogIn(
                event.getPlayer()
        );
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onPlayerLogOut(PlayerEvent.PlayerLoggedOutEvent event) {
        if (isEventCancelled(event))
            return;

        MinecraftPlayerInteractions.onLogOut(
                event.getPlayer()
        );
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onPlayerChat(ServerChatEvent event) {
        if (isEventCancelled(event))
            return;

        MinecraftPlayerInteractions.onChat(
                event.getPlayer(),
                event.getMessage()
        );
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onPlayerChangeDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        if (isEventCancelled(event))
            return;

        MinecraftPlayerInteractions.onChangeDimension(
                event.getPlayer(),
                event.getFrom(),
                event.getTo()
        );
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onTriggerVillageSiege(VillageSiegeEvent event) {
        if (isEventCancelled(event))
            return;

        MinecraftPlayerInteractions.onTriggerVillageSiege(
                event.getPlayer()
        );
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onRespawn(PlayerEvent.PlayerRespawnEvent event) {
        if (isEventCancelled(event))
            return;

        MinecraftPlayerInteractions.onRespawn(
                event.getPlayer()
        );
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onSleep(PlayerSleepInBedEvent event) {
        if (isEventCancelled(event))
            return;

        MinecraftPlayerInteractions.onSleep(
                event.getPlayer()
        );
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onWakeUp(PlayerWakeUpEvent event) {
        if (isEventCancelled(event))
            return;

        MinecraftPlayerInteractions.onWakeUp(
                event.getPlayer()
        );
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onXpLevelChange(PlayerXpEvent.LevelChange event) {
        if (isEventCancelled(event))
            return;

        MinecraftPlayerInteractions.onXpLevelChange(
                event.getPlayer(),
                event.getLevels()
        );
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onXpChange(PlayerXpEvent.XpChange event) {
        if (isEventCancelled(event))
            return;

        MinecraftPlayerInteractions.onXpChange(
                event.getPlayer(),
                event.getAmount()
        );
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onGainAdvancement(AdvancementEvent event) {
        if (isEventCancelled(event))
            return;

        MinecraftPlayerInteractions.onGainAdvancement(
                event.getPlayer(),
                event.getAdvancement()
        );
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onArrowLoose(ArrowLooseEvent event) {
        if (isEventCancelled(event))
            return;

        MinecraftPlayerInteractions.onArrowLoose(
                event.getPlayer(),
                event.getBow(),
                event.getCharge()
        );
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onArrowNock(ArrowNockEvent event) {
        if (isEventCancelled(event))
            return;

        if (event.getAction().getResult() == InteractionResult.FAIL)
            return;

        MinecraftPlayerInteractions.onArrowNock(
                event.getPlayer(),
                event.getBow()
        );
    }

}