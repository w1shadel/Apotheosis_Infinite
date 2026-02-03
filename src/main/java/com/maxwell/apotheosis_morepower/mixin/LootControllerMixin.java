package com.maxwell.apotheosis_morepower.mixin;

import dev.shadowsoffire.apotheosis.adventure.affix.Affix;
import dev.shadowsoffire.apotheosis.adventure.affix.AffixHelper;
import dev.shadowsoffire.apotheosis.adventure.affix.AffixInstance;
import dev.shadowsoffire.apotheosis.adventure.loot.LootCategory;
import dev.shadowsoffire.apotheosis.adventure.loot.LootController;
import dev.shadowsoffire.apotheosis.adventure.loot.LootRarity;
import dev.shadowsoffire.apotheosis.adventure.loot.RarityRegistry;
import dev.shadowsoffire.apotheosis.adventure.socket.SocketHelper;
import dev.shadowsoffire.placebo.reload.DynamicHolder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.mutable.MutableInt;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.List;

@Mixin(value = LootController.class, remap = false)
public class LootControllerMixin {

    // スレッドごとに古い能力を一時保存するためのバッファ
    @Unique
    private static final ThreadLocal<Map<DynamicHolder<? extends Affix>, AffixInstance>> OLD_AFFIXES = ThreadLocal.withInitial(HashMap::new);

    /**
     * リフォージの「直前」に今の能力をバックアップする
     */
    @Inject(method = "createLootItem(Lnet/minecraft/world/item/ItemStack;Ldev/shadowsoffire/apotheosis/adventure/loot/LootCategory;Ldev/shadowsoffire/apotheosis/adventure/loot/LootRarity;Lnet/minecraft/util/RandomSource;)Lnet/minecraft/world/item/ItemStack;",
            at = @At("HEAD"))
    private static void captureOldAffixes(ItemStack stack, LootCategory cat, LootRarity rarity, RandomSource rand, CallbackInfoReturnable<ItemStack> cir) {
        OLD_AFFIXES.set(new HashMap<>(AffixHelper.getAffixes(stack)));
    }

    /**
     * 新しい能力が決まった後、バックアップしておいた能力と合算（マージ）する
     */
    @Inject(method = "createLootItem(Lnet/minecraft/world/item/ItemStack;Ldev/shadowsoffire/apotheosis/adventure/loot/LootCategory;Ldev/shadowsoffire/apotheosis/adventure/loot/LootRarity;Lnet/minecraft/util/RandomSource;)Lnet/minecraft/world/item/ItemStack;",
            at = @At(value = "INVOKE", target = "Ldev/shadowsoffire/apotheosis/adventure/affix/AffixHelper;setName(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/network/chat/Component;)V"),
            locals = LocalCapture.CAPTURE_FAILHARD,
            cancellable = true)
    private static void finalMergeAndInfiniteName(ItemStack stack, LootCategory cat, LootRarity rarity, RandomSource rand, CallbackInfoReturnable<ItemStack> cir, Set selected, MutableInt sockets, float durability, Map<DynamicHolder<? extends Affix>, AffixInstance> loaded, List nameList, String key, MutableComponent name) {

        Map<DynamicHolder<? extends Affix>, AffixInstance> backup = OLD_AFFIXES.get();
        DynamicHolder<LootRarity> currentRarity = RarityRegistry.INSTANCE.holder(rarity);

        // 1. バックアップした古い能力を新しいリスト(loaded)にマージ
        backup.forEach((affix, oldInst) -> {
            if (loaded.containsKey(affix)) {
                // 同じ能力ならレベルを足し算（累積強化）
                float combinedLevel = oldInst.level() + loaded.get(affix).level();
                loaded.put(affix, new AffixInstance((DynamicHolder)affix, stack, currentRarity, combinedLevel));
            } else {
                // 抽選されなかった能力も、消さずに引き継ぐ
                loaded.put(affix, oldInst);
            }
        });

        // 2. 名前の再構築（重複を防ぎつつ無限に伸ばす）
        // アイテム本体の名前（翻訳キーから取得）
        Component baseName = Component.translatable(stack.getItem().getDescriptionId());
        MutableComponent prefixChain = Component.empty();

        for (AffixInstance inst : loaded.values()) {
            // 接頭辞(形容詞)のみを抽出して繋げる
            String adj = inst.getName(true).getString().replace(baseName.getString(), "").trim();
            if (!adj.isEmpty()) prefixChain.append(Component.literal(adj)).append(" ");
        }

        MutableComponent finalName = prefixChain.append(baseName);
        finalName.withStyle(Style.EMPTY.withColor(rarity.getColor()));

        // 3. ソケット加算
        SocketHelper.setSockets(stack, SocketHelper.getSockets(stack) + sockets.getValue());

        // 4. 最終保存
        AffixHelper.setAffixes(stack, loaded);
        AffixHelper.setName(stack, finalName);

        // バックアップの消去
        OLD_AFFIXES.remove();

        cir.setReturnValue(stack);
    }
}