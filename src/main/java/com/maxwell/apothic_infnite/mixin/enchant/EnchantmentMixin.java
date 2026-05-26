package com.maxwell.apothic_infnite.mixin.enchant;

import com.maxwell.apothic_infnite.InfiniteConfig;
import net.minecraft.world.item.enchantment.Enchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Enchantment.class)
public class EnchantmentMixin {
    @Inject(method = "getMaxLevel", at = @At("RETURN"), cancellable = true)
    private void liftVanillaMaxLevel(CallbackInfoReturnable<Integer> cir) {
        Enchantment self = (Enchantment) (Object) this;

        if (self.isCurse() && !InfiniteConfig.ALLOW_HIGH_LEVEL_CURSES.get()) {
            cir.setReturnValue(1);
            return;
        }

        int configMax = InfiniteConfig.MAX_ENCHANTMENT_LEVEL.get();
        cir.setReturnValue(Math.min(configMax, 255));
    }
}