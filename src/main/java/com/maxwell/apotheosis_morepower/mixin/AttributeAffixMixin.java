package com.maxwell.apotheosis_morepower.mixin;

import dev.shadowsoffire.apotheosis.adventure.affix.AttributeAffix;
import dev.shadowsoffire.apotheosis.adventure.loot.LootRarity;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = AttributeAffix.class, remap = false)
public class AttributeAffixMixin {

    // 表示上の数値をインフレさせる
    @ModifyVariable(method = "getAugmentingText", at = @At("HEAD"), argsOnly = true)
    private float multiplyLevelForTooltip(float level, ItemStack stack, LootRarity rarity) {
        int count = stack.getOrCreateTag().getInt("ReforgeCount");
        return level * (count + 1);
    }
}