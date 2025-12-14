package net.superkat.flounderlib.impl.text.client.builtin.renderer;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.superkat.flounderlib.FlounderLib;
import net.superkat.flounderlib.api.text.v1.builtin.TapeText;
import net.superkat.flounderlib.api.text.v1.client.FlounderTextRenderer;
import net.superkat.flounderlib.api.util.v1.ease.Easings;

import java.util.List;

public class TapeTextRenderer extends FlounderTextRenderer.Singleton<TapeText> {
    public static final Identifier TAPE_LINE_TEXTURE = Identifier.fromNamespaceAndPath(FlounderLib.MOD_ID, "text/tape/tape");

    @Override
    public void addText(TapeText text) {
        super.addText(text);

        List<TapeText.TapeLine> lines = this.createTapeLines();
        lines.forEach(text::addTapeLine);
    }

    @Override
    public void renderText(GuiGraphics graphics, DeltaTracker deltaTracker, TapeText text, int entry) {
        for (TapeText.TapeLine line : text.lines) {
            this.drawTapeLine(graphics, deltaTracker, text, line);
        }
    }

    public void drawTapeLine(GuiGraphics context, DeltaTracker tickCounter, TapeText text, TapeText.TapeLine line) {
        float tickDelta = text.getTicks() + tickCounter.getGameTimeDeltaPartialTick(false);
        float moveDelta = 1f;
        float alphaDelta = 1f;

        if (text.getTicks() < line.moveTicks) { // Move in
            float d = tickDelta / line.moveTicks;
            moveDelta = line.bounce ? Easings.easeInOutBack(d) : Easings.easeOutSine(d);
        }
        if (text.getTicks() < line.fadeInTicks) { // Fade in
            alphaDelta = tickDelta / line.fadeInTicks;
        }
        if (text.ticks > text.maxTicks - line.fadeOutTicks) { // Fade out
            float delta = (this.getTickAndDelta(text, tickCounter) - (text.maxTicks - line.fadeOutTicks) - 1) / line.fadeOutTicks;
            alphaDelta = Mth.lerp(delta, 1f, 0f);
        }

        float x = Mth.lerp(moveDelta, line.startX, line.targetX);
        float y = Mth.lerp(moveDelta, line.startY, line.targetY);

        int color = ARGB.color(alphaDelta, text.color);

        context.pose().pushMatrix();
        context.pose().translate(x, y);

        int textWidth = this.font.width(text.getText());
        int textHeight = this.font.wordWrapHeight(text.getText(), 216);
        int padding = 25; // Padding between each text repeat

        // Determine scale of text based on the line height, adding some padding between the top and bottom
        float scale = (line.height / textHeight) - (line.height * 0.01f);
        int textPadding = textWidth + padding;

        // To help with readability, at least one text should be centered on the screen
        // If you're trying to understand what's happening here, I'm very sorry
        // This was almost 2 hours of trial & error, and it's held together by hopes and dreams
        float targetCenteredX = (context.guiWidth() / 2f / scale) - (textWidth / 2f) - (line.targetX / scale);

        context.pose().rotate(line.rotation);
        context.blitSprite(RenderPipelines.GUI_TEXTURED, TAPE_LINE_TEXTURE, 0, 0, (int) line.width, (int) line.height, color);

        context.pose().translate(0, line.height - textHeight * scale);
        context.pose().scale(scale);

//        context.drawTextWithShadow(this.textRenderer, text.getText(), (int) (targetCenteredX), 0, Colors.YELLOW);
        int repeats = Mth.ceil((line.width / scale) / (textPadding));

        float startX = targetCenteredX;
        for (int i = 0; i < repeats; i++) {
            startX -= textPadding;
            if(startX <= textPadding) break;
        }

        float textX;
        int textColor = ARGB.color(alphaDelta, text.textColor);
        for (int i = 0; i < repeats; i++) {
            textX = startX + (textPadding) * i;
            context.drawString(this.font, text.getText(), (int) textX, 0, textColor, text.textShadow);
        }

        context.pose().popMatrix();
    }

    // This iis horrifying
    public List<TapeText.TapeLine> createTapeLines() {
        int screenWidth = this.client.getWindow().getGuiScaledWidth();
        int screenHeight = this.client.getWindow().getGuiScaledHeight();
        float widthMargin = (screenWidth * 0.15f);

        int fadeInTicks = 4;
        int moveInTicks = 5;
        int fadeOutTicks = 14;

        return List.of(
                new TapeText.TapeLine(
                        screenWidth * 1.5f, -widthMargin / 2f, screenHeight * -0.2f, screenHeight * 0.3f,
                        screenWidth + widthMargin * 2f, screenHeight * 0.1f,
                        (float) Math.toRadians(-10), false,
                        moveInTicks + 1, fadeInTicks, fadeOutTicks
                ),
                new TapeText.TapeLine(
                        screenWidth * -1.75f, -widthMargin, screenHeight * -0.7f, screenHeight * 0.05f,
                        screenWidth + widthMargin * 2f, screenHeight * 0.1f,
                        (float) Math.toRadians(13), false,
                        moveInTicks + 2, fadeInTicks, fadeOutTicks
                ),
                new TapeText.TapeLine(
                        screenWidth * 1.75f, -widthMargin, screenHeight * 1.05f, screenHeight * 0.55f,
                        screenWidth + widthMargin * 2f, screenHeight * 0.075f,
                        (float) Math.toRadians(9), false,
                        moveInTicks, fadeInTicks, fadeOutTicks
                ),
                new TapeText.TapeLine(
                        screenWidth * 1.5f, -widthMargin / 2f, screenHeight * 0.2f, screenHeight * 0.75f,
                        screenWidth + widthMargin * 2f, screenHeight * 0.125f,
                        (float) Math.toRadians(-11), true,
                        moveInTicks + 2, fadeInTicks, fadeOutTicks
                )
        );
    }

}
