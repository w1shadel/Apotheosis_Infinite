package com.maxwell.apotheosis_infnite;

import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(AFP.MODID)
public class AFP {
    public static final String MODID = "apotheosis_infnite";

    public AFP(FMLJavaModLoadingContext context) {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, InfiniteConfig.SPEC, "apo_inf-common.toml");
    }
}
