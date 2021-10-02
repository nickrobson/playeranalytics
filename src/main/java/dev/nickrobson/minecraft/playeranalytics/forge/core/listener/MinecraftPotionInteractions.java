package dev.nickrobson.minecraft.playeranalytics.forge.core.listener;

import dev.nickrobson.minecraft.playeranalytics.forge.core.api.interaction.InteractionAttributes;
import dev.nickrobson.minecraft.playeranalytics.forge.core.api.interaction.minecraft.MinecraftInteraction;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.alchemy.PotionUtils;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static dev.nickrobson.minecraft.playeranalytics.forge.core.interaction.InteractionEventHelper.trackEntityInteractionWithActor;
import static dev.nickrobson.minecraft.playeranalytics.forge.core.interaction.InteractionEventHelper.trackServerInteraction;

public class MinecraftPotionInteractions {

    public static void onPotionBrewed(ItemStack[] itemStacks) {
        for (int i = 0; i < itemStacks.length; i++) {
            ItemStack itemStack = itemStacks[i];
            if (itemStack.getItem() instanceof PotionItem) {
                trackServerInteraction(
                        MinecraftInteraction.POTION_BREWED,
                        getEffectAttributes(itemStack)
                                .set("slot", i)
                );
            }
        }
    }

    public static void onPlayerCollectPotion(Player player, ItemStack itemStack) {
        trackEntityInteractionWithActor(
                player,
                MinecraftInteraction.POTION_COLLECTED,
                getEffectAttributes(itemStack)
        );
    }

    // This is fired when mobs with potion effects spawn, and Minecolonies settlers also spam this :(
    // Maybe one day there'll be a config for this to support more entities...
    public static void onPotionAdded(Player player, MobEffectInstance mobEffect) {
        trackEntityInteractionWithActor(
                player,
                MinecraftInteraction.POTION_EFFECT_ADDED,
                getEffectAttributes(mobEffect)
        );
    }

    // Minecolonies settlers also spam this :(
    // Maybe one day there'll be a config for this to support more entities...
    public static void onPotionRemoved(Player player, MobEffectInstance mobEffect) {
        trackEntityInteractionWithActor(
                player,
                MinecraftInteraction.POTION_EFFECT_REMOVED,
                mobEffect == null
                        ? new InteractionAttributes()
                        : getEffectAttributes(mobEffect)
        );
    }

    public static void onPotionExpired(Player player, MobEffectInstance mobEffect) {
        trackEntityInteractionWithActor(
                player,
                MinecraftInteraction.POTION_EFFECT_EXPIRED,
                mobEffect == null
                        ? new InteractionAttributes()
                        : getEffectAttributes(mobEffect)
        );
    }

    private static InteractionAttributes getEffectAttributes(@Nonnull ItemStack potionItemStack) {
        List<MobEffect> effects = PotionUtils.getMobEffects(potionItemStack)
                .stream()
                .map(MobEffectInstance::getEffect)
                .collect(Collectors.toList());
        List<String> beneficialEffects = effects.stream().filter(effect -> effect.getCategory() == MobEffectCategory.BENEFICIAL).map(MobEffect::getDescriptionId).collect(Collectors.toList());
        List<String> neutralEffects = effects.stream().filter(effect -> effect.getCategory() == MobEffectCategory.NEUTRAL).map(MobEffect::getDescriptionId).collect(Collectors.toList());
        List<String> harmfulEffects = effects.stream().filter(effect -> effect.getCategory() == MobEffectCategory.HARMFUL).map(MobEffect::getDescriptionId).collect(Collectors.toList());

        return new InteractionAttributes()
                .set("hasBeneficialEffect", !beneficialEffects.isEmpty())
                .setStringList("beneficialEffects", beneficialEffects)
                .set("hasNeutralEffect", !neutralEffects.isEmpty())
                .setStringList("neutralEffects", neutralEffects)
                .set("hasHarmfulEffect", !harmfulEffects.isEmpty())
                .setStringList("harmfulEffects", harmfulEffects);
    }

    private static InteractionAttributes getEffectAttributes(@Nonnull MobEffectInstance mobEffectInstance) {
        MobEffect effect = mobEffectInstance.getEffect();

        return new InteractionAttributes()
                .set("effect", effect.getDescriptionId())
                .set("effectCategory", effect.getCategory().name().toLowerCase(Locale.ROOT));
    }
}
