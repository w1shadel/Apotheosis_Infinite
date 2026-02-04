package com.maxwell.apotheosis_morepower.mixin;

import dev.shadowsoffire.apotheosis.adventure.socket.AddSocketsRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = AddSocketsRecipe.class, remap = true)
public class AddSocketsRecipeMixin {
    @Redirect(method = "matches", at = @At(value = "INVOKE", target = "Ldev/shadowsoffire/apotheosis/adventure/socket/AddSocketsRecipe;getMaxSockets()I", remap = false))
    private int bypassMaxSockets(AddSocketsRecipe recipe) {
        return Integer.MAX_VALUE;
    }
}