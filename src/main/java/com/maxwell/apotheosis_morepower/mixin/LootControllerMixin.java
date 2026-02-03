package com.maxwell.apotheosis_morepower.mixin;

import dev.shadowsoffire.apotheosis.adventure.affix.Affix;
import dev.shadowsoffire.apotheosis.adventure.affix.AffixHelper;
import dev.shadowsoffire.apotheosis.adventure.affix.AffixInstance;
import dev.shadowsoffire.apotheosis.adventure.affix.AffixRegistry;
import dev.shadowsoffire.apotheosis.adventure.loot.LootCategory;
import dev.shadowsoffire.apotheosis.adventure.loot.LootController;
import dev.shadowsoffire.apotheosis.adventure.loot.LootRarity;
import dev.shadowsoffire.apotheosis.adventure.socket.SocketHelper;
import dev.shadowsoffire.placebo.reload.DynamicHolder;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.mutable.MutableInt;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Mixin(value = LootController.class, remap = false)
public class LootControllerMixin {

    @Inject(method = "createLootItem(Lnet/minecraft/world/item/ItemStack;Ldev/shadowsoffire/apotheosis/adventure/loot/LootCategory;Ldev/shadowsoffire/apotheosis/adventure/loot/LootRarity;Lnet/minecraft/util/RandomSource;)Lnet/minecraft/world/item/ItemStack;",
            at = @At(value = "INVOKE", target = "Ldev/shadowsoffire/apotheosis/adventure/affix/AffixHelper;setName(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/network/chat/Component;)V"),
            locals = LocalCapture.CAPTURE_FAILHARD,
            cancellable = true)
    private static void simpleAppendMode(ItemStack stack, LootCategory cat, LootRarity rarity, RandomSource rand, CallbackInfoReturnable<ItemStack> cir, Set selected, MutableInt sockets, float durability, Map loaded, List nameList, String key, MutableComponent name) {
        Map<DynamicHolder<? extends Affix>, AffixInstance> existing = AffixHelper.getAffixes(stack);
        for (AffixInstance inst : existing.values()) {
            // 新しいHolderを作ることで重複キーとして登録
            loaded.put(DynamicHolderAccessor.create(AffixRegistry.INSTANCE, inst.affix().getId()), inst);
        }
        int rank = AffixHelper.getAffixes(stack).size();
        AffixHelper.setAffixes(stack, loaded);
        Component oldName = AffixHelper.getName(stack);
        MutableComponent finalName = name.copy();
        finalName.append(Component.literal(" (+" + rank + ")").withStyle(ChatFormatting.GOLD));

        // レアリティの色を適用
        finalName.withStyle(Style.EMPTY.withColor(rarity.getColor()));
        AffixHelper.setName(stack, finalName);
        SocketHelper.setSockets(stack, SocketHelper.getSockets(stack) + sockets.getValue());

        cir.setReturnValue(stack);
    }
}