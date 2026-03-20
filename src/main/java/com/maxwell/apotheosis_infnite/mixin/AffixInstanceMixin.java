package com.maxwell.apotheosis_infnite.mixin;

import com.maxwell.apotheosis_infnite.InfiniteConfig;
import dev.shadowsoffire.apotheosis.adventure.affix.AffixInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(value = AffixInstance.class, remap = false)
public class AffixInstanceMixin {
    @ModifyConstant(method = "withNewLevel", constant = @Constant(floatValue = 1.0F))
    private float liftInternalLevelClamp(float original) {
        return InfiniteConfig.MAX_AFFIX_LEVEL.get().floatValue();
    }
}