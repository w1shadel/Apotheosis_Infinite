package com.maxwell.apotheosis_morepower.mixin;

import dev.shadowsoffire.apotheosis.adventure.affix.Affix;
import dev.shadowsoffire.apotheosis.adventure.affix.AffixHelper;
import dev.shadowsoffire.apotheosis.adventure.affix.AffixInstance;
import dev.shadowsoffire.apotheosis.adventure.affix.AffixRegistry;
import dev.shadowsoffire.apotheosis.adventure.loot.LootRarity;
import dev.shadowsoffire.apotheosis.adventure.loot.RarityRegistry;
import dev.shadowsoffire.placebo.reload.DynamicHolder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.*;



import dev.shadowsoffire.apotheosis.adventure.affix.Affix;
import dev.shadowsoffire.apotheosis.adventure.affix.AffixHelper;
import dev.shadowsoffire.apotheosis.adventure.affix.AffixInstance;
import dev.shadowsoffire.apotheosis.adventure.affix.AffixRegistry;
import dev.shadowsoffire.apotheosis.adventure.loot.LootRarity;
import dev.shadowsoffire.apotheosis.adventure.loot.RarityRegistry;
import dev.shadowsoffire.placebo.reload.DynamicHolder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.*;

@Mixin(value = AffixHelper.class, remap = false)
public class AffixHelperMixin {

    /**
     * @author Maxwell
     * @reason 新しいアフィックスを既存のリストに「追記」する。
     * これにより、リフォージしても古い効果が消えずに残る。
     */
    @Overwrite
    public static void setAffixes(ItemStack stack, Map<DynamicHolder<? extends Affix>, AffixInstance> newAffixes) {
        CompoundTag afxData = stack.getOrCreateTagElement("affix_data");

        // 既存のリストを取得（なければ新規作成）
        ListTag list = afxData.getList("affixes", Tag.TAG_COMPOUND);

        // 今回追加された分(newAffixes)だけをリストの後ろにくっつける
        for (AffixInstance inst : newAffixes.values()) {
            CompoundTag tag = new CompoundTag();
            tag.putString("id", inst.affix().getId().toString());
            tag.putFloat("lvl", inst.level());

            // スタック用の一意なUUIDを付与
            tag.putUUID("StackUUID", UUID.randomUUID());

            list.add(tag);
        }
        afxData.put("affixes", list);
    }

    /**
     * @author Maxwell
     * @reason 重複を許容する IdentityHashMap を使用して、全ての効果を読み込む。
     */
    @Overwrite
    public static Map<DynamicHolder<? extends Affix>, AffixInstance> getAffixesImpl(ItemStack stack) {
        if (stack.isEmpty()) return Collections.emptyMap();

        // ★重要: HashMapだとキー重複で消えるので、IdentityHashMapを使う
        // これにより、同じIDのアフィックスが何個あっても全て保持できる
        Map<DynamicHolder<? extends Affix>, AffixInstance> map = new IdentityHashMap<>();
        CompoundTag afxData = stack.getTagElement("affix_data");

        if (afxData != null && afxData.contains("affixes")) {
            ListTag list = afxData.getList("affixes", Tag.TAG_COMPOUND);
            DynamicHolder<LootRarity> rarity = AffixHelper.getRarity(afxData);
            if (!rarity.isBound()) rarity = RarityRegistry.getMinRarity();

            for (int i = 0; i < list.size(); i++) {
                CompoundTag afxTag = list.getCompound(i);
                ResourceLocation id = new ResourceLocation(afxTag.getString("id"));

                // 毎回新しいインスタンスを作ることで、IdentityHashMap上で別キーとして扱わせる
                DynamicHolder<Affix> holder = DynamicHolderAccessor.create(AffixRegistry.INSTANCE, id);

                if (holder.isBound()) {
                    map.put(holder, new AffixInstance(holder, stack, rarity, afxTag.getFloat("lvl")));
                }
            }
        }
        return Collections.unmodifiableMap(map);
    }
}