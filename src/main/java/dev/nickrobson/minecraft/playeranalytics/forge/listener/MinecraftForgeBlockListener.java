package dev.nickrobson.minecraft.playeranalytics.forge.listener;

import dev.nickrobson.minecraft.playeranalytics.forge.core.listener.MinecraftBlockInteractions;
import net.minecraftforge.event.entity.player.BonemealEvent;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.NoteBlockEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class MinecraftForgeBlockListener {
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onBlockPlace(BlockEvent.EntityPlaceEvent event) {
        MinecraftBlockInteractions.onBlockPlace(
                event.getEntity(),
                event.getPlacedBlock()
        );
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onBlockMultiPlace(BlockEvent.EntityMultiPlaceEvent event) {
        MinecraftBlockInteractions.onBlockPlace(
                event.getEntity(),
                event.getPlacedBlock()
        );
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        MinecraftBlockInteractions.onBlockBreak(
                event.getPlayer(),
                event.getState()
        );
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        MinecraftBlockInteractions.onRightClickBlock(
                event.getPlayer(),
                event.getWorld().getBlockState(event.getPos()),
                event.getItemStack()
        );
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onBlockToolInteract(BlockEvent.BlockToolInteractEvent event) {
        MinecraftBlockInteractions.onBlockToolInteract(
                event.getPlayer(),
                event.getState(),
                event.getHeldItemStack()
        );
    }

    @SuppressWarnings("unused")
    public static void onHarvest(PlayerEvent.HarvestCheck event) {
        // This seemingly fires every time a block gets cracks as you break it... far too spammy!
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onFarmlandTrample(BlockEvent.FarmlandTrampleEvent event) {
        MinecraftBlockInteractions.onFarmlandTrample(
                event.getEntity(),
                event.getState(),
                event.getFallDistance()
        );
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onBonemeal(BonemealEvent event) {
        MinecraftBlockInteractions.onBonemeal(
                event.getEntity(),
                event.getBlock(),
                event.getStack()
        );
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onContainerOpen(PlayerContainerEvent.Open event) {
        MinecraftBlockInteractions.onContainerOpen(
                event.getPlayer(),
                event.getContainer()
        );
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onContainerClose(PlayerContainerEvent.Close event) {
        MinecraftBlockInteractions.onContainerClose(
                event.getPlayer(),
                event.getContainer()
        );
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onNoteBlockPlay(NoteBlockEvent.Play event) {
        MinecraftBlockInteractions.onNoteBlockPlay(
                event.getInstrument(),
                event.getVanillaNoteId()
        );
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onNoteBlockChange(NoteBlockEvent.Change event) {
        MinecraftBlockInteractions.onNoteBlockChange(
                event.getVanillaNoteId()
        );
    }
}
