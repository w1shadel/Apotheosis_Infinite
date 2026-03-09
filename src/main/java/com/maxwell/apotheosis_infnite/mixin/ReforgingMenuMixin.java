package com.maxwell.apotheosis_infnite.mixin;

import com.maxwell.apotheosis_infnite.InfiniteConfig;
import com.maxwell.apotheosis_infnite.adventure.util.AffixMerger;
import dev.shadowsoffire.apotheosis.adventure.affix.reforging.ReforgingMenu;
import dev.shadowsoffire.apotheosis.adventure.socket.SocketHelper;
import dev.shadowsoffire.placebo.cap.InternalItemHandler;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ReforgingMenu.class)
public abstract class ReforgingMenuMixin {

    @Shadow protected InternalItemHandler itemInv;
    @Shadow protected InternalItemHandler choicesInv;
    @Shadow @Final protected RandomSource random;


    @Inject(method = "slotsChanged", at = @At("TAIL"))
    private void maxwell$onSlotsChanged(Container pContainer, CallbackInfo ci) {
        ItemStack input = this.itemInv.getStackInSlot(0);
        if (input.isEmpty()) return;

        for (int i = 0; i < 3; i++) {
            ItemStack output = this.choicesInv.getStackInSlot(i);
            if (!output.isEmpty() && output.hasTag()) {
                int totalSockets = SocketHelper.getSockets(input) + SocketHelper.getSockets(output);
                SocketHelper.setSockets(output, Math.min(InfiniteConfig.MAX_SOCKETS.get(), totalSockets));
                AffixMerger.mergeAffixes(input, output, this.random.nextLong());
            }
        }
    }
}