package com.maxwell.apotheosis_morepower.mixin;

import dev.shadowsoffire.apotheosis.adventure.affix.Affix;
import dev.shadowsoffire.apotheosis.adventure.affix.AffixHelper;
import dev.shadowsoffire.apotheosis.adventure.affix.AffixInstance;
import dev.shadowsoffire.apotheosis.adventure.affix.AttributeAffix;
import dev.shadowsoffire.apotheosis.adventure.loot.LootRarity;
import dev.shadowsoffire.placebo.reload.DynamicHolder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;
import java.util.UUID;

import dev.shadowsoffire.apotheosis.adventure.affix.Affix;
import dev.shadowsoffire.apotheosis.adventure.affix.AffixHelper;
import dev.shadowsoffire.apotheosis.adventure.affix.AffixInstance;
import dev.shadowsoffire.apotheosis.adventure.affix.AttributeAffix;
import dev.shadowsoffire.apotheosis.adventure.loot.LootRarity;
import dev.shadowsoffire.attributeslib.AttributesLib;
import dev.shadowsoffire.attributeslib.api.IFormattableAttribute;
import dev.shadowsoffire.placebo.reload.DynamicHolder;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import java.util.Map;

@Mixin(value = AttributeAffix.class, remap = false)
public abstract class AttributeAffixMixin {

    @Shadow @Final protected Attribute attribute;
    @Shadow @Final protected Operation operation;
    @Shadow @Final protected Map<LootRarity, AttributeAffix.ModifierInst> modifiers;

    /**
     * @author Maxwell
     * @reason 表記上の数値も合計レベルで計算し直す
     */
    @Overwrite
    public Component getAugmentingText(ItemStack stack, LootRarity rarity, float level) {
        AttributeAffix self = (AttributeAffix) (Object) this;
        float totalLevel = 0;

        // アイテム全体の同じアフィックスのレベルを合算
        var affixes = AffixHelper.getAffixes(stack);
        for (var inst : affixes.values()) {
            if (inst.affix().getId().equals(self.getId())) totalLevel += inst.level();
        }

        var modif = this.modifiers.get(rarity);
        if (modif == null) return Component.empty();

        // 合算レベルで威力を算出
        double value = modif.valueFactory().get(totalLevel);
        MutableComponent valueComp = IFormattableAttribute.toValueComponent(this.attribute, this.operation, Math.abs(value), AttributesLib.getTooltipFlag());

        return value > 0.0D
                ? Component.translatable("attributeslib.modifier.plus", valueComp, Component.translatable(this.attribute.getDescriptionId())).withStyle(ChatFormatting.BLUE)
                : Component.translatable("attributeslib.modifier.take", valueComp, Component.translatable(this.attribute.getDescriptionId())).withStyle(ChatFormatting.RED);
    }
}