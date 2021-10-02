package dev.nickrobson.minecraft.playeranalytics.core.listener;

import dev.nickrobson.minecraft.playeranalytics.core.api.interaction.InteractionAttributes;
import dev.nickrobson.minecraft.playeranalytics.core.api.interaction.minecraft.MinecraftInteraction;
import dev.nickrobson.minecraft.playeranalytics.core.interaction.InteractionAttributeHelper;
import dev.nickrobson.minecraft.playeranalytics.core.interaction.InteractionEventHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;

import static dev.nickrobson.minecraft.playeranalytics.core.interaction.InteractionAttributeHelper.getBlockType;
import static dev.nickrobson.minecraft.playeranalytics.core.interaction.InteractionEventHelper.trackEntityInteractionWithActor;

public class MinecraftItemInteractions {
    static final String ATTRIBUTE_TARGET_TYPE = "targetType";
    static final String ATTRIBUTE_TARGET = "target";

    public static void onAnvilRepair(Player player, ItemStack primaryInput, ItemStack secondaryInput, ItemStack result) {
        InteractionEventHelper.trackEntityInteractionWithActor(
                player,
                MinecraftInteraction.ITEM_REPAIRED,
                new InteractionAttributes()
                        .set("type", InteractionAttributeHelper.getItemStackType(result))
                        .set("inputType", InteractionAttributeHelper.getItemStackType(primaryInput))
                        .set("ingredientType", InteractionAttributeHelper.getItemStackType(secondaryInput))
        );
    }

    public static void onItemPickup(Player player, ItemStack itemStack) {
        InteractionEventHelper.trackEntityInteractionWithActor(
                player,
                MinecraftInteraction.ITEM_PICKED_UP,
                InteractionAttributeHelper.getItemStackAttributes(itemStack)
        );
    }

    public static void onItemToss(Player player, ItemStack itemStack) {
        InteractionEventHelper.trackEntityInteractionWithActor(
                player,
                MinecraftInteraction.ITEM_DROPPED,
                InteractionAttributeHelper.getItemStackAttributes(itemStack)
        );
    }

    public static void onItemCraft(Player player, ItemStack itemStack) {
        InteractionEventHelper.trackEntityInteractionWithActor(
                player,
                MinecraftInteraction.ITEM_CRAFTED,
                InteractionAttributeHelper.getItemStackAttributes(itemStack)
        );
    }

    public static void onItemSmelt(Player player, ItemStack itemStack) {
        InteractionEventHelper.trackEntityInteractionWithActor(
                player,
                MinecraftInteraction.ITEM_SMELTED,
                InteractionAttributeHelper.getItemStackAttributes(itemStack)
        );
    }

    public static void onItemFish(Player player, ItemStack itemStack) {
        InteractionEventHelper.trackEntityInteractionWithActor(
                player,
                MinecraftInteraction.ITEM_FISHED,
                InteractionAttributeHelper.getItemStackAttributes(itemStack)
        );
    }

    public static void onItemUse(Entity entity, ItemStack itemStack) {
        InteractionEventHelper.trackEntityInteractionWithActor(
                entity,
                MinecraftInteraction.ITEM_USED,
                InteractionAttributeHelper.getItemStackAttributes(itemStack)
        );
    }

    public static void onItemDestroy(Player player, ItemStack itemStack) {
        InteractionEventHelper.trackEntityInteractionWithActor(
                player,
                MinecraftInteraction.ITEM_DESTROYED,
                InteractionAttributeHelper.getItemStackAttributes(itemStack)
        );
    }

    public static void onItemRightClick(Player player, ItemStack itemStack) {
        InteractionEventHelper.trackEntityInteractionWithActor(
                player,
                MinecraftInteraction.ITEM_RIGHT_CLICKED,
                InteractionAttributeHelper.getItemStackAttributes(itemStack)
                        .set(ATTRIBUTE_TARGET_TYPE, "none")
        );
    }

    public static void onRightClickBlock(Player player, ItemStack itemStack, BlockState blockState) {
        if (itemStack.getItem() instanceof BlockItem) {
            // Assume the player is trying to place a block - not worth tracking as a right click
            return;
        }

        InteractionEventHelper.trackEntityInteractionWithActor(
                player,
                MinecraftInteraction.ITEM_RIGHT_CLICKED,
                InteractionAttributeHelper.getItemStackAttributes(itemStack)
                        .set(ATTRIBUTE_TARGET_TYPE, "block")
                        .set(ATTRIBUTE_TARGET, InteractionAttributeHelper.getBlockType(blockState))
        );
    }

    public static void onRightClickEntity(Player player, ItemStack itemStack, Entity entity) {
        InteractionEventHelper.trackEntityInteractionWithActor(
                player,
                MinecraftInteraction.ITEM_RIGHT_CLICKED,
                InteractionAttributeHelper.getItemStackAttributes(itemStack)
                        .set(ATTRIBUTE_TARGET_TYPE, "entity")
                        .set(ATTRIBUTE_TARGET, InteractionAttributeHelper.getEntityType(entity))
        );
    }

    public static void onBucketFill(Player player, ItemStack itemStack, Fluid fluid) {
        InteractionEventHelper.trackEntityInteractionWithActor(
                player,
                MinecraftInteraction.BUCKET_FILLED,
                InteractionAttributeHelper.getItemStackAttributes(itemStack)
                        .set("fluid", InteractionAttributeHelper.getFluidId(fluid))
        );
    }

    public static void onBucketEmpty(Player player, ItemStack itemStack, Fluid fluid) {
        InteractionEventHelper.trackEntityInteractionWithActor(
                player,
                MinecraftInteraction.BUCKET_EMPTIED,
                InteractionAttributeHelper.getItemStackAttributes(itemStack)
                        .set("fluid", InteractionAttributeHelper.getFluidId(fluid))
        );
    }
}
