package com.maxwell.apothic_infnite.events;

import com.maxwell.apothic_infnite.AFP;
import com.maxwell.apothic_infnite.InfiniteConfig;
import net.minecraft.CrashReport;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import java.io.File;
import java.io.FileOutputStream;

@Mod.EventBusSubscriber(modid = AFP.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class WorldDestroyer {

    @SubscribeEvent
    public static void onPlayerDeath(LivingDeathEvent event) {
        if (!InfiniteConfig.ENABLE_WORLD_DESTRUCTION.get()) return;
        if (!(event.getEntity() instanceof ServerPlayer player)) return;

        boolean found = false;
        for (ItemStack stack : player.getAllSlots()) {
            if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.VANISHING_CURSE, stack) > 100) {
                found = true;
                break;
            }
        }

        if (found) {
            DoomedData data = DoomedData.get(player.serverLevel());
            data.setDoomed(true);
            player.getServer().saveEverything(true, true, true);

            player.connection.disconnect(Component.translatable("message.apotheosis_infnite.lol"));
        }
    }
    @SubscribeEvent
    public static void onWorldLoad(LevelEvent.Load event) {
        if (event.getLevel().isClientSide()) return;
        if (!(event.getLevel() instanceof ServerLevel level)) return;

        if (DoomedData.get(level).isDoomed()) {
            File worldDir = level.getServer().getWorldPath(LevelResource.ROOT).toFile();
            File levelDat = new File(worldDir, "level.dat");

            // level.dat をゴミデータで上書き破壊
            if (levelDat.exists()) {
                try (FileOutputStream fos = new FileOutputStream(levelDat)) {
                    byte[] junk = new byte[4096];
                    new java.util.Random().nextBytes(junk);
                    fos.write(junk);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            // 強制クラッシュ
            Minecraft.getInstance().crash(new CrashReport("消滅の呪いにより世界は崩壊しました。", new RuntimeException("World Corrupted")));
        }
    }
}