package dev.nickrobson.minecraft.playeranalytics.core.listener;

import dev.nickrobson.minecraft.playeranalytics.core.api.interaction.InteractionAttributes;
import dev.nickrobson.minecraft.playeranalytics.core.api.interaction.minecraft.MinecraftInteraction;
import dev.nickrobson.minecraft.playeranalytics.core.interaction.InteractionAttributeHelper;
import dev.nickrobson.minecraft.playeranalytics.core.interaction.InteractionEventHelper;
import net.minecraft.advancements.Advancement;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import static dev.nickrobson.minecraft.playeranalytics.core.interaction.InteractionAttributeHelper.getDimensionId;
import static dev.nickrobson.minecraft.playeranalytics.core.interaction.InteractionEventHelper.trackEntityInteractionWithActor;

public class MinecraftPlayerInteractions {

    public static void onLogIn(Player player) {
        InteractionEventHelper.trackEntityInteractionWithActor(
                player,
                MinecraftInteraction.PLAYER_CONNECTED
        );
    }

    public static void onLogOut(Player player) {
        InteractionEventHelper.trackEntityInteractionWithActor(
                player,
                MinecraftInteraction.PLAYER_DISCONNECTED
        );
    }

    public static void onChat(Player player, String message) {
        InteractionEventHelper.trackEntityInteractionWithActor(
                player,
                MinecraftInteraction.PLAYER_CHATTED,
                new InteractionAttributes()
                        .set("messageLength", message.length())
        );
    }

    public static void onChangeDimension(Player player, ResourceKey<Level> from, ResourceKey<Level> to) {
        InteractionEventHelper.trackEntityInteractionWithActor(
                player,
                MinecraftInteraction.PLAYER_CHANGED_DIMENSION,
                new InteractionAttributes()
                        .set("from", InteractionAttributeHelper.getDimensionId(from))
                        .set("to", InteractionAttributeHelper.getDimensionId(to))
        );
    }

    public static void onTriggerVillageSiege(Player player) {
        InteractionEventHelper.trackEntityInteractionWithActor(
                player,
                MinecraftInteraction.VILLAGE_SIEGE_TRIGGERED
        );
    }

    public static void onRespawn(Player player) {
        InteractionEventHelper.trackEntityInteractionWithActor(
                player,
                MinecraftInteraction.PLAYER_RESPAWNED
        );
    }

    public static void onSleep(Player player) {
        InteractionEventHelper.trackEntityInteractionWithActor(
                player,
                MinecraftInteraction.PLAYER_SLEPT
        );
    }

    public static void onWakeUp(Player player) {
        InteractionEventHelper.trackEntityInteractionWithActor(
                player,
                MinecraftInteraction.PLAYER_WOKE_UP
        );
    }

    public static void onXpLevelChange(Player player, int levelChange) {
        if (levelChange == 0)
            return;

        InteractionEventHelper.trackEntityInteractionWithActor(
                player,
                MinecraftInteraction.PLAYER_XP_LEVEL_CHANGED,
                new InteractionAttributes()
                        .set("oldXp", player.experienceLevel)
                        .set("newXp", player.experienceLevel + levelChange)
                        .set("change", levelChange)
        );
    }

    public static void onXpChange(Player player, int experience) {
        if (experience == 0)
            return;

        InteractionEventHelper.trackEntityInteractionWithActor(
                player,
                MinecraftInteraction.PLAYER_XP_CHANGED,
                new InteractionAttributes()
                        .set("oldXp", player.totalExperience)
                        .set("newXp", Mth.clamp(player.totalExperience + experience, 0, Integer.MAX_VALUE))
                        .set("change", experience)
        );
    }

    public static void onGainAdvancement(Player player, Advancement advancement) {
        InteractionEventHelper.trackEntityInteractionWithActor(
                player,
                MinecraftInteraction.ADVANCEMENT_GAINED,
                new InteractionAttributes()
                        .set("id", advancement.getId().toString())
        );
    }

    public static void onArrowLoose(Player player, ItemStack bow, int charge) {
        InteractionEventHelper.trackEntityInteractionWithActor(
                player,
                MinecraftInteraction.ARROW_LOOSED,
                new InteractionAttributes()
                        .set("bowType", InteractionAttributeHelper.getItemStackType(bow))
                        .set("charge", charge)
        );
    }

    public static void onArrowNock(Player player, ItemStack bow) {
        InteractionEventHelper.trackEntityInteractionWithActor(
                player,
                MinecraftInteraction.ARROW_NOCKED,
                new InteractionAttributes()
                        .set("bowType", InteractionAttributeHelper.getItemStackType(bow))
        );
    }
}
