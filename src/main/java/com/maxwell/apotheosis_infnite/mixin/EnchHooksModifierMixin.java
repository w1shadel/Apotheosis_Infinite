package com.maxwell.apotheosis_infnite.mixin;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.maxwell.apotheosis_infnite.InfiniteConfig;
import dev.shadowsoffire.apotheosis.ench.asm.EnchHooks;
import net.minecraft.world.item.enchantment.Enchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = EnchHooks.class, remap = false)
public class EnchHooksModifierMixin {

    @ModifyReturnValue(method = "getMaxLevel", at = @At("RETURN"))
    private static int apoth_modifyMaxLevel(int original, Enchantment ench) {
        return InfiniteConfig.MAX_ENCHANTMENT_LEVEL.get();
    }
}