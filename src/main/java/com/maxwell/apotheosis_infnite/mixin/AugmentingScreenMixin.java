package com.maxwell.apotheosis_infnite.mixin;

import com.maxwell.apotheosis_infnite.InfiniteConfig;
import dev.shadowsoffire.apotheosis.adventure.affix.augmenting.AugmentingScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(value = AugmentingScreen.class, remap = false)
public class AugmentingScreenMixin {

    @ModifyConstant(method = {"updateCachedState", "renderBg"}, constant = @Constant(floatValue = 1.0F))
    private float expandScreenLevelLimit(float original) {
        return InfiniteConfig.MAX_AFFIX_LEVEL.get().floatValue();
    }
}