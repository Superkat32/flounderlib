package net.superkat.flounderlibtest.render.hud;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.util.Identifier;
import net.superkat.flounderlibtest.FlounderLibTest;

public class TestRenderedMinigameHud {
    public static final Identifier ID = Identifier.of(FlounderLibTest.MOD_ID, "test_rendered_minigame_id");

    public static void render(DrawContext context, RenderTickCounter counter) {
        int index = 0;
//        for (TestRenderedMinigame game : FlounderLibTest.TEST_RENDERED_MINIGAME.getActiveGames()) {
//            renderGame(game, context, counter, index);
//            index++;
//        }
    }

//    public static void renderGame(TestRenderedMinigame game, DrawContext context, RenderTickCounter counter, int index) {
//        MinecraftClient client = MinecraftClient.getInstance();
//        TextRenderer textRenderer = client.textRenderer;
//        int centerX = context.getScaledWindowWidth() / 2;
//        int centerY = context.getScaledWindowHeight() / 2;
//
//        Text text = Text.of("What's up homie buddy?");
//        int width = textRenderer.getWidth(text);
//        int height = textRenderer.getWrappedLinesHeight(text, 114);
//
//        int x = centerX - (width / 2);
//        int y = 16 + ((height - 4) * index);
//        context.drawTextWithShadow(textRenderer, text, x, y, Colors.WHITE);
//    }

}
