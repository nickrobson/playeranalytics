package dev.nickrobson.minecraft.playeranalytics.forge.listener;

import dev.nickrobson.minecraft.playeranalytics.forge.core.listener.MinecraftPotionInteractions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.brewing.PlayerBrewedPotionEvent;
import net.minecraftforge.event.brewing.PotionBrewEvent;
import net.minecraftforge.event.entity.living.PotionEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static dev.nickrobson.minecraft.playeranalytics.forge.listener.ForgeEventUtil.isEventCancelled;

public class MinecraftForgePotionListener {

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onPotionBrewed(PotionBrewEvent.Post event) {
        if (isEventCancelled(event))
            return;

        ItemStack[] itemStacks = new ItemStack[event.getLength()];
        for (int i = 0; i < event.getLength(); i++) {
            itemStacks[i] = event.getItem(i);
        }
        MinecraftPotionInteractions.onPotionBrewed(itemStacks);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onPlayerCollectPotion(PlayerBrewedPotionEvent event) {
        if (isEventCancelled(event))
            return;

        MinecraftPotionInteractions.onPlayerCollectPotion(
                event.getPlayer(),
                event.getStack()
        );
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onPotionAdded(PotionEvent.PotionAddedEvent event) {
        if (isEventCancelled(event))
            return;

        if (!(event.getEntity() instanceof Player))
            // Minecolonies settlers spam this :(
            // Maybe one day there'll be a config for this...
            return;

        MinecraftPotionInteractions.onPotionAdded(
                (Player) event.getEntity(),
                event.getPotionEffect()
        );
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onPotionRemoved(PotionEvent.PotionRemoveEvent event) {
        if (isEventCancelled(event))
            return;

        if (!(event.getEntity() instanceof Player))
            // Minecolonies settlers spam this :(
            // Maybe one day there'll be a config for this...
            return;

        MinecraftPotionInteractions.onPotionRemoved(
                (Player) event.getEntity(),
                event.getPotionEffect()
        );
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onPotionExpired(PotionEvent.PotionExpiryEvent event) {
        if (isEventCancelled(event))
            return;

        if (!(event.getEntity() instanceof Player))
            // Only fire for players to match the above interactions
            // Maybe one day there'll be a config for this...
            return;

        MinecraftPotionInteractions.onPotionExpired(
                (Player) event.getEntity(),
                event.getPotionEffect()
        );
    }

}
