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

@Mixin(value = SocketTooltipRenderer.class, remap = false)
public abstract class SocketTooltipRendererMixin {

    @Shadow
    @Final
    private SocketTooltipRenderer.SocketComponent comp;
    @Shadow @Final private int spacing;

    /**
     * @author Maxwell
     * @reason ソケットが多すぎる場合に1行に圧縮する
     */
    @Overwrite
    public int getHeight() {
        // ソケットが3個より多ければ、1行分の高さ(spacing)だけ返す
        if (this.comp.gems().size() > 3) return spacing;
        return this.comp.gems().size() * spacing;
    }

    @Overwrite
    public int getWidth(Font font) {
        if (this.comp.gems().size() > 3) return font.width("Sockets: " + this.comp.gems().size() + " Total") + 20;

        int maxWidth = 0;
        for (GemInstance inst : this.comp.gems().gems()) {
            maxWidth = Math.max(maxWidth, font.width(SocketTooltipRenderer.getSocketDesc(inst)) + 12);
        }
        return maxWidth;
    }

    @Overwrite
    public void renderImage(Font font, int x, int y, GuiGraphics gfx) {
        int size = this.comp.gems().size();
        if (size > 3) {
            // ソケットアイコンを1つだけ描画
            gfx.blit(SocketTooltipRenderer.SOCKET, x, y, 0, 0, 0, 9, 9, 9, 9);
        } else {
            // 3個以下なら通常通り描画
            for (int i = 0; i < size; i++) {
                gfx.blit(SocketTooltipRenderer.SOCKET, x, y + (i * spacing), 0, 0, 0, 9, 9, 9, 9);
            }
        }
    }

    @Overwrite
    public void renderText(Font font, int x, int y, Matrix4f matrix, MultiBufferSource.BufferSource buffer) {
        int size = this.comp.gems().size();
        if (size > 3) {
            // 「ソケット: XX個 (詳細は Shift)」的な簡易表示にする
            Component text = Component.literal("Sockets: " + size + " Total (Stats Applied)")
                    .withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC);
            font.drawInBatch(text, x + 12, y + 1, 0xAABBCC, true, matrix, buffer, Font.DisplayMode.NORMAL, 0, 15728880);
        } else {
            // 通常表示
            for (int i = 0; i < size; i++) {
                Component desc = SocketTooltipRenderer.getSocketDesc(this.comp.gems().get(i));
                font.drawInBatch(desc, x + 12, y + 1 + (this.spacing * i), 0xAABBCC, true, matrix, buffer, Font.DisplayMode.NORMAL, 0, 15728880);
            }
        }
    }
}