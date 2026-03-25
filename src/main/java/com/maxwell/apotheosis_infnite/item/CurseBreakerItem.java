package com.maxwell.apotheosis_infnite.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CurseBreakerItem extends Item {

    public CurseBreakerItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean overrideStackedOnOther(ItemStack scroll, Slot slot, ClickAction action, Player player) {
        if (action != ClickAction.SECONDARY || slot.getItem().isEmpty()) {
            return false;
        }

        ItemStack target = slot.getItem();
        Map<Enchantment, Integer> enchants = EnchantmentHelper.getEnchantments(target);

        List<Enchantment> curses = enchants.keySet().stream()
                .filter(Enchantment::isCurse)
                .toList();

        if (curses.isEmpty()) {
            return false;
        }

        Map<Enchantment, Integer> cleanedEnchants = enchants.entrySet().stream()
                .filter(entry -> !entry.getKey().isCurse())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        EnchantmentHelper.setEnchantments(cleanedEnchants, target);

        if (!player.getAbilities().instabuild) {
            scroll.shrink(1);
        }

        player.level().playSound(null, player.blockPosition(), SoundEvents.TOTEM_USE, SoundSource.PLAYERS, 0.5F, 1.5F);
        return true;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("tooltip.apotheosis_infinite.curse_breaker.desc").withStyle(ChatFormatting.GOLD));
        tooltip.add(Component.translatable("tooltip.apotheosis_infinite.curse_breaker.usage").withStyle(ChatFormatting.GRAY));
    }
}