package com.maxwell.apotheosis_infnite.adventure.util;


import dev.shadowsoffire.apotheosis.adventure.affix.Affix;
import dev.shadowsoffire.apotheosis.adventure.affix.AffixHelper;
import dev.shadowsoffire.apotheosis.adventure.affix.AffixInstance;
import dev.shadowsoffire.apotheosis.adventure.loot.LootRarity;
import dev.shadowsoffire.placebo.reload.DynamicHolder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.ItemStack;

import java.util.*;

public class NameBuilder {
    public static void buildAndApplyInfiniteName(ItemStack stack, LootRarity rarity, long randomSeed) {
        if (stack == null || stack.isEmpty()) {
            return;
        }
        Map<DynamicHolder<? extends Affix>, AffixInstance> affixes = AffixHelper.getAffixes(stack);
        if (affixes == null || affixes.isEmpty()) {
            return;
        }
        List<AffixInstance> nameList = new ArrayList<>();
        for (Map.Entry<DynamicHolder<? extends Affix>, AffixInstance> entry : affixes.entrySet()) {
                if (entry.getKey() != null && entry.getKey().isBound() && entry.getValue() != null) {
                    Component testPrefix = entry.getValue().getName(true);
                    if (testPrefix != null) {
                        nameList.add(entry.getValue());
                    }
                }
        }
        if (nameList.isEmpty()) {
            return;
        }
        Random jRand = new Random(randomSeed);
        Collections.shuffle(nameList, jRand);
        MutableComponent prefix = Component.empty();
        int prefixCount = nameList.size() > 1 ? nameList.size() - 1 : 1;
        for (int i = 0; i < prefixCount; i++) {
            if (i > 0) {
                prefix.append(Component.literal(" "));
            }
            prefix.append(nameList.get(i).getName(true));
        }
        Object suffix = "";
        if (nameList.size() > 1) {
                Component testSuffix = nameList.get(nameList.size() - 1).getName(false);
                if (testSuffix != null) {
                    suffix = testSuffix;
                }
        }
        String key = nameList.size() > 1 ? "misc.apotheosis.affix_name.three" : "misc.apotheosis.affix_name.two";
        Style style = rarity != null ? Style.EMPTY.withColor(rarity.getColor()) : Style.EMPTY;
        MutableComponent customName = Component.translatable(key, prefix, "", suffix).withStyle(style);
        AffixHelper.setName(stack, customName);
    }
}