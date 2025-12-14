package net.superkat.flounderlibtest.test.client;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.CommonColors;
import net.minecraft.world.phys.Vec3;
import net.superkat.flounderlib.api.minigame.v1.FlounderClientApi;
import net.superkat.flounderlibtest.FlounderLibTest;
import net.superkat.flounderlibtest.test.TestSyncedMinigame;

public class TestSyncedMinigameRenderer {

    public static void renderSyncedMinigameHud(GuiGraphics context, DeltaTracker tickCounter) {
        if(!FlounderClientApi.anyMinigames(FlounderLibTest.TEST_SYNCED_MINIGAME_TYPE)) return;
        Minecraft client = Minecraft.getInstance();
        Font textRenderer = client.font;

        TestRenderState renderState = TestRenderState.fromSyncState(
                FlounderClientApi.getFirstSyncState(TestSyncedMinigame.STATE_SYNCER)
        );

        int ticks = renderState.ticks();
        boolean myBoolean = renderState.myBoolean();
        int myInteger = renderState.myInteger();
        String myString = renderState.myString();
        Vec3 myVec3d = renderState.myVec3d();
        Component myText = renderState.myText();
        if(ticks == -1) return;

        int y = 0;
        y = render(context, textRenderer, ticks, y);
        y = render(context, textRenderer, myText, y);
        y = render(context, textRenderer, myVec3d, y);
        y = render(context, textRenderer, myString, y);
        y = render(context, textRenderer, myInteger, y);
        y = render(context, textRenderer, myBoolean, y);
    }

    private static int render(GuiGraphics context, Font textRenderer, Object value, int y) {
        context.drawString(textRenderer, String.valueOf(value), 0, y, CommonColors.WHITE, true);
        return y + 10;
    }

    public record TestRenderState(int ticks, boolean myBoolean, int myInteger, String myString, Vec3 myVec3d, Component myText) {
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
