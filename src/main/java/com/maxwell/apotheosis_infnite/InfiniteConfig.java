package com.maxwell.apotheosis_infnite;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;

public class InfiniteConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;
    public static final ForgeConfigSpec.IntValue MAX_SOCKETS;
    public static final ForgeConfigSpec.BooleanValue ENABLE_ALL_ITEMS_REFORGE;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> REFORGEABLE_CATEGORY_LIST;

    static {
        BUILDER.push("General Settings");
        MAX_SOCKETS = BUILDER.comment("Maximum number of sockets that can be combined when reforging")
                .defineInRange("max_sockets", 50, 1, Integer.MAX_VALUE);
        ENABLE_ALL_ITEMS_REFORGE = BUILDER.comment("Will items that cannot be reborn be reforged?")
                .define("enable_all_items_reforge", true);
        REFORGEABLE_CATEGORY_LIST = BUILDER.comment("List of LootCategory IDs to be used when 'enable_all_items_reforge' is active.",
                        "You can remove IDs to 'BAN' them, or add IDs from other mods.")
                .defineList("reforgeable_category_list",
                        List.of("bow", "crossbow", "pickaxe", "shovel", "heavy_weapon", "helmet", "chestplate", "leggings", "boots", "shield", "trident", "sword"),
                        obj -> obj instanceof String);
        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}