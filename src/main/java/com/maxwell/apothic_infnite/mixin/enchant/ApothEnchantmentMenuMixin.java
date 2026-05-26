package com.maxwell.apothic_infnite.mixin.enchant;

import dev.shadowsoffire.apotheosis.ench.table.ApothEnchantmentMenu;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ApothEnchantmentMenu.class, remap = false)
public class ApothEnchantmentMenuMixin {
    @Inject(method = "isEnchantableEnough", at = @At("RETURN"), cancellable = true)
    private static void allowEnchantingEvenIfEnchanted(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(true);
    }
}