package net.superkat.flounderlibtest.test.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.math.Vec3d;
import net.superkat.flounderlib.api.minigame.v1.FlounderClientApi;
import net.superkat.flounderlibtest.FlounderLibTest;
import net.superkat.flounderlibtest.test.TestSyncedMinigame;

public class TestSyncedMinigameRenderer {

    public static void renderSyncedMinigameHud(DrawContext context, RenderTickCounter tickCounter) {
        if(!FlounderClientApi.anyMinigames(FlounderLibTest.TEST_SYNCED_MINIGAME_TYPE)) return;
        MinecraftClient client = MinecraftClient.getInstance();
        TextRenderer textRenderer = client.textRenderer;

        TestRenderState renderState = TestRenderState.fromSyncState(
                FlounderClientApi.getFirstSyncState(TestSyncedMinigame.STATE_SYNCER)
        );

        int ticks = renderState.ticks();
        boolean myBoolean = renderState.myBoolean();
        int myInteger = renderState.myInteger();
        String myString = renderState.myString();
        Vec3d myVec3d = renderState.myVec3d();
        Text myText = renderState.myText();
        if(ticks == -1) return;

        int y = 0;
        y = render(context, textRenderer, ticks, y);
        y = render(context, textRenderer, myText, y);
        y = render(context, textRenderer, myVec3d, y);
        y = render(context, textRenderer, myString, y);
        y = render(context, textRenderer, myInteger, y);
        y = render(context, textRenderer, myBoolean, y);
    }

    private static int render(DrawContext context, TextRenderer textRenderer, Object value, int y) {
        context.drawText(textRenderer, String.valueOf(value), 0, y, Colors.WHITE, true);
        return y + 10;
    }

    public record TestRenderState(int ticks, boolean myBoolean, int myInteger, String myString, Vec3d myVec3d, Text myText) {
        public static TestRenderState fromSyncState(TestSyncedMinigame.SyncState syncState) {
            return new TestRenderState(
                    syncState.ticks,
                    syncState.myBoolean,
                    syncState.myInteger,
                    syncState.myString,
                    syncState.myVec3d,
                    syncState.myText
            );
        }
    }

}
