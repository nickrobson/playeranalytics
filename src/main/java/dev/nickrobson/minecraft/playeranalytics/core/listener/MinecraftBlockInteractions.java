package dev.nickrobson.minecraft.playeranalytics.core.listener;

import dev.nickrobson.minecraft.playeranalytics.core.api.interaction.InteractionAttributes;
import dev.nickrobson.minecraft.playeranalytics.core.api.interaction.minecraft.MinecraftInteraction;
import dev.nickrobson.minecraft.playeranalytics.core.interaction.InteractionAttributeHelper;
import dev.nickrobson.minecraft.playeranalytics.core.interaction.InteractionEventHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;

import static dev.nickrobson.minecraft.playeranalytics.core.interaction.InteractionAttributeHelper.getBlockAttributes;
import static dev.nickrobson.minecraft.playeranalytics.core.interaction.InteractionAttributeHelper.getToolType;

public class MinecraftBlockInteractions {
    private static final String ATTRIBUTE_ITEM_TYPE = "itemType";
    private static final String ATTRIBUTE_TOOL_TYPE = "toolType";

    public static void onBlockPlace(Entity entity, BlockState blockState) {
        InteractionEventHelper.trackEntityInteractionWithActor(
                entity,
                MinecraftInteraction.BLOCK_PLACED,
                InteractionAttributeHelper.getBlockAttributes(blockState)
        );
    }

    public static void onBlockBreak(Player player, BlockState blockState) {
        InteractionEventHelper.trackEntityInteractionWithActor(
                player,
                MinecraftInteraction.BLOCK_BROKEN,
                InteractionAttributeHelper.getBlockAttributes(blockState)
        );
    }

    public static void onRightClickBlock(Player player, BlockState blockState, ItemStack itemStack) {
        if (itemStack.getItem() instanceof BlockItem) {
            // Assume the player is trying to place a block - not worth tracking as a right click
            return;
        }

        InteractionEventHelper.trackEntityInteractionWithActor(
                player,
                MinecraftInteraction.BLOCK_RIGHT_CLICKED,
                InteractionAttributeHelper.getBlockAttributes(blockState)
                        .set(ATTRIBUTE_ITEM_TYPE, InteractionAttributeHelper.getItemStackType(itemStack))
        );
    }

    public static void onBlockToolInteract(Player player, BlockState blockState, ItemStack itemStack) {
        InteractionEventHelper.trackEntityInteractionWithActor(
                player,
                MinecraftInteraction.BLOCK_RIGHT_CLICKED,
                InteractionAttributeHelper.getBlockAttributes(blockState)
                        .set(ATTRIBUTE_TOOL_TYPE, InteractionAttributeHelper.getToolType(itemStack))
        );
    }

    public static void onFarmlandTrample(Entity entity, BlockState blockState, double fallDistance) {
        InteractionEventHelper.trackEntityInteractionWithActor(
                entity,
                MinecraftInteraction.BLOCK_TRAMPLED,
                InteractionAttributeHelper.getBlockAttributes(blockState)
                        .set("fallDistance", fallDistance)
        );
    }

    public static void onBonemeal(Entity entity, BlockState blockState, ItemStack itemStack) {
        InteractionEventHelper.trackEntityInteractionWithActor(
                entity,
                MinecraftInteraction.BLOCK_BONEMEALED,
                InteractionAttributeHelper.getBlockAttributes(blockState)
                        .set(ATTRIBUTE_ITEM_TYPE, InteractionAttributeHelper.getItemStackType(itemStack))
        );
    }

    public static void onContainerOpen(Player player, AbstractContainerMenu containerMenu) {
        InteractionEventHelper.trackEntityInteractionWithActor(
                player,
                MinecraftInteraction.CONTAINER_OPENED,
                InteractionAttributeHelper.getContainerAttributes(containerMenu)
        );
    }

    public static void onContainerClose(Player player, AbstractContainerMenu containerMenu) {
        // Ignore closing the player's inventory
        if (containerMenu instanceof InventoryMenu)
            return;

        InteractionEventHelper.trackEntityInteractionWithActor(
                player,
                MinecraftInteraction.CONTAINER_CLOSED,
                InteractionAttributeHelper.getContainerAttributes(containerMenu)
        );
    }

    public static void onNoteBlockPlay(NoteBlockInstrument instrument, int noteId) {
        InteractionEventHelper.trackServerInteraction(
                MinecraftInteraction.NOTE_BLOCK_PLAYED,
                new InteractionAttributes()
                        .set("instrument", instrument.getSerializedName())
                        .set("note", getNoteName(noteId))
                        .set("octave", getOctaveFromNote(noteId))
        );
    }

    public static void onNoteBlockChange(int noteId) {
        InteractionEventHelper.trackServerInteraction(
                MinecraftInteraction.NOTE_BLOCK_CHANGED,
                new InteractionAttributes()
                        .set("note", getNoteName(noteId))
                        .set("octave", getOctaveFromNote(noteId))
        );
    }

    private static String getNoteName(int noteId) {
        return notes[noteId % notes.length];
    }

    private static String getOctaveFromNote(int noteId) {
        return noteId < notes.length ? "low" : noteId < notes.length * 2 ? "mid" : "high";
    }

    private static final String[] notes = {
            "f_sharp",
            "g", "g_sharp",
            "a", "a_sharp",
            "b",
            "c", "c_sharp",
            "d", "d_sharp",
            "e",
            "f"
    };
}
