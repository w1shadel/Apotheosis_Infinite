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
        boolean allowHighLevelCurses = InfiniteConfig.SPEC.isLoaded()
                ? InfiniteConfig.ALLOW_HIGH_LEVEL_CURSES.get()
                : InfiniteConfig.ALLOW_HIGH_LEVEL_CURSES.getDefault();
        if (self.isCurse() && !allowHighLevelCurses) {
            cir.setReturnValue(1);
            return;
        }
        int configMax = InfiniteConfig.SPEC.isLoaded()
                ? InfiniteConfig.MAX_ENCHANTMENT_LEVEL.get()
                : InfiniteConfig.MAX_ENCHANTMENT_LEVEL.getDefault();
        cir.setReturnValue(Math.min(configMax, 255));
    }
}