package dev.nickrobson.minecraft.playeranalytics.forge.listener;

import dev.nickrobson.minecraft.playeranalytics.forge.core.listener.MinecraftEntityInteractions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraftforge.event.entity.EntityStruckByLightningEvent;
import net.minecraftforge.event.entity.EntityTeleportEvent;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.AnimalTameEvent;
import net.minecraftforge.event.entity.living.BabyEntitySpawnEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static dev.nickrobson.minecraft.playeranalytics.forge.listener.ForgeEventUtil.isEventCancelled;

public class MinecraftForgeEntityListener {

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onTeleport(EntityTeleportEvent event) {
        if (isEventCancelled(event))
            return;

        MinecraftEntityInteractions.TeleportCause teleportCause = MinecraftEntityInteractions.TeleportCause.UNKNOWN;
        if (event instanceof EntityTeleportEvent.ChorusFruit) {
            teleportCause = MinecraftEntityInteractions.TeleportCause.CHORUS_FRUIT;
        } else if (event instanceof EntityTeleportEvent.EnderEntity) {
            teleportCause = MinecraftEntityInteractions.TeleportCause.ENDER_ABILITY;
        } else if (event instanceof EntityTeleportEvent.EnderPearl) {
            teleportCause = MinecraftEntityInteractions.TeleportCause.ENDER_PEARL;
        } else if (event instanceof EntityTeleportEvent.TeleportCommand || event instanceof EntityTeleportEvent.SpreadPlayersCommand) {
            teleportCause = MinecraftEntityInteractions.TeleportCause.COMMAND;
        }

        MinecraftEntityInteractions.onTeleport(event.getEntity(), teleportCause);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onAnimalTame(AnimalTameEvent event) {
        if (isEventCancelled(event))
            return;

        MinecraftEntityInteractions.onAnimalTame(event.getTamer(), event.getAnimal());
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onBabyEntitySpawn(BabyEntitySpawnEvent event) {
        if (isEventCancelled(event))
            return;

        MinecraftEntityInteractions.onBabyEntitySpawn(
                event.getCausedByPlayer(),
                event.getParentA(),
                event.getParentB(),
                event.getChild()
        );
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onLivingEntityEquip(LivingEquipmentChangeEvent event) {
        if (isEventCancelled(event))
            return;

        MinecraftEntityInteractions.onLivingEntityEquip(
                event.getEntity(),
                event.getSlot(),
                event.getFrom(),
                event.getTo()
        );
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onStruckByLightning(EntityStruckByLightningEvent event) {
        if (isEventCancelled(event))
            return;

        MinecraftEntityInteractions.onStruckByLightning(
                event.getEntity()
        );
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onLivingEntityHeal(LivingHealEvent event) {
        if (isEventCancelled(event))
            return;

        if (!(event.getEntity() instanceof Player))
            // Mowzie's Mobs' Ferrous Wroughtnaut sends 20 heal events a second!
            // So we're disabling this because of the spam :(
            // Maybe one day I'll add a config for this!
            return;

        MinecraftEntityInteractions.onPlayerHeal(
                (Player) event.getEntity(),
                event.getAmount()
        );
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onEntityAttack(AttackEntityEvent event) {
        if (isEventCancelled(event))
            return;

        MinecraftEntityInteractions.onEntityAttack(
                event.getPlayer(),
                event.getTarget()
        );
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onLivingEntityDamaged(LivingDamageEvent event) {
        if (isEventCancelled(event))
            return;

        MinecraftEntityInteractions.onLivingEntityDamaged(
                event.getEntity(),
                event.getAmount(),
                event.getSource()
        );
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onLivingEntityDeath(LivingDeathEvent event) {
        if (isEventCancelled(event))
            return;

        MinecraftEntityInteractions.onLivingEntityDeath(
                event.getEntity(),
                event.getSource()
        );
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onProjectileImpact(ProjectileImpactEvent event) {
        if (isEventCancelled(event))
            return;

        if (!(event.getEntity() instanceof Projectile))
            return;

        MinecraftEntityInteractions.onProjectileImpact(
                (Projectile) event.getEntity()
        );
    }
}