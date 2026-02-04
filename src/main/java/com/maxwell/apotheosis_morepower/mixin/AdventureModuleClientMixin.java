package com.maxwell.apotheosis_morepower.mixin;

import dev.shadowsoffire.apotheosis.adventure.affix.Affix;
import dev.shadowsoffire.apotheosis.adventure.affix.AffixHelper;
import dev.shadowsoffire.apotheosis.adventure.affix.AffixInstance;
import dev.shadowsoffire.apotheosis.adventure.client.AdventureModuleClient;
import dev.shadowsoffire.apotheosis.adventure.loot.LootRarity;
import dev.shadowsoffire.placebo.reload.DynamicHolder;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.*;
import java.util.function.Consumer;

@Mixin(value = AdventureModuleClient.class, remap = false)
public class AdventureModuleClientMixin {
    /**
     * @author Maxwell
     * @reason 大量のアフィックス説明文（黄色い文字）を、種類ごとにレベルを合算して1行にまとめる
     */
    @Inject(method = "affixTooltips", at = @At("HEAD"), cancellable = true)
    private static void consolidateAffixTooltips(ItemTooltipEvent e, CallbackInfo ci) {
        ci.cancel();
        ItemStack stack = e.getItemStack();
        if (!stack.hasTag()) return;
        Map<DynamicHolder<? extends Affix>, AffixInstance> affixes = AffixHelper.getAffixes(stack);
        Map<ResourceLocation, Float> levelMap = new HashMap<>();
        Map<ResourceLocation, AffixInstance> representativeMap = new HashMap<>();
        for (AffixInstance inst : affixes.values()) {
            ResourceLocation id = inst.affix().getId();
            levelMap.put(id, levelMap.getOrDefault(id, 0f) + inst.level());
            representativeMap.putIfAbsent(id, inst);
        }
        List<Component> components = new ArrayList<>();
        Consumer<Component> dotPrefixer = afxComp -> {
            components.add(Component.translatable("text.apotheosis.dot_prefix", afxComp).withStyle(ChatFormatting.YELLOW));
        };
        representativeMap.values().stream()
                .sorted(Comparator.comparingInt(a -> a.affix().get().getType().ordinal()))
                .forEach(inst -> {
                    ResourceLocation id = inst.affix().getId();
                    float totalLevel = levelMap.get(id);
                    Affix affix = inst.affix().get();
                    LootRarity rarity = inst.rarity().get();
                    Component desc = affix.getDescription(stack, rarity, totalLevel);
                    if (desc.getContents() != ComponentContents.EMPTY) {
                        dotPrefixer.accept(desc);
                    }
                });
        e.getToolTip().addAll(1, components);
    }
}