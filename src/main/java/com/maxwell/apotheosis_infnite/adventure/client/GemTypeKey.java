package com.maxwell.apotheosis_infnite.adventure.client;

import dev.shadowsoffire.apotheosis.adventure.loot.LootRarity;
import dev.shadowsoffire.apotheosis.adventure.socket.gem.Gem;

public record GemTypeKey(Gem gem, LootRarity rarity) {
}