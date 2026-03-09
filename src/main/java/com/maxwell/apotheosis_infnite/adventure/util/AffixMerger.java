package com.maxwell.apotheosis_infnite.adventure.util;

import dev.shadowsoffire.apotheosis.adventure.affix.Affix;
import dev.shadowsoffire.apotheosis.adventure.affix.AffixHelper;
import dev.shadowsoffire.apotheosis.adventure.affix.AffixInstance;
import dev.shadowsoffire.apotheosis.adventure.loot.LootRarity;
import dev.shadowsoffire.placebo.reload.DynamicHolder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.HashMap;
import java.util.Map;

public class AffixMerger {

    public static void mergeAffixes(ItemStack input, ItemStack output, long rand) {
        if (input == null || output == null || input.isEmpty() || output.isEmpty()) return;

        try {
            // 1. スポナーの情報を保護（BlockEntityTagをコピー）
            if (input.is(Items.SPAWNER) && input.hasTag() && input.getTag().contains("BlockEntityTag")) {
                output.getOrCreateTag().put("BlockEntityTag", input.getTag().getCompound("BlockEntityTag").copy());
            }

            // 2. アフィックスの抽出と合算
            Map<DynamicHolder<? extends Affix>, AffixInstance> oldAffixes = AffixHelper.getAffixes(input);
            Map<DynamicHolder<? extends Affix>, AffixInstance> newAffixes = AffixHelper.getAffixes(output);
            Map<DynamicHolder<? extends Affix>, AffixInstance> merged = new HashMap<>();

            if (oldAffixes != null) {
                merged.putAll(oldAffixes);
            }

            if (newAffixes != null) {
                for (Map.Entry<DynamicHolder<? extends Affix>, AffixInstance> entry : newAffixes.entrySet()) {
                    DynamicHolder<? extends Affix> holder = entry.getKey();
                    if (holder == null || !holder.isBound()) continue;

                    if (merged.containsKey(holder)) {
                        float summedLevel = merged.get(holder).level() + entry.getValue().level();
                        merged.put(holder, new AffixInstance(holder, output, entry.getValue().rarity(), summedLevel));
                    } else {
                        merged.put(holder, entry.getValue());
                    }
                }
            }

            // 3. レアリティの同期
            DynamicHolder<LootRarity> inR = AffixHelper.getRarity(input);
            DynamicHolder<LootRarity> outR = AffixHelper.getRarity(output);
            LootRarity finalRarity = (outR != null && outR.isBound()) ? outR.get() : (inR != null && inR.isBound() ? inR.get() : null);

            if (finalRarity != null) {
                AffixHelper.setRarity(output, finalRarity);
            }

            // 4. 合算データの適用
            AffixHelper.setAffixes(output, merged);

            // 5. 名前の無限連結を適用
            NameBuilder.buildAndApplyInfiniteName(output, finalRarity, rand);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}