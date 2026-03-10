package com.maxwell.apotheosis_infnite.adventure.util;

import dev.shadowsoffire.apotheosis.adventure.affix.AffixHelper;
import dev.shadowsoffire.apotheosis.adventure.affix.AffixInstance;
import dev.shadowsoffire.apotheosis.adventure.loot.LootRarity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.HashMap;

public class AffixMerger {
    public static void mergeAffixes(ItemStack input, ItemStack output, long rand) {
        if (input == null || output == null || input.isEmpty() || output.isEmpty()) return;
        try {
            if (input.is(Items.SPAWNER) && input.hasTag() && input.getTag().contains("BlockEntityTag")) {
                output.getOrCreateTag().put("BlockEntityTag", input.getTag().getCompound("BlockEntityTag").copy());
            }
            var oldMap = AffixHelper.getAffixes(input);
            var newMap = AffixHelper.getAffixes(output);
            var merged = new HashMap<>(oldMap);
            newMap.forEach((holder, newInst) -> {
                if (holder != null && holder.isBound()) {
                    merged.compute(holder, (h, oldInst) -> oldInst == null
                            ? newInst
                            : new AffixInstance(h, output, newInst.rarity(), oldInst.level() + newInst.level()));
                }
            });
            LootRarity rarity = AffixHelper.getRarity(output).getOptional()
                    .orElse(AffixHelper.getRarity(input).getOptional().orElse(null));
            if (rarity != null) AffixHelper.setRarity(output, rarity);
            AffixHelper.setAffixes(output, merged);
            NameBuilder.buildAndApplyInfiniteName(output, rarity, rand);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
