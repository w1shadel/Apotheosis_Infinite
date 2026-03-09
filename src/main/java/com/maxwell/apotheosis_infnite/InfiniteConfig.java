package com.maxwell.apotheosis_infnite;

import net.minecraftforge.common.ForgeConfigSpec;

public class InfiniteConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;
    public static final ForgeConfigSpec.IntValue MAX_SOCKETS;
    public static final ForgeConfigSpec.BooleanValue ENABLE_ALL_ITEMS_REFORGE;

    static {
        BUILDER.push("General Settings");
        MAX_SOCKETS = BUILDER.comment("Maximum number of sockets that can be combined when reforging").defineInRange("max_sockets", 50, 1, Integer.MAX_VALUE);
        ENABLE_ALL_ITEMS_REFORGE = BUILDER.comment("Will items that cannot be reborn be reforged?").define("enable_all_items_reforge", true);
        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
