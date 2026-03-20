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
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Mixin(value = ApothEnchantmentMenu.class, remap = false)
public class ApothEnchantmentMenuMixin {
    @Inject(method = "isEnchantableEnough", at = @At("RETURN"), cancellable = true)
    private static void allowEnchantingEvenIfEnchanted(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(true);
    }
}