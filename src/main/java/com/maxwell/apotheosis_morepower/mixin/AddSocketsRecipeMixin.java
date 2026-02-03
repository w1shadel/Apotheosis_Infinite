package com.maxwell.apotheosis_morepower.mixin;

import dev.shadowsoffire.apotheosis.adventure.socket.AddSocketsRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = AddSocketsRecipe.class, remap = false)
public class AddSocketsRecipeMixin {
    @Overwrite
    public int getMaxSockets() {
        return Integer.MAX_VALUE;
    }
}