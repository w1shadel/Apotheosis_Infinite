package com.maxwell.apothic_infnite.mixin.addon;

import com.maxwell.apothic_infnite.InfiniteConfig;
import com.maxwell.apothic_infnite.adventure.util.AffixMerger;
import dev.shadowsoffire.apotheosis.adventure.socket.SocketHelper;
import dev.shadowsoffire.placebo.cap.InternalItemHandler;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "com.mira.esotericreforging.screen.EsotericReforgingMenu", remap = false)
public abstract class EsotericReforgingMenuMixin {
    @Shadow(remap = false)
    protected InternalItemHandler itemInv;
    @Shadow(remap = false)
    protected InternalItemHandler choicesInv;
    @Shadow(remap = false)
    @Final
    protected RandomSource random;

    @Dynamic
    @Inject(method = "m_6199_", at = @At("TAIL"), remap = false)
    private void lovevivi$onSlotsChanged(Container pContainer, CallbackInfo ci) {
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