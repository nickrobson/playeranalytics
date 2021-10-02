package dev.nickrobson.minecraft.playeranalytics.forge.core.listener;

import dev.nickrobson.minecraft.playeranalytics.forge.core.api.interaction.InteractionAttributes;
import dev.nickrobson.minecraft.playeranalytics.forge.core.api.interaction.minecraft.MinecraftInteraction;
import net.minecraft.advancements.Advancement;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import static dev.nickrobson.minecraft.playeranalytics.forge.core.interaction.InteractionAttributeHelper.getDimensionId;
import static dev.nickrobson.minecraft.playeranalytics.forge.core.interaction.InteractionAttributeHelper.getItemStackType;
import static dev.nickrobson.minecraft.playeranalytics.forge.core.interaction.InteractionEventHelper.trackEntityInteractionWithActor;

public class MinecraftPlayerInteractions {

    public static void onLogIn(Player player) {
        trackEntityInteractionWithActor(
                player,
                MinecraftInteraction.PLAYER_CONNECTED
        );
    }

    public static void onLogOut(Player player) {
        trackEntityInteractionWithActor(
                player,
                MinecraftInteraction.PLAYER_DISCONNECTED
        );
    }

    public static void onChat(Player player, String message) {
        trackEntityInteractionWithActor(
                player,
                MinecraftInteraction.PLAYER_CHATTED,
                new InteractionAttributes()
                        .set("messageLength", message.length())
        );
    }

    public static void onChangeDimension(Player player, ResourceKey<Level> from, ResourceKey<Level> to) {
        trackEntityInteractionWithActor(
                player,
                MinecraftInteraction.PLAYER_CHANGED_DIMENSION,
                new InteractionAttributes()
                        .set("from", getDimensionId(from))
                        .set("to", getDimensionId(to))
        );
    }

    public static void onTriggerVillageSiege(Player player) {
        trackEntityInteractionWithActor(
                player,
                MinecraftInteraction.VILLAGE_SIEGE_TRIGGERED
        );
    }

    public static void onRespawn(Player player) {
        trackEntityInteractionWithActor(
                player,
                MinecraftInteraction.PLAYER_RESPAWNED
        );
    }

    public static void onSleep(Player player) {
        trackEntityInteractionWithActor(
                player,
                MinecraftInteraction.PLAYER_SLEPT
        );
    }

    public static void onWakeUp(Player player) {
        trackEntityInteractionWithActor(
                player,
                MinecraftInteraction.PLAYER_WOKE_UP
        );
    }

    public static void onXpLevelChange(Player player, int levelChange) {
        if (levelChange == 0)
            return;

        trackEntityInteractionWithActor(
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

        trackEntityInteractionWithActor(
                player,
                MinecraftInteraction.PLAYER_XP_CHANGED,
                new InteractionAttributes()
                        .set("oldXp", player.totalExperience)
                        .set("newXp", Mth.clamp(player.totalExperience + experience, 0, Integer.MAX_VALUE))
                        .set("change", experience)
        );
    }

    public static void onGainAdvancement(Player player, Advancement advancement) {
        trackEntityInteractionWithActor(
                player,
                MinecraftInteraction.ADVANCEMENT_GAINED,
                new InteractionAttributes()
                        .set("id", advancement.getId().toString())
        );
    }

    public static void onArrowLoose(Player player, ItemStack bow, int charge) {
        trackEntityInteractionWithActor(
                player,
                MinecraftInteraction.ARROW_LOOSED,
                new InteractionAttributes()
                        .set("bowType", getItemStackType(bow))
                        .set("charge", charge)
        );
    }

    public static void onArrowNock(Player player, ItemStack bow) {
        trackEntityInteractionWithActor(
                player,
                MinecraftInteraction.ARROW_NOCKED,
                new InteractionAttributes()
                        .set("bowType", getItemStackType(bow))
        );
    }
}
