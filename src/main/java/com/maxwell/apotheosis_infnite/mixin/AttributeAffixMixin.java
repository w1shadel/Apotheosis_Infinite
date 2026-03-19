package com.maxwell.apotheosis_infnite.mixin;

import com.maxwell.apotheosis_infnite.InfiniteConfig;
import dev.shadowsoffire.apotheosis.adventure.affix.AttributeAffix;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(value = AttributeAffix.class, remap = false)
public class AttributeAffixMixin {

    // getAugmentingText 内の modif.valueFactory.get(1.0F) の 1.0F を書き換え
    // これにより強化画面の [Min - Max] の表示上限がコンフィグ通りに伸びる
    @ModifyConstant(method = "getAugmentingText", constant = @Constant(floatValue = 1.0F))
    private float expandAttributeRangeDisplay(float original) {
        return InfiniteConfig.MAX_AFFIX_LEVEL.get().floatValue();
    }
}