package com.maxwell.apothic_infnite;

import net.minecraftforge.common.ForgeConfigSpec;
import java.util.List;

public class InfiniteConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.IntValue MAX_SOCKETS;
    public static final ForgeConfigSpec.BooleanValue ENABLE_ALL_ITEMS_REFORGE;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> REFORGEABLE_CATEGORY_LIST;
    public static final ForgeConfigSpec.IntValue MAX_ENCHANTMENT_LEVEL;
    public static final ForgeConfigSpec.BooleanValue ENABLE_INFINITE_ENCHANTING; 
    public static final ForgeConfigSpec.BooleanValue ENABLE_WORLD_DESTRUCTION;
    public static final ForgeConfigSpec.BooleanValue ALLOW_HIGH_LEVEL_CURSES;
    public static final ForgeConfigSpec.DoubleValue MAX_AFFIX_LEVEL;
    public static final ForgeConfigSpec.DoubleValue MIN_AFFIX_LEVEL;

    static {
        BUILDER.comment("==================================================",
                " Apotheosis: Infinite Configuration ",
                "==================================================",
                "").push("General Settings");

        BUILDER.push("Sockets");
        MAX_SOCKETS = BUILDER.comment("The maximum number of sockets that can be inherited and combined during the reforging process.")
                .defineInRange("max_sockets", 50, 1, Integer.MAX_VALUE);
        BUILDER.pop();

        BUILDER.comment("--------------------------------------------------").push("Affixes");
        MAX_AFFIX_LEVEL = BUILDER.comment(
                        "The maximum effectiveness multiplier for affixes.",
                        "1.0 represents the original intended maximum (100%).",
                        "Increasing this value allows affixes to be upgraded beyond their limits,",
                        "resulting in exponentially higher stats.")
                .defineInRange("max_affix_level", 10.0, 1.0, Double.MAX_VALUE);

        MIN_AFFIX_LEVEL = BUILDER.comment("The minimum effectiveness level for affixes.")
                .defineInRange("min_affix_level", 0.0, 0.0, Double.MAX_VALUE);
        BUILDER.pop();

        BUILDER.comment("--------------------------------------------------").push("Reforging");
        ENABLE_ALL_ITEMS_REFORGE = BUILDER.comment(
                        "If true, items that do not naturally support Apotheosis affixes",
                        "(including items from other mods) will be eligible for reforging.")
                .define("enable_all_items_reforge", true);

        REFORGEABLE_CATEGORY_LIST = BUILDER.comment(
                        "A list of LootCategory IDs to be utilized when 'enable_all_items_reforge' is active.",
                        "You can remove IDs to blacklist them, or add custom IDs from other mods.")
                .defineList("reforgeable_category_list",
                        List.of("bow", "crossbow", "pickaxe", "shovel", "heavy_weapon", "helmet", "chestplate", "leggings", "boots", "shield", "trident", "sword"),
                        obj -> obj instanceof String);
        BUILDER.pop();

        BUILDER.comment("--------------------------------------------------").push("Enchanting");
        MAX_ENCHANTMENT_LEVEL = BUILDER.comment("The global maximum level for all enchantments.")
                .defineInRange("max_enchantment_level", 100, 1, 255);

        ENABLE_INFINITE_ENCHANTING = BUILDER.comment(
                        "If true, items can be enchanted repeatedly even if they already possess enchantments,",
                        "and applying the same enchantment will stack its level additively.")
                .define("enable_infinite_enchanting", true);

        ALLOW_HIGH_LEVEL_CURSES = BUILDER.comment(
                        "Determines if Curse enchantments can be upgraded up to 'max_enchantment_level'.",
                        "If false, Curses will be locked to level 1 to minimize their negative impact",
                        "while allowing other enchantments to scale infinitely.")
                .define("allow_high_level_curses", false);
        BUILDER.pop();

        BUILDER.pop();

        BUILDER.comment("==================================================",
                " DANGER ZONE / JOKES ",
                "==================================================").push("Jokes");

        ENABLE_WORLD_DESTRUCTION = BUILDER.comment(
                        "CRITICAL WARNING:",
                        "If enabled, dying while holding an item with a Curse of Vanishing level",
                        "of 100 or higher will PERMANENTLY DELETE THE WORLD.",
                        "",
                        "The developer is not responsible for any loss of data. Use at your own risk.")
                .define("enable_world_destruction", false);

        BUILDER.pop(); 

        SPEC = BUILDER.build();
    }
}