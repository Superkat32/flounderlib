package net.superkat.flounderlibtest.test.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.util.Colors;
import net.superkat.flounderlib.api.FlounderClientApi;
import net.superkat.flounderlibtest.FlounderLibTest;
import net.superkat.flounderlibtest.test.TestSyncedMinigame;

public class TestSyncedMinigameRenderer {

    public static void renderSyncedMinigameHud(DrawContext context, RenderTickCounter tickCounter) {
        if(!FlounderClientApi.anyMinigames(FlounderLibTest.TEST_SYNCED_MINIGAME)) return;
        MinecraftClient client = MinecraftClient.getInstance();
        TextRenderer textRenderer = client.textRenderer;

        TestSyncedMinigame.Data data = FlounderClientApi.getMinigameSyncData(FlounderLibTest.TEST_SYNCED_MINIGAME);

        String text = data.title();
        int ticks = data.ticks();

        context.drawText(textRenderer, text, 0, 0, Colors.WHITE, true);
        context.drawText(textRenderer, String.valueOf(ticks), 0, 16, Colors.WHITE, true);
    }

}
