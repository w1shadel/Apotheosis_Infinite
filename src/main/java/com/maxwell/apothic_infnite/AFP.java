package com.maxwell.apothic_infnite;

import com.maxwell.apothic_infnite.init.ModItems;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(AFP.MODID)
public class AFP {
    public static final String MODID = "apothic_infnite";

    public AFP(FMLJavaModLoadingContext context) {
        ModItems.ITEMS.register(context.getModEventBus());
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, InfiniteConfig.SPEC, "apo_inf-common.toml");
    }
}
