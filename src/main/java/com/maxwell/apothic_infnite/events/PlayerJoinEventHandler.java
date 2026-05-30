package com.maxwell.apothic_infnite.events;

import com.maxwell.apothic_infnite.AFP;
import com.maxwell.apothic_infnite.init.ModItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = AFP.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PlayerJoinEventHandler {

    private static final String GIVEN_CHECKER_KEY = "apothic_infnite.given_config_checker";

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();
        if (!player.level().isClientSide) {
            CompoundTag persistentData = player.getPersistentData();
            CompoundTag forgeData = persistentData.getCompound(Player.PERSISTED_NBT_TAG);
            if (!forgeData.getBoolean(GIVEN_CHECKER_KEY)) {
                ItemStack stack = new ItemStack(ModItems.CONFIG_CHECKER.get());
                if (!player.getInventory().add(stack)) {
                    player.drop(stack, false);
                }
                forgeData.putBoolean(GIVEN_CHECKER_KEY, true);
                persistentData.put(Player.PERSISTED_NBT_TAG, forgeData);
            }
        }
    }
}