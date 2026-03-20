package com.maxwell.apotheosis_infnite.mixin;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.maxwell.apotheosis_infnite.InfiniteConfig;
import dev.shadowsoffire.apotheosis.ench.asm.EnchHooks;
import net.minecraft.world.item.enchantment.Enchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = EnchHooks.class, remap = false)
public class EnchHooksModifierMixin {

    @ModifyReturnValue(method = {"getMaxLevel", "getMaxLootLevel"}, at = @At("RETURN"))
    private static int apoth_modifyInfiniteLevels(int original, Enchantment ench) {
        if (ench == null) return original;

        if (ench.isCurse() && !InfiniteConfig.ALLOW_HIGH_LEVEL_CURSES.get()) {
            return 1;
        }

        return Math.min(InfiniteConfig.MAX_ENCHANTMENT_LEVEL.get(), 255);
    }
}