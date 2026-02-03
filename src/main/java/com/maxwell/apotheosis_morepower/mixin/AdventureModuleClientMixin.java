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
        // 元の処理（全行表示）をキャンセル
        ci.cancel();

        ItemStack stack = e.getItemStack();
        if (!stack.hasTag()) return;

        // 1. 全アフィックスを取得
        Map<DynamicHolder<? extends Affix>, AffixInstance> affixes = AffixHelper.getAffixes(stack);

        // 2. IDごとにレベルを集計する
        Map<ResourceLocation, Float> levelMap = new HashMap<>();
        Map<ResourceLocation, AffixInstance> representativeMap = new HashMap<>();

        for (AffixInstance inst : affixes.values()) {
            ResourceLocation id = inst.affix().getId();

            // レベルを加算
            levelMap.put(id, levelMap.getOrDefault(id, 0f) + inst.level());

            // 代表インスタンスを保持（説明文生成のため）
            representativeMap.putIfAbsent(id, inst);
        }

        // 3. ソートして表示用リストを作成
        List<Component> components = new ArrayList<>();
        Consumer<Component> dotPrefixer = afxComp -> {
            components.add(Component.translatable("text.apotheosis.dot_prefix", afxComp).withStyle(ChatFormatting.YELLOW));
        };

        // アフィックスタイプ順などでソート（見た目を整える）
        representativeMap.values().stream()
                .sorted(Comparator.comparingInt(a -> a.affix().get().getType().ordinal()))
                .forEach(inst -> {
                    ResourceLocation id = inst.affix().getId();
                    float totalLevel = levelMap.get(id);

                    // ★重要：合算したレベルを使って説明文を生成する
                    // これにより「移動速度上昇 I」が10個あっても、「移動速度上昇 X」の1行になる
                    Affix affix = inst.affix().get();
                    LootRarity rarity = inst.rarity().get(); // レアリティは代表のものを使用

                    Component desc = affix.getDescription(stack, rarity, totalLevel);

                    // 空の説明文（AttributeModifierなど）は除外
                    if (desc.getContents() != ComponentContents.EMPTY) {
                        dotPrefixer.accept(desc);
                    }
                });

        // 4. ツールチップに追加
        e.getToolTip().addAll(1, components);
    }
}