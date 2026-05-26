package com.maxwell.apothic_infnite.mixin.augment;

import com.maxwell.apothic_infnite.InfiniteConfig;
import dev.shadowsoffire.apotheosis.adventure.affix.augmenting.AugmentingMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(value = AugmentingMenu.class, remap = false)
public class AugmentingMenuMixin {

    @ModifyConstant(method = "clickMenuButton", constant = @Constant(floatValue = 1.0F, ordinal = 0), remap = true)
    private float expandMenuLevelLimit(float original) {
        return InfiniteConfig.MAX_AFFIX_LEVEL.get().floatValue();
    }
}