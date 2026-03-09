package com.maxwell.apotheosis_infnite.mixin;

import com.maxwell.apotheosis_infnite.adventure.client.GemGroupEntry;
import com.maxwell.apotheosis_infnite.adventure.client.GemTooltipRenderer;
import com.maxwell.apotheosis_infnite.adventure.client.GemTypeKey;
import dev.shadowsoffire.apotheosis.adventure.client.SocketTooltipRenderer;
import dev.shadowsoffire.apotheosis.adventure.socket.gem.GemInstance;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.*;

@Mixin(value = SocketTooltipRenderer.class, priority = Integer.MAX_VALUE)
public abstract class SocketTooltipGroupingMixin {
    @Shadow @Final private int spacing;
    private List<GemGroupEntry> lovevivi$entries;
    private boolean lovevivi$isGrouped = false;

    @Inject(method = "<init>", at = @At("RETURN"), remap = false)
    private void lovevivi$onInit(SocketTooltipRenderer.SocketComponent comp, CallbackInfo ci) {
        try {
            if (comp == null || comp.gems() == null || comp.gems().gems() == null || comp.gems().size() < 4) return;
            this.lovevivi$entries = new ArrayList<>();
            Map<GemTypeKey, Integer> counts = new LinkedHashMap<>();
            Map<GemTypeKey, GemInstance> examples = new HashMap<>();
            for (GemInstance inst : comp.gems().gems()) {
                GemTypeKey key;
                if (!inst.isValid() || inst.gem() == null || !inst.gem().isBound() || inst.rarity() == null || !inst.rarity().isBound()) {
                    key = new GemTypeKey(null, null);
                } else {
                    key = new GemTypeKey(inst.gem().get(), inst.rarity().get());
                }
                counts.put(key, counts.getOrDefault(key, 0) + 1);
                examples.putIfAbsent(key, inst);
            }
            counts.forEach((k, v) -> this.lovevivi$entries.add(new GemGroupEntry(examples.get(k), v)));
            this.lovevivi$isGrouped = true;
        } catch (Throwable t) {
            this.lovevivi$isGrouped = false;
        }
    }

    @Inject(method = "getHeight", at = @At("RETURN"), cancellable = true, require = 0)
    private void lovevivi$modifyHeight(CallbackInfoReturnable<Integer> cir) {
        if (lovevivi$isGrouped && lovevivi$entries != null) {
            cir.setReturnValue(this.spacing * lovevivi$entries.size());
        }
    }

    @Inject(method = "renderImage", at = @At("HEAD"), cancellable = true, require = 0)
    private void lovevivi$renderImage(Font font, int x, int y, GuiGraphics gfx, CallbackInfo ci) {
        if (lovevivi$isGrouped && lovevivi$entries != null) {
            ci.cancel();
            GemTooltipRenderer.render(lovevivi$entries, this.spacing, x, y, gfx);
        }
    }

    @Inject(method = "renderText", at = @At("HEAD"), cancellable = true, require = 0)
    private void lovevivi$renderText(Font f, int x, int y, Matrix4f m, MultiBufferSource.BufferSource b, CallbackInfo ci) {
        if (lovevivi$isGrouped && lovevivi$entries != null) {
            ci.cancel();
            GemTooltipRenderer.renderText(lovevivi$entries, this.spacing, x, y, m, b, f);
        }
    }
}