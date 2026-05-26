package com.maxwell.apothic_infnite.init;

import com.maxwell.apothic_infnite.AFP;
import com.maxwell.apothic_infnite.item.ConfigCheckerItem;
import com.maxwell.apothic_infnite.item.CurseBreakerItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, AFP.MODID);

    public static final RegistryObject<Item> CURSE_BREAKER = ITEMS.register("curse_breaker",
            () -> new CurseBreakerItem(new Item.Properties().stacksTo(16).rarity(Rarity.EPIC)));
    public static final RegistryObject<Item> CONFIG_CHECKER = ITEMS.register("config_checker",
            () -> new ConfigCheckerItem(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC).fireResistant()));
}