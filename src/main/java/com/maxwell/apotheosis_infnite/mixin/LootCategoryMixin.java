package com.maxwell.apotheosis_infnite.mixin;

import com.maxwell.apotheosis_infnite.InfiniteConfig;
import dev.shadowsoffire.apotheosis.adventure.loot.LootCategory;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Objects;

@Mixin(value = LootCategory.class, remap = false)
public class LootCategoryMixin {
    @Inject(method = "forItem", at = @At("HEAD"), cancellable = true)
    private static void lovevivi$makeEverythingReforgeable(ItemStack stack, CallbackInfoReturnable<LootCategory> cir) {
        if (!InfiniteConfig.ENABLE_ALL_ITEMS_REFORGE.get() || stack.isEmpty()) return;
        LootCategory original = LootCategory.VALUES.stream()
                .filter(c -> c.isValid(stack))
                .findFirst()
                .orElse(LootCategory.NONE);
        if (original == LootCategory.NONE) {
            List<LootCategory> pool = lovevivi$getEnabledCategories();
            if (!pool.isEmpty()) {
                int hash = Math.abs(stack.getItem().getDescriptionId().hashCode());
                LootCategory pseudoCat = pool.get(hash % pool.size());
                cir.setReturnValue(pseudoCat);
            }
        }
    }

    @Unique
    private static List<LootCategory> lovevivi$getEnabledCategories() {
        return InfiniteConfig.REFORGEABLE_CATEGORY_LIST.get().stream()
                .map(LootCategory::byId)
                .filter(Objects::nonNull)
                .filter(c -> !c.isNone())
                .toList();
    }
}