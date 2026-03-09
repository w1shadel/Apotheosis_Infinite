package com.maxwell.apotheosis_infnite.mixin;

import com.maxwell.apotheosis_infnite.adventure.util.NameBuilder;
import dev.shadowsoffire.apotheosis.adventure.loot.LootCategory;
import dev.shadowsoffire.apotheosis.adventure.loot.LootController;
import dev.shadowsoffire.apotheosis.adventure.loot.LootRarity;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LootController.class)
public class LootControllerMixin {
    @Inject(
            method = "createLootItem(Lnet/minecraft/world/item/ItemStack;Ldev/shadowsoffire/apotheosis/adventure/loot/LootCategory;Ldev/shadowsoffire/apotheosis/adventure/loot/LootRarity;Lnet/minecraft/util/RandomSource;)Lnet/minecraft/world/item/ItemStack;",
            at = @At("RETURN"),
            remap = false
    )
    private static void lovevivi$injectInfinitePrefixes(ItemStack stack, LootCategory cat, LootRarity rarity, RandomSource rand, CallbackInfoReturnable<ItemStack> cir) {
        ItemStack resultStack = cir.getReturnValue();
        if (resultStack != null && !resultStack.isEmpty()) {
            long seed = rand.nextLong();
            long random = seed != 0 ? seed : System.currentTimeMillis();
            NameBuilder.buildAndApplyInfiniteName(resultStack, rarity, random);
        }

    }
}