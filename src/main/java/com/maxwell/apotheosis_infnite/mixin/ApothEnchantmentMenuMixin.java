package com.maxwell.apotheosis_infnite.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.maxwell.apotheosis_infnite.InfiniteConfig;
import dev.shadowsoffire.apotheosis.ench.table.ApothEnchantmentMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Mixin(value = ApothEnchantmentMenu.class, remap = false)
public class ApothEnchantmentMenuMixin {

    @ModifyReturnValue(method = "isEnchantableEnough", at = @At("RETURN"))
    private static boolean allowAllEnchantable(boolean original) {
        return true;
    }

    @ModifyReturnValue(method = "getEnchantmentList", at = @At("RETURN"))
    private List<EnchantmentInstance> modifyEnchantmentList(List<EnchantmentInstance> original, ItemStack stack) {
        if (original == null || original.isEmpty()) return original;

        Map<Enchantment, Integer> existingEnchants = EnchantmentHelper.getEnchantments(stack);
        List<EnchantmentInstance> modifiedList = new ArrayList<>();

        int configMax = InfiniteConfig.MAX_ENCHANTMENT_LEVEL.get();
        int safeLimit = Math.min(configMax, 255);
        boolean allowHighLevelCurses = InfiniteConfig.ALLOW_HIGH_LEVEL_CURSES.get();

        for (EnchantmentInstance inst : original) {
            int newLevel;
            if (inst.enchantment.isCurse() && !allowHighLevelCurses) {
                newLevel = 1;
            } else {
                int currentLevel = existingEnchants.getOrDefault(inst.enchantment, 0);
                newLevel = Math.min(currentLevel + inst.level, safeLimit);
            }
            modifiedList.add(new EnchantmentInstance(inst.enchantment, newLevel));
        }

        return modifiedList;
    }
}