package net.superkat.flounderlibtest.test.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.math.Vec3d;
import net.superkat.flounderlib.api.minigame.v1.FlounderClientApi;
import net.superkat.flounderlib.impl.minigame.sync.FlounderSyncState;
import net.superkat.flounderlibtest.FlounderLibTest;
import net.superkat.flounderlibtest.test.TestSyncedMinigame;

public class TestSyncedMinigameRenderer {

    public static void renderSyncedMinigameHud(DrawContext context, RenderTickCounter tickCounter) {
        if(!FlounderClientApi.anyMinigames(FlounderLibTest.TEST_SYNCED_MINIGAME)) return;
        MinecraftClient client = MinecraftClient.getInstance();
        TextRenderer textRenderer = client.textRenderer;

        TestRenderState renderState = TestRenderState.fromSyncState(
                FlounderClientApi.getFirstSyncState(FlounderLibTest.TEST_SYNCED_MINIGAME)
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
        public static TestRenderState fromSyncState(FlounderSyncState syncState) {
            if(syncState.values.size() < 2) return new TestRenderState(-1, false, 0, "", Vec3d.ZERO, Text.of(""));

            int ticks = syncState.getValue(TestSyncedMinigame.TICKS_KEY);
            boolean myBoolean = syncState.getValue(TestSyncedMinigame.MY_BOOLEAN_KEY);
            int myInteger = syncState.getValue(TestSyncedMinigame.MY_INTEGER_KEY);
            String myString = syncState.getValue(TestSyncedMinigame.MY_STRING_KEY);
            Vec3d myVec3d = syncState.getValue(TestSyncedMinigame.MY_VEC_3D_KEY);
            Text myText = syncState.getValue(TestSyncedMinigame.MY_TEXT_KEY);
            return new TestRenderState(ticks, myBoolean, myInteger, myString, myVec3d, myText);
        }
    }

}
