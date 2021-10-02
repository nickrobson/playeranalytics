package dev.nickrobson.minecraft.playeranalytics.core.interaction;

import dev.nickrobson.minecraft.playeranalytics.core.api.interaction.InteractionAttributes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;

import javax.annotation.Nonnull;
import java.util.Optional;

public final class InteractionAttributeHelper {
    private InteractionAttributeHelper() {
    }

    private static final String ATTRIBUTE_TYPE = "type";

    public static InteractionAttributes getBlockAttributes(@Nonnull Block block) {
        return new InteractionAttributes()
                .set(ATTRIBUTE_TYPE, getBlockType(block));
    }

    public static InteractionAttributes getBlockAttributes(@Nonnull Level level, @Nonnull BlockPos blockPos) {
        return getBlockAttributes(level.getBlockState(blockPos));
    }

    public static InteractionAttributes getBlockAttributes(@Nonnull BlockState blockState) {
        return getBlockAttributes(blockState.getBlock());
    }

    public static String getBlockType(@Nonnull Block block) {
        return block.getDescriptionId();
    }

    public static String getBlockType(@Nonnull BlockState blockState) {
        return getBlockType(blockState.getBlock());
    }

    public static String getBlockType(@Nonnull Level level, @Nonnull BlockPos blockPos) {
        return getBlockType(level.getBlockState(blockPos));
    }

    public static InteractionAttributes getContainerAttributes(@Nonnull AbstractContainerMenu containerMenu) {
        InteractionAttributes attributes = new InteractionAttributes()
                .set("slots", containerMenu.slots.size());

        try {
            // this method can throw if the container can't be opened from a menu
            MenuType<?> menuType = containerMenu.getType();
            ResourceLocation resourceLocation = Registry.MENU.getKey(menuType);
            if (resourceLocation != null) {
                attributes.set(ATTRIBUTE_TYPE, resourceLocation.toString());
            }
        } catch (UnsupportedOperationException ignored) {
        }

        return attributes;
    }

    public static InteractionAttributes getItemStackAttributes(@Nonnull ItemStack itemStack) {
        return new InteractionAttributes()
                .set(ATTRIBUTE_TYPE, getItemStackType(itemStack))
                .set("stackSize", itemStack.getCount());
    }

    public static String getItemStackType(@Nonnull ItemStack itemStack) {
        return itemStack.getDescriptionId();
    }

    public static String getToolType(@Nonnull ItemStack itemStack) {
        return getToolType(itemStack.getItem());
    }

    public static String getToolType(@Nonnull Item item) {
        if (item instanceof AxeItem) {
            return "axe";
        } else if (item instanceof HoeItem) {
            return "hoe";
        } else if (item instanceof PickaxeItem) {
            return "pickaxe";
        } else if (item instanceof ShovelItem) {
            return "shovel";
        } else if (item instanceof SwordItem) {
            return "sword";
        }
        // If the class name is meaningful, we try to use it to guess the name of the tool type :P
        if (item.getClass().getSimpleName().endsWith("Item")) {
            return item.getClass().getSimpleName()
                    .replaceFirst("Item$", "") // CoolToolItem -> CoolTool
                    .replaceAll("([A-Z])", "_$1") // CoolTool -> Cool_Tool
                    .toLowerCase(); // Cool_Tool -> cool_tool
        }
        return null;
    }

    public static InteractionAttributes getItemAttributes(@Nonnull Item item) {
        return new InteractionAttributes()
                .set(ATTRIBUTE_TYPE, getItemType(item));
    }

    public static String getItemType(@Nonnull Item item) {
        return item.getDescriptionId();
    }

    public static String getEntityType(@Nonnull Entity entity) {
        return entity.getType().getDescriptionId();
    }

    public static String getEntityDisplayName(@Nonnull Entity entity) {
        return entity.getDisplayName().getString();
    }

    public static String getEntityName(@Nonnull Entity entity) {
        if (entity instanceof Player) {
            return getPlayerName((Player) entity);
        }
        return entity.getType().getDescription().getString();
    }

    public static String getPlayerName(@Nonnull Player entity) {
        return entity.getGameProfile().getName();
    }

    public static String getDimensionId(@Nonnull Level level) {
        return getDimensionId(level.dimension());
    }

    public static String getDimensionId(@Nonnull ResourceKey<Level> dimension) {
        return dimension.location().toString();
    }

    public static Optional<String> getBiomeId(@Nonnull Level level, @Nonnull BlockPos blockPos) {
        return level.getBiomeName(blockPos)
                .map(key -> key.location().toString());
    }

    public static String getBiomeCategory(@Nonnull Level level, @Nonnull BlockPos blockPos) {
        return level.getBiome(blockPos).getBiomeCategory().getName();
    }

    public static String getFluidId(@Nonnull Fluid fluid) {
        return Registry.FLUID.getKey(fluid).toString();
    }

    public static void populateDamageSourceAttributes(@Nonnull DamageSource damageSource, @Nonnull InteractionAttributes attributes) {
        attributes.set("damageSource", damageSource.msgId);

        Entity entity = damageSource.getEntity();
        Entity directEntity = damageSource.getDirectEntity();
        if (entity != null) {
            attributes.set("damager", getEntityType(entity));
            if (entity instanceof Player) {
                attributes.set("damagerPlayerName", getPlayerName((Player) entity));
            }
        }
        if (directEntity != null && (entity == null || !directEntity.is(entity))) {
            attributes.set("directDamager", getEntityType(directEntity));
        }
    }
}
