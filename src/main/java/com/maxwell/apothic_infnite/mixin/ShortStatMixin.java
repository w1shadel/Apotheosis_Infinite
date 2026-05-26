package com.maxwell.apothic_infnite.mixin;

import dev.shadowsoffire.apotheosis.spawn.modifiers.SpawnerStat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(targets = "dev.shadowsoffire.apotheosis.spawn.modifiers.SpawnerStats$ShortStat", remap = false)
public abstract class ShortStatMixin {
    @ModifyVariable(method = "apply(Ljava/lang/Short;Ljava/lang/Short;Ljava/lang/Short;Ldev/shadowsoffire/apotheosis/spawn/spawner/ApothSpawnerTile;)Z", at = @At("HEAD"), ordinal = 1, argsOnly = true)
    private Short lovevivi$overrideMin(Short originalMin) {
        String statId = ((SpawnerStat<?>) this).getId();
        return (short) (statId != null && statId.contains("delay") ? 1 : 0);
    }

    @ModifyVariable(method = "apply(Ljava/lang/Short;Ljava/lang/Short;Ljava/lang/Short;Ldev/shadowsoffire/apotheosis/spawn/spawner/ApothSpawnerTile;)Z", at = @At("HEAD"), ordinal = 2, argsOnly = true)
    private Short lovevivi$overrideMax(Short originalMax) {
        return (short) 32767;
    }
}