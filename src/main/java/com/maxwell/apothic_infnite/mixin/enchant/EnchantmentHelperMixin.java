package com.maxwell.apothic_infnite.mixin.enchant;

import com.maxwell.apothic_infnite.InfiniteConfig;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.LinkedHashMap;
import java.util.Map;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {

    @Inject(method = "getEnchantments(Lnet/minecraft/world/item/ItemStack;)Ljava/util/Map;", at = @At("RETURN"), cancellable = true)
    private static void apoth_capEnchantments(ItemStack stack, CallbackInfoReturnable<Map<Enchantment, Integer>> cir) {
        Map<Enchantment, Integer> original = cir.getReturnValue();
        if (original.isEmpty()) return;

        int maxLevel = InfiniteConfig.MAX_ENCHANTMENT_LEVEL.get();
        boolean allowCurses = InfiniteConfig.ALLOW_HIGH_LEVEL_CURSES.get();
        boolean changed = false;

        Map<Enchantment, Integer> capped = new LinkedHashMap<>();
        for (Map.Entry<Enchantment, Integer> entry : original.entrySet()) {
            int level = entry.getValue();
            Enchantment ench = entry.getKey();
            int cappedLevel = level;

            if (ench.isCurse() && !allowCurses) {
                cappedLevel = 1;
            } else if (level > maxLevel) {
                cappedLevel = maxLevel;
            }

            if (cappedLevel != level) {
                changed = true;
            }
            capped.put(ench, cappedLevel);
        }

        if (changed) {
            cir.setReturnValue(capped);
        }
    }

    @Inject(method = "getItemEnchantmentLevel(Lnet/minecraft/world/item/enchantment/Enchantment;Lnet/minecraft/world/item/ItemStack;)I", at = @At("RETURN"), cancellable = true)
    private static void apoth_capItemEnchantmentLevel(Enchantment enchantment, ItemStack stack, CallbackInfoReturnable<Integer> cir) {
        int original = cir.getReturnValue();
        if (original == 0) return;

        int maxLevel = InfiniteConfig.MAX_ENCHANTMENT_LEVEL.get();
        boolean allowCurses = InfiniteConfig.ALLOW_HIGH_LEVEL_CURSES.get();

        if (enchantment.isCurse() && !allowCurses) {
            cir.setReturnValue(1);
        } else if (original > maxLevel) {
            cir.setReturnValue(maxLevel);
        }
    }
}
