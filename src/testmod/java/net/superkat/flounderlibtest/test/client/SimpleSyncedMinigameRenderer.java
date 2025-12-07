package net.superkat.flounderlibtest.test.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.superkat.flounderlib.api.minigame.v1.FlounderClientApi;
import net.superkat.flounderlibtest.test.SimpleSyncedMinigame;

import java.awt.*;
import java.util.List;

public class SimpleSyncedMinigameRenderer {

    public static void render(DrawContext context, RenderTickCounter tickCounter) {
        List<SimpleSyncedMinigame.SyncState> syncStates = FlounderClientApi.getSyncStates(SimpleSyncedMinigame.STATE_SYNCER);
        for (int i = 0; i < syncStates.size(); i++) {
            SimpleSyncedMinigame.SyncState syncState = syncStates.get(i);
            SimpleSyncedMinigameRenderState renderState = SimpleSyncedMinigameRenderState.fromSyncState(syncState);
            draw(context, tickCounter, renderState, i * 8);
        }
    }

    public static void draw(DrawContext context, RenderTickCounter tickCounter, SimpleSyncedMinigameRenderState renderState, int y) {
        MinecraftClient client = MinecraftClient.getInstance();
        TextRenderer textRenderer = client.textRenderer;

        Random random = Random.create(renderState.id * 10000L);
        int idColor = randomColor(random);

        MutableText id = Text.literal(String.valueOf(renderState.id)).withColor(idColor).append(" - ");
        Text ticks = Text.literal(String.valueOf(renderState.ticks)).withColor(Colors.CYAN).append(" - ");
        Text pos = Text.literal(renderState.pos.toShortString()).withColor(Colors.YELLOW).append(" - ");
        Text aBoolean = Text.literal(String.valueOf(renderState.aBoolean)).withColor(renderState.aBoolean ? Colors.GREEN : Colors.LIGHT_RED);

        context.drawTextWithShadow(textRenderer, id.append(ticks).append(pos).append(aBoolean), 0, y, Colors.WHITE);
    }

    public static int randomColor(Random random) {
        float hue = random.nextFloat();
        float sat = 1f;
        float value = 1f;
        return Color.HSBtoRGB(hue, sat, value);
    }

    public static record SimpleSyncedMinigameRenderState(int id, int ticks, BlockPos pos, boolean aBoolean) {
        public static SimpleSyncedMinigameRenderState fromSyncState(SimpleSyncedMinigame.SyncState syncState) {
            return new SimpleSyncedMinigameRenderState(
                    syncState.id,
                    syncState.ticks,
                    syncState.pos,
                    syncState.aBoolean
            );
        }
    }

}
