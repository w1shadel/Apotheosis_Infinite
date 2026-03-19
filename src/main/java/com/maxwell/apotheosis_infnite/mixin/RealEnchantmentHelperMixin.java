package com.maxwell.apotheosis_infnite.mixin;

import dev.shadowsoffire.apotheosis.ench.table.RealEnchantmentHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Collections;
import java.util.Map;

@Mixin(value = RealEnchantmentHelper.class, remap = false)
public class RealEnchantmentHelperMixin {

    @Redirect(
            method = "selectEnchantment",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;getEnchantments(Lnet/minecraft/world/item/ItemStack;)Ljava/util/Map;"
            )
    )
    private static Map<Enchantment, Integer> apoth_forceNoExistingEnchants(ItemStack stack) {
        return Collections.emptyMap();
    }
}