package net.superkat.flounderlib.impl.text.client.builtin.renderer;

import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.util.Colors;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;
import net.superkat.flounderlib.FlounderLib;
import net.superkat.flounderlib.api.text.v1.builtin.TapeText;
import net.superkat.flounderlib.api.text.v1.client.FlounderTextRenderer;
import net.superkat.flounderlib.api.util.v1.ease.Easings;

import java.util.List;

public class TapeTextRenderer extends FlounderTextRenderer.Singleton<TapeText> {

    public static final Identifier TAPE_LINE_TEXTURE = Identifier.of(FlounderLib.MOD_ID, "text/tape/tape");

    @Override
    public void addText(TapeText text) {
        super.addText(text);

        List<TapeText.TapeLine> lines = this.createTapeLines();
        lines.forEach(text::addTapeLine);
    }

    @Override
    public void renderText(DrawContext context, RenderTickCounter tickCounter, TapeText text, int entry) {
        for (TapeText.TapeLine line : text.lines) {
            this.drawTapeLine(context, tickCounter, text, line);
        }
    }

    public void drawTapeLine(DrawContext context, RenderTickCounter tickCounter, TapeText text, TapeText.TapeLine line) {
        float tickDelta = text.getTicks() + tickCounter.getTickProgress(false);
        float moveDelta = 1f;
        float alphaDelta = 1f;

        if(text.getTicks() < line.moveTicks) {
            float d = tickDelta / line.moveTicks;
            moveDelta = line.bounce ? Easings.easeInOutBack(d) : Easings.easeOutSine(d);
        }
        if(text.getTicks() < line.fadeInTicks) {
            alphaDelta = tickDelta / line.fadeInTicks;
        }

        float x = MathHelper.lerp(moveDelta, line.startX, line.targetX);
        float y = MathHelper.lerp(moveDelta, line.startY, line.targetY);

        int color = ColorHelper.withAlpha(alphaDelta, text.color);

        context.getMatrices().pushMatrix();
        context.getMatrices().translate(x, y);
        context.getMatrices().rotate(line.rotation);

        context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, TAPE_LINE_TEXTURE, 0, 0, line.width, line.height, color);

        int repeats = 5;
        int textPadding = (line.width / 2 / repeats);
        int textHeight = this.textRenderer.getWrappedLinesHeight(text.getText(), 256);
        float heightScale = (float) (line.height / textHeight);
        context.getMatrices().translate(0, textHeight / heightScale);
        context.getMatrices().scale(heightScale);
        for (int i = 0; i < repeats; i++) {
            int textX = textPadding * i;
            context.drawTextWithShadow(this.textRenderer, text.getText(), textX, 0, Colors.GRAY);
        }

        context.getMatrices().popMatrix();
    }



    public List<TapeText.TapeLine> createTapeLines() {
        int screenWidth = this.client.getWindow().getScaledWidth();
        int screenHeight = this.client.getWindow().getScaledHeight();
        int halfWidth = screenWidth / 2;
        int halfHeight = screenHeight / 2;
        float widthMargin = (screenWidth * 0.15f);

        int fadeInTicks = 4;
        int moveInTicks = 5;

        return List.of(
                new TapeText.TapeLine(
                        screenWidth * 1.5f, -widthMargin / 2f, screenHeight * -0.2f, screenHeight * 0.3f,
                        (int) (screenWidth + widthMargin * 2f), (int) (screenHeight * 0.1f),
                        (float) Math.toRadians(-10), false,
                        moveInTicks + 1, fadeInTicks
                ),
                new TapeText.TapeLine(
                        screenWidth * -1.75f, -widthMargin, screenHeight * -0.7f, screenHeight * 0.05f,
                        (int) (screenWidth + widthMargin * 2f), (int) (screenHeight * 0.1f),
                        (float) Math.toRadians(13), false,
                        moveInTicks + 2, fadeInTicks
                ),
                new TapeText.TapeLine(
                        screenWidth * 1.75f, -widthMargin, screenHeight * 1.05f, screenHeight * 0.55f,
                        (int) (screenWidth + widthMargin * 2f), (int) (screenHeight * 0.075f),
                        (float) Math.toRadians(9), false,
                        moveInTicks, fadeInTicks
                ),
                new TapeText.TapeLine(
                        screenWidth * 1.5f, -widthMargin / 2f, screenHeight * 0.2f, screenHeight * 0.75f,
                        (int) (screenWidth + widthMargin * 2f), (int) (screenHeight * 0.125f),
                        (float) Math.toRadians(-11), true,
                        moveInTicks + 2, fadeInTicks
                )
        );
    }

}
