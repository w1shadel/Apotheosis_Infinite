package com.maxwell.apotheosis_infnite.adventure.client;

import dev.shadowsoffire.apotheosis.adventure.client.SocketTooltipRenderer;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import org.joml.Matrix4f;

import java.util.List;


public class GemTooltipRenderer {
    private static final int SOCKET_SIZE = 9;

    public static void render(List<GemGroupEntry> entries, int spacing, int x, int y, GuiGraphics gfx) {
        for (int i = 0; i < entries.size(); i++) {
            int currentY = y + spacing * i;
            gfx.blit(SocketTooltipRenderer.SOCKET, x, currentY, 0, 0, 0, SOCKET_SIZE, SOCKET_SIZE, SOCKET_SIZE, SOCKET_SIZE);

            GemGroupEntry entry = entries.get(i);
            if (entry.instance().isValid()) {
                gfx.pose().pushPose();
                gfx.pose().translate(x + 1, currentY + 1, 0);
                gfx.pose().scale(0.5F, 0.5F, 1.0F);
                gfx.renderFakeItem(entry.instance().gemStack(), 0, 0);
                gfx.pose().popPose();
            }
        }
    }

    public static void renderText(List<GemGroupEntry> entries, int spacing, int x, int y, Matrix4f matrix, MultiBufferSource.BufferSource buffer, Font font) {
        for (int i = 0; i < entries.size(); i++) {
            GemGroupEntry entry = entries.get(i);
            Component desc = SocketTooltipRenderer.getSocketDesc(entry.instance());

            Component finalSafeText = entry.count() > 1
                    ? desc.copy().append(Component.literal(" x" + entry.count()))
                    : desc;

            font.drawInBatch(finalSafeText, x + 12, y + 1 + spacing * i, 0xAABBCC, true, matrix, buffer, Font.DisplayMode.NORMAL, 0, 15728880);
        }
    }
}