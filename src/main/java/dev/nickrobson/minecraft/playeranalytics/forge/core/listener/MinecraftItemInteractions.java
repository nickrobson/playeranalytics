package dev.nickrobson.minecraft.playeranalytics.forge.core.listener;

import dev.nickrobson.minecraft.playeranalytics.forge.core.api.interaction.InteractionAttributes;
import dev.nickrobson.minecraft.playeranalytics.forge.core.api.interaction.minecraft.MinecraftInteraction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;

import static dev.nickrobson.minecraft.playeranalytics.forge.core.interaction.InteractionAttributeHelper.getBlockType;
import static dev.nickrobson.minecraft.playeranalytics.forge.core.interaction.InteractionAttributeHelper.getEntityType;
import static dev.nickrobson.minecraft.playeranalytics.forge.core.interaction.InteractionAttributeHelper.getFluidId;
import static dev.nickrobson.minecraft.playeranalytics.forge.core.interaction.InteractionAttributeHelper.getItemStackAttributes;
import static dev.nickrobson.minecraft.playeranalytics.forge.core.interaction.InteractionAttributeHelper.getItemStackType;
import static dev.nickrobson.minecraft.playeranalytics.forge.core.interaction.InteractionEventHelper.trackEntityInteractionWithActor;

public class MinecraftItemInteractions {
    static final String ATTRIBUTE_TARGET_TYPE = "targetType";
    static final String ATTRIBUTE_TARGET = "target";

    public static void onAnvilRepair(Player player, ItemStack primaryInput, ItemStack secondaryInput, ItemStack result) {
        trackEntityInteractionWithActor(
                player,
                MinecraftInteraction.ITEM_REPAIRED,
                new InteractionAttributes()
                        .set("type", getItemStackType(result))
                        .set("inputType", getItemStackType(primaryInput))
                        .set("ingredientType", getItemStackType(secondaryInput))
        );
    }

    public static void onItemPickup(Player player, ItemStack itemStack) {
        trackEntityInteractionWithActor(
                player,
                MinecraftInteraction.ITEM_PICKED_UP,
                getItemStackAttributes(itemStack)
        );
    }

    public static void onItemToss(Player player, ItemStack itemStack) {
        trackEntityInteractionWithActor(
                player,
                MinecraftInteraction.ITEM_DROPPED,
                getItemStackAttributes(itemStack)
        );
    }

    public static void onItemCraft(Player player, ItemStack itemStack) {
        trackEntityInteractionWithActor(
                player,
                MinecraftInteraction.ITEM_CRAFTED,
                getItemStackAttributes(itemStack)
        );
    }

    public static void onItemSmelt(Player player, ItemStack itemStack) {
        trackEntityInteractionWithActor(
                player,
                MinecraftInteraction.ITEM_SMELTED,
                getItemStackAttributes(itemStack)
        );
    }

    public static void onItemFish(Player player, ItemStack itemStack) {
        trackEntityInteractionWithActor(
                player,
                MinecraftInteraction.ITEM_FISHED,
                getItemStackAttributes(itemStack)
        );
    }

    public static void onItemUse(Entity entity, ItemStack itemStack) {
        trackEntityInteractionWithActor(
                entity,
                MinecraftInteraction.ITEM_USED,
                getItemStackAttributes(itemStack)
        );
    }

    public static void onItemDestroy(Player player, ItemStack itemStack) {
        trackEntityInteractionWithActor(
                player,
                MinecraftInteraction.ITEM_DESTROYED,
                getItemStackAttributes(itemStack)
        );
    }

    public static void onItemRightClick(Player player, ItemStack itemStack) {
        trackEntityInteractionWithActor(
                player,
                MinecraftInteraction.ITEM_RIGHT_CLICKED,
                getItemStackAttributes(itemStack)
                        .set(ATTRIBUTE_TARGET_TYPE, "none")
        );
    }

    public static void onRightClickBlock(Player player, ItemStack itemStack, BlockState blockState) {
        if (itemStack.getItem() instanceof BlockItem) {
            // Assume the player is trying to place a block - not worth tracking as a right click
            return;
        }

        trackEntityInteractionWithActor(
                player,
                MinecraftInteraction.ITEM_RIGHT_CLICKED,
                getItemStackAttributes(itemStack)
                        .set(ATTRIBUTE_TARGET_TYPE, "block")
                        .set(ATTRIBUTE_TARGET, getBlockType(blockState))
        );
    }

    public static void onRightClickEntity(Player player, ItemStack itemStack, Entity entity) {
        trackEntityInteractionWithActor(
                player,
                MinecraftInteraction.ITEM_RIGHT_CLICKED,
                getItemStackAttributes(itemStack)
                        .set(ATTRIBUTE_TARGET_TYPE, "entity")
                        .set(ATTRIBUTE_TARGET, getEntityType(entity))
        );
    }

    public static void onBucketFill(Player player, ItemStack itemStack, Fluid fluid) {
        trackEntityInteractionWithActor(
                player,
                MinecraftInteraction.BUCKET_FILLED,
                getItemStackAttributes(itemStack)
                        .set("fluid", getFluidId(fluid))
        );
    }

    public static void onBucketEmpty(Player player, ItemStack itemStack, Fluid fluid) {
        trackEntityInteractionWithActor(
                player,
                MinecraftInteraction.BUCKET_EMPTIED,
                getItemStackAttributes(itemStack)
                        .set("fluid", getFluidId(fluid))
        );
    }
}
