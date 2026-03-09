package com.maxwell.apotheosis_infnite.mixin;

import com.maxwell.apotheosis_infnite.InfiniteConfig;
import dev.shadowsoffire.apotheosis.adventure.loot.LootCategory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(value = LootCategory.class, remap = false)
public class LootCategoryMixin {

    @Inject(method = "forItem", at = @At("HEAD"), cancellable = true)
    private static void maxwell$makeEverythingReforgeable(ItemStack stack, CallbackInfoReturnable<LootCategory> cir) {
        if (!InfiniteConfig.ENABLE_ALL_ITEMS_REFORGE.get()) return;
        if (!stack.isEmpty()) {
            LootCategory original = LootCategory.VALUES.stream()
                    .filter(c -> c.isValid(stack))
                    .findFirst()
                    .orElse(LootCategory.NONE);

            if (original == LootCategory.NONE) {
                List<LootCategory> validCats = LootCategory.VALUES.stream()
                        .filter(c -> c != LootCategory.NONE)
                        .toList();

                int hash = Math.abs(stack.getItem().getDescriptionId().hashCode());
                LootCategory pseudoCat = validCats.get(hash % validCats.size());
                cir.setReturnValue(pseudoCat);
            }
        }
    }
}