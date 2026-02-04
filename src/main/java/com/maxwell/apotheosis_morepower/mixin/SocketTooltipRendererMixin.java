package com.maxwell.apotheosis_morepower.mixin;

import dev.shadowsoffire.apotheosis.adventure.client.SocketTooltipRenderer;
import dev.shadowsoffire.apotheosis.adventure.socket.gem.GemInstance;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(value = SocketTooltipRenderer.class, remap = true)
public abstract class SocketTooltipRendererMixin {
    @Shadow(remap = false)
    @Final
    private SocketTooltipRenderer.SocketComponent comp;
    @Shadow(remap = false)
    @Final
    private int spacing;
    private static final int MAX_VISIBLE = 5;
    /**
     * @author Maxwell
     * @reason ソケット数管理
     */
    @Overwrite
    public int getHeight() {
        int size = this.comp.gems().size();
        if (size > MAX_VISIBLE) {
            return (MAX_VISIBLE + 1) * spacing;
        }
        return size * spacing;
    }
    /**
     * @author Maxwell
     * @reason 同じくソケット管理関連
     */
    @Overwrite
    public int getWidth(Font font) {
        int size = this.comp.gems().size();
        int maxWidth = 0;
        int limit = Math.min(size, MAX_VISIBLE);
        for (int i = 0; i < limit; i++) {
            GemInstance inst = this.comp.gems().get(i);
            maxWidth = Math.max(maxWidth, font.width(SocketTooltipRenderer.getSocketDesc(inst)) + 12);
        }
        if (size > MAX_VISIBLE) {
            maxWidth = Math.max(maxWidth, font.width("... and " + (size - MAX_VISIBLE) + " more sockets") + 12);
        }
        return maxWidth;
    }
    /**
     * @author Maxwell
     * @reason ソケットの表示、最大5個で止める
     */
    @Overwrite
    public void renderImage(Font font, int x, int y, GuiGraphics gfx) {
        List<GemInstance> gems = this.comp.gems().gems();
        int size = gems.size();
        int limit = Math.min(size, MAX_VISIBLE);
        for (int i = 0; i < limit; i++) {
            int renderY = y + (i * spacing);
            gfx.blit(SocketTooltipRenderer.SOCKET, x, renderY, 0, 0, 0, 9, 9, 9, 9);
            GemInstance inst = gems.get(i);
            if (inst.isValid()) {
                gfx.pose().pushPose();
                gfx.pose().translate(x + 0.5f, renderY + 0.5f, 0);
                gfx.pose().scale(0.5F, 0.5F, 1);
                gfx.renderFakeItem(inst.gemStack(), 0, 0);
                gfx.pose().popPose();
            }
        }
        if (size > MAX_VISIBLE) {
            gfx.blit(SocketTooltipRenderer.SOCKET, x, y + (MAX_VISIBLE * spacing), 0, 0, 0, 9, 9, 9, 9);
        }
    }
    /**
     * @author Maxwell
     * @reason ソケット数の表示、サイズ肥大化による可視性の低下を防ぐ
     */
    @Overwrite
    public void renderText(Font font, int x, int y, Matrix4f matrix, MultiBufferSource.BufferSource buffer) {
        int size = this.comp.gems().size();
        int limit = Math.min(size, MAX_VISIBLE);
        for (int i = 0; i < limit; i++) {
            Component desc = SocketTooltipRenderer.getSocketDesc(this.comp.gems().get(i));
            font.drawInBatch(desc, x + 12, y + 1 + (this.spacing * i), 0xAABBCC, true, matrix, buffer, Font.DisplayMode.NORMAL, 0, 15728880);
        }
        if (size > MAX_VISIBLE) {
            int remaining = size - MAX_VISIBLE;
            Component more = Component.literal("... and " + remaining + " more sockets")
                    .withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC);
            font.drawInBatch(more, x + 12, y + 1 + (this.spacing * MAX_VISIBLE), 0xAABBCC, true, matrix, buffer, Font.DisplayMode.NORMAL, 0, 15728880);
        }
    }
}