package com.maxwell.apothic_infnite.mixin.affix;

import com.maxwell.apothic_infnite.InfiniteConfig;
import dev.shadowsoffire.apotheosis.adventure.affix.AttributeAffix;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(value = AttributeAffix.class, remap = false)
public class AttributeAffixMixin {

    @ModifyConstant(method = "getAugmentingText", constant = @Constant(floatValue = 1.0F))
    private float expandAttributeRangeDisplay(float original) {
        return InfiniteConfig.MAX_AFFIX_LEVEL.get().floatValue();
    }
}