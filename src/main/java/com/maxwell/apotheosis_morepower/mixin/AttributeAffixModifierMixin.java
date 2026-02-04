package com.maxwell.apotheosis_morepower.mixin;

import dev.shadowsoffire.apotheosis.adventure.affix.AttributeAffix;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;

@Mixin(value = AttributeAffix.ModifierInst.class, remap = false)
public class AttributeAffixModifierMixin {
    @Inject(method = "getOrCreateUUID", at = @At("HEAD"), cancellable = true)
    private static void stackAllStats(ItemStack stack, ResourceLocation id, CallbackInfoReturnable<UUID> cir) {
        cir.setReturnValue(UUID.randomUUID());
    }
}