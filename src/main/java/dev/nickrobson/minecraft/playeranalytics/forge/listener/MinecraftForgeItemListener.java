package dev.nickrobson.minecraft.playeranalytics.forge.listener;

import dev.nickrobson.minecraft.playeranalytics.core.listener.MinecraftItemInteractions;
import dev.nickrobson.minecraft.playeranalytics.forge.PlayerAnalyticsMod;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.AnvilRepairEvent;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.event.entity.player.ItemFishedEvent;
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;

import javax.annotation.Nullable;

import static dev.nickrobson.minecraft.playeranalytics.core.interaction.InteractionAttributeHelper.getItemStackType;
import static dev.nickrobson.minecraft.playeranalytics.forge.listener.ForgeEventUtil.isEventCancelled;

public class MinecraftForgeItemListener {

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onAnvilRepair(AnvilRepairEvent event) {
        if (isEventCancelled(event))
            return;

        MinecraftItemInteractions.onAnvilRepair(
                event.getPlayer(),
                event.getItemInput(),
                event.getIngredientInput(),
                event.getItemResult()
        );
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onItemPickup(PlayerEvent.ItemPickupEvent event) {
        if (isEventCancelled(event))
            return;

        MinecraftItemInteractions.onItemPickup(
                event.getPlayer(),
                event.getStack()
        );
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onItemToss(ItemTossEvent event) {
        if (isEventCancelled(event))
            return;

        MinecraftItemInteractions.onItemToss(
                event.getPlayer(),
                event.getEntityItem().getItem()
        );
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onItemCraft(PlayerEvent.ItemCraftedEvent event) {
        if (isEventCancelled(event))
            return;

        MinecraftItemInteractions.onItemCraft(
                event.getPlayer(),
                event.getCrafting()
        );
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onItemSmelt(PlayerEvent.ItemSmeltedEvent event) {
        if (isEventCancelled(event))
            return;

        MinecraftItemInteractions.onItemSmelt(
                event.getPlayer(),
                event.getSmelting()
        );
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onItemFish(ItemFishedEvent event) {
        if (isEventCancelled(event))
            return;

        event.getDrops().forEach(itemStack ->
                MinecraftItemInteractions.onItemFish(
                        event.getPlayer(),
                        itemStack
                )
        );
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onItemUse(LivingEntityUseItemEvent.Finish event) {
        if (isEventCancelled(event))
            return;

        MinecraftItemInteractions.onItemUse(
                event.getEntity(),
                event.getItem()
        );
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onItemDestroy(PlayerDestroyItemEvent event) {
        if (isEventCancelled(event))
            return;

        MinecraftItemInteractions.onItemDestroy(
                event.getPlayer(),
                event.getOriginal()
        );
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onItemRightClick(PlayerInteractEvent.RightClickItem event) {
        if (isEventCancelled(event))
            return;

        MinecraftItemInteractions.onItemRightClick(
                event.getPlayer(),
                event.getItemStack()
        );
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (isEventCancelled(event))
            return;

        MinecraftItemInteractions.onRightClickBlock(
                event.getPlayer(),
                event.getItemStack(),
                event.getWorld().getBlockState(event.getPos())
        );
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onRightClickEntity(PlayerInteractEvent.EntityInteractSpecific event) {
        if (isEventCancelled(event))
            return;

        MinecraftItemInteractions.onRightClickEntity(
                event.getPlayer(),
                event.getItemStack(),
                event.getTarget()
        );
    }

    // despite what the event name & javadoc says, this event is also fired when emptying a bucket!
    // TODO: revisit: can we find what fluids are being filled & emptied with the bucket?
    // disabled for now because it's broken!!
    // @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onBucketUse(FillBucketEvent event) {
        if (isEventCancelled(event))
            return;

        if (event.getWorld().isClientSide) return; // TEMP
        HitResult rayTraceResult = event.getTarget();
        if (rayTraceResult == null || rayTraceResult.getType() == HitResult.Type.MISS)
            return;

        ItemStack oldBucketStack = event.getEmptyBucket();
        if (!(oldBucketStack.getItem() instanceof BucketItem))
            return;
        Fluid oldFluid = getFluid(oldBucketStack);
        if (oldFluid == null) {
            PlayerAnalyticsMod.logger.debug(
                    "Ignoring FillBucketEvent as I couldn't find the Fluid in the original bucket"
            );
            return;
        }

        Fluid targetedFluid = null;
        int targetedFluidAmount = 0;
        if (rayTraceResult.getType() == HitResult.Type.BLOCK) {
            BlockHitResult blockHitResult = (BlockHitResult) rayTraceResult;
            BlockPos blockPos = oldFluid == Fluids.EMPTY
                    ? blockHitResult.getBlockPos() // pick up the liquid that's being interacted with
                    : blockHitResult.getBlockPos().relative(blockHitResult.getDirection()); // the fluid that's on this side of the block being interacted with
            BlockState blockState = event.getWorld().getBlockState(blockPos);
            FluidState fluidState = blockState.getFluidState();
            targetedFluid = fluidState.getType();
            targetedFluidAmount = fluidState.getAmount();
        }

        if (oldFluid != Fluids.EMPTY) {
            if (targetedFluid != null) {
                MinecraftItemInteractions.onBucketEmpty(
                        event.getPlayer(),
                        oldBucketStack,
                        oldFluid
                );
            }
            return;
        }

        // FillBucketEvent#getFilledBucket() is wrongly annotated as @Nonnull - it's not actually set by default so it's null if no mod intervenes!
        // ...so we need to do our own calculation of what will happen if we grab the fluid...
        ItemStack newBucketStack = event.getFilledBucket();
        //noinspection ConstantConditions - getFilledBucket() is wrongly annotated as @Nonnull
        if (newBucketStack == null) {
            if (targetedFluid == null) {
                return;
            }
            int amount = Math.min(targetedFluidAmount, FluidAttributes.BUCKET_VOLUME);
            newBucketStack = FluidUtil.getFilledBucket(new FluidStack(targetedFluid, amount));
        }

        Fluid newFluid = getFluid(newBucketStack);
        if (newFluid == null) {
            PlayerAnalyticsMod.logger.debug(
                    "Ignoring FillBucketEvent as I couldn't find the Fluid in the resulting bucket (after: {}  {})",
                    getItemStackType(newBucketStack),
                    "<null>"
            );
            return;
        }

        MinecraftItemInteractions.onBucketFill(
                event.getPlayer(),
                newBucketStack,
                newFluid
        );
    }

    @Nullable
    private static Fluid getFluid(@Nullable ItemStack bucketStack) {
        if (bucketStack == null || !(bucketStack.getItem() instanceof BucketItem bucketItem))
            return null;

        return bucketItem.getFluid();
    }

}