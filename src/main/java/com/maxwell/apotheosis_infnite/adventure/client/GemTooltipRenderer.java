package com.maxwell.apotheosis_infnite.adventure.client;

import dev.shadowsoffire.apotheosis.adventure.client.SocketTooltipRenderer;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import org.joml.Matrix4f;

import java.util.List;

public class GemTooltipRenderer {
    public static void render(List<GemGroupEntry> entries, int spacing, int x, int y, GuiGraphics gfx) {
        for (int i = 0; i < entries.size(); i++) {
            gfx.blit(SocketTooltipRenderer.SOCKET, x, y + spacing * i, 0, 0, 0, 9, 9, 9, 9);
            GemGroupEntry entry = entries.get(i);
            if (entry.instance().isValid()) {
                gfx.pose().pushPose();
                gfx.pose().scale(0.5F, 0.5F, 1);
                gfx.renderFakeItem(entry.instance().gemStack(), 2 * x + 1, 2 * y + 1 + 2 * spacing * i);
                gfx.pose().popPose();
            }
        }
    }

    public static void renderText(List<GemGroupEntry> entries, int spacing, int pX, int pY, Matrix4f pMatrix4f, MultiBufferSource.BufferSource pBufferSource, Font pFont) {
        for (int i = 0; i < entries.size(); i++) {
            GemGroupEntry entry = entries.get(i);
            Component text = SocketTooltipRenderer.getSocketDesc(entry.instance());
            if (entry.count() > 1) text = text.copy().append(" x" + entry.count());
            pFont.drawInBatch(text, pX + 12, pY + 1 + spacing * i, 0xAABBCC, true, pMatrix4f, pBufferSource, Font.DisplayMode.NORMAL, 0, 15728880);
        }
    }
}