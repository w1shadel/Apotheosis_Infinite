package com.maxwell.apothic_infnite.mixin.enchant;

import com.maxwell.apothic_infnite.InfiniteConfig;
import dev.shadowsoffire.apotheosis.ench.table.RealEnchantmentHelper;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.*;

@Mixin(value = RealEnchantmentHelper.class, remap = false)
public class RealEnchantmentHelperMixin {

    @ModifyVariable(method = "selectEnchantment", at = @At(value = "STORE", ordinal = 0), remap = false, name = "enchants")
    private static Map<Enchantment, Integer> bypassExistingEnchantFilter(Map<Enchantment, Integer> original) {
        return Collections.emptyMap();
    }

    @Inject(method = "selectEnchantment", at = @At("RETURN"), cancellable = true)
    private static void apoth_applyAdditiveLevels(RandomSource pRandom, ItemStack pStack, int pLevel, float quanta, float arcana, float rectification, boolean treasure, Set<Enchantment> blacklist, CallbackInfoReturnable<List<EnchantmentInstance>> cir) {
        List<EnchantmentInstance> original = cir.getReturnValue();
        if (original == null || original.isEmpty()) return;

        Map<Enchantment, Integer> existing = EnchantmentHelper.getEnchantments(pStack);
        List<EnchantmentInstance> modified = new ArrayList<>();
        int safeLimit = Math.min(InfiniteConfig.MAX_ENCHANTMENT_LEVEL.get(), 255);
        boolean allowCurses = InfiniteConfig.ALLOW_HIGH_LEVEL_CURSES.get();

        for (EnchantmentInstance inst : original) {
            int newLevel;
            if (inst.enchantment.isCurse() && !allowCurses) {
                newLevel = 1;
            } else {
                int current = existing.getOrDefault(inst.enchantment, 0);
                newLevel = Math.min(current + inst.level, safeLimit);
            }
            modified.add(new EnchantmentInstance(inst.enchantment, newLevel));
        }
        cir.setReturnValue(modified);
    }
}