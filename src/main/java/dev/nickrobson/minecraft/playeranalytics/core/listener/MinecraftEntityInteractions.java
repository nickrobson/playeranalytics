package dev.nickrobson.minecraft.playeranalytics.core.listener;

import dev.nickrobson.minecraft.playeranalytics.core.api.interaction.InteractionAttributes;
import dev.nickrobson.minecraft.playeranalytics.core.api.interaction.minecraft.MinecraftInteraction;
import dev.nickrobson.minecraft.playeranalytics.core.interaction.InteractionAttributeHelper;
import dev.nickrobson.minecraft.playeranalytics.core.interaction.InteractionEventHelper;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;

import static dev.nickrobson.minecraft.playeranalytics.core.interaction.InteractionEventHelper.trackEntityInteraction;
import static dev.nickrobson.minecraft.playeranalytics.core.interaction.InteractionEventHelper.trackEntityInteractionWithActor;
import static dev.nickrobson.minecraft.playeranalytics.core.interaction.InteractionEventHelper.trackEntityInteractionWithSubject;

public class MinecraftEntityInteractions {

    public static void onTeleport(Entity entity, TeleportCause teleportCause) {
        InteractionEventHelper.trackEntityInteractionWithActor(
                entity,
                MinecraftInteraction.ENTITY_TELEPORTED,
                new InteractionAttributes()
                        .set("teleportCause", teleportCause.name().toLowerCase())
        );
    }

    public static void onAnimalTame(Player player, Animal animal) {
        InteractionEventHelper.trackEntityInteractionWithActor(
                player,
                MinecraftInteraction.ANIMAL_TAMED,
                new InteractionAttributes()
                        .set("type", InteractionAttributeHelper.getEntityType(animal))
        );
    }

    public static void onBabyEntitySpawn(Player causedByPlayer, Entity parentA, Entity parentB, Entity child) {
        // We only track babies spawned as a result of players' actions
        if (causedByPlayer == null)
            return;
        // We only track baby animals
        if (!(parentA instanceof Animal) || !(parentB instanceof Animal))
            return;
        if (child == null)
            return;

        InteractionEventHelper.trackEntityInteractionWithActor(
                causedByPlayer,
                MinecraftInteraction.ANIMAL_BRED,
                new InteractionAttributes()
                        .set("type", InteractionAttributeHelper.getEntityType(child))
        );
    }

    public static void onLivingEntityEquip(Entity entity, EquipmentSlot slot, ItemStack oldItemStack, ItemStack newItemStack) {
        // For now, we only care about players, to avoid spamming when mobs spawn
        if (!(entity instanceof Player))
            return;

        // Ignore players changing their held items
        if (slot == EquipmentSlot.MAINHAND || slot == EquipmentSlot.OFFHAND)
            return;

        InteractionEventHelper.trackEntityInteractionWithActor(
                entity,
                MinecraftInteraction.ENTITY_EQUIPPED,
                new InteractionAttributes()
                        .set("slot", slot.getName())
                        .set("from", InteractionAttributeHelper.getItemStackType(oldItemStack))
                        .set("to", InteractionAttributeHelper.getItemStackType(newItemStack))
        );
    }

    public static void onStruckByLightning(Entity entity) {
        InteractionEventHelper.trackEntityInteractionWithSubject(
                entity,
                MinecraftInteraction.ENTITY_STRUCK_BY_LIGHTNING
        );
    }

    // Mowzie's Mobs' Ferrous Wroughtnaut sends 20 heal events a second!
    // So we're disabling this because of the spam :(
    // Maybe one day we'll be able to add support for other entity types using a config!
    public static void onPlayerHeal(Player player, double amount) {
        InteractionEventHelper.trackEntityInteractionWithSubject(
                player,
                MinecraftInteraction.ENTITY_HEALED,
                new InteractionAttributes()
                        .set("amount", amount)
        );
    }

    public static void onEntityAttack(Player attacker, Entity target) {
        InteractionEventHelper.trackEntityInteraction(
                attacker,
                target,
                MinecraftInteraction.ENTITY_ATTACKED
        );
    }

    public static void onLivingEntityDamaged(Entity entity, double amount, DamageSource damageSource) {
        boolean isPlayerDeath = entity instanceof Player;
        boolean isCausedByPlayer = isPlayerDamageSource(damageSource);
        // To prevent spamming analytics when e.g. the sun comes out and all the undead die,
        // when bats randomly fly into lava, when enderman get hurt by rain, etc. etc.,
        // we only send this analytic when players die or kill
        if (!(isPlayerDeath || isCausedByPlayer))
            return;

        InteractionAttributes attributes = new InteractionAttributes()
                .set("amount", amount);

        InteractionAttributeHelper.populateDamageSourceAttributes(damageSource, attributes);

        InteractionEventHelper.trackEntityInteraction(
                damageSource.getEntity(),
                entity,
                MinecraftInteraction.ENTITY_DAMAGED,
                attributes
        );
    }

    public static void onLivingEntityDeath(Entity entity, DamageSource damageSource) {
        boolean isPlayerDeath = entity instanceof Player;
        boolean isCausedByPlayer = isPlayerDamageSource(damageSource);
        // To prevent spamming analytics when e.g. the sun comes out
        // and all the undead die, we only send this analytic when players die or kill
        if (!(isPlayerDeath || isCausedByPlayer))
            return;

        InteractionAttributes attributes = new InteractionAttributes();

        InteractionAttributeHelper.populateDamageSourceAttributes(damageSource, attributes);

        InteractionEventHelper.trackEntityInteraction(
                damageSource.getEntity(),
                entity,
                MinecraftInteraction.ENTITY_DIED,
                attributes
        );
    }

    private static boolean isPlayerDamageSource(DamageSource damageSource) {
        if (damageSource.getEntity() instanceof Player) {
            return true;
        }
        if (damageSource.getEntity() instanceof Projectile) {
            Entity projectileShooter = ((Projectile) damageSource.getEntity()).getOwner();
            return projectileShooter instanceof Player;
        }
        return false;
    }

    public static void onProjectileImpact(Projectile projectile) {
        InteractionEventHelper.trackEntityInteraction(
                projectile.getOwner(),
                projectile,
                MinecraftInteraction.PROJECTILE_IMPACTED,
                new InteractionAttributes().set("type", InteractionAttributeHelper.getEntityType(projectile))
        );
    }

    public enum TeleportCause {
        CHORUS_FRUIT,
        ENDER_ABILITY,
        ENDER_PEARL,
        COMMAND,
        UNKNOWN,
    }
}
