package net.superkat.flounderlib.impl.text.client.builtin.renderer;

import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;
import net.superkat.flounderlib.FlounderLib;
import net.superkat.flounderlib.api.text.v1.builtin.WipeoutText;
import net.superkat.flounderlib.api.text.v1.client.FlounderTextRenderer;
import net.superkat.flounderlib.api.util.v1.ease.Easings;

public class WipeoutTextRenderer extends FlounderTextRenderer.Singleton<WipeoutText> {
    public static final Identifier BACKGROUND_TEXTURE = Identifier.of(FlounderLib.MOD_ID, "text/wipeout/background");

    @Override
    public void renderText(DrawContext context, RenderTickCounter tickCounter, WipeoutText text, int entry) {
        float textAlpha = 1f;
        float backgroundAlpha = text.maxBackgroundAlpha;
        float backgroundHorizontalStretch = text.maxBackgroundHorizontal;
        float backgroundVerticalStretch = 2f;

        // Fade in the background
        if(text.ticks < text.backgroundFadeIn) {
            float delta = this.getTickAndDelta(text, tickCounter) / text.backgroundFadeIn;
            backgroundAlpha = MathHelper.lerp(delta, 0f, text.maxBackgroundAlpha);
        }

        // Stretch in the background
        if(text.ticks < text.backgroundStretchInTicks) { // Horizontal stretch in
            float delta = this.getTickAndDelta(text, tickCounter) / text.backgroundStretchInTicks;
            backgroundHorizontalStretch = MathHelper.lerp(Easings.easeOutCubic(delta), 0f, text.maxBackgroundHorizontal);
        } else if(text.ticks < text.maxTicks) {
            float delta = (this.getTickAndDelta(text, tickCounter) - (text.backgroundStretchInTicks)) / (text.maxTicks - text.backgroundStretchInTicks);
            backgroundVerticalStretch = MathHelper.lerp(delta, 2f, text.maxBackgroundVertical);
        }

        // Fade out the text and background
        if (text.ticks > text.maxTicks - text.fadeOutTicks) { // Stretch and fade out
            float delta = (this.getTickAndDelta(text, tickCounter) - (text.maxTicks - text.fadeOutTicks) - 1) / text.fadeOutTicks;
            backgroundAlpha = MathHelper.lerp(delta, text.maxBackgroundAlpha, 0f);
            textAlpha = MathHelper.lerp(delta, 1f, 0f);
        }

        // Determine positions and color
        int centerX = context.getScaledWindowWidth() / 2;
        int centerY = context.getScaledWindowHeight() / 2;
        int textWidth = this.textRenderer.getWidth(text.getText());
        int textHeight = this.textRenderer.getWrappedLinesHeight(text.getText(), 216);

        int textX = -textWidth / 2;
        int textY = -centerY / 4;

        int backgroundHeight = 16;
        int backgroundX = -textWidth / 2;
        int backgroundY = -backgroundHeight / 2;
        int backgroundColor = ColorHelper.withAlpha(backgroundAlpha, text.color);

        // Center to middle of screen, used by both background and text
        context.getMatrices().pushMatrix();
        context.getMatrices().translate(centerX, centerY);

        // Draw background (I really can't tell you why this works)
        context.getMatrices().pushMatrix();
        context.getMatrices().translate(0, textY - backgroundHeight - textHeight + 4);
        context.getMatrices().scale(backgroundHorizontalStretch, backgroundVerticalStretch);
        context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, BACKGROUND_TEXTURE, backgroundX, backgroundY, textWidth, backgroundHeight, backgroundColor);
        context.getMatrices().popMatrix();

        // Draw the normal text
        int letterX = textX;
        int textColor = ColorHelper.withAlpha(backgroundAlpha, text.positive ? Colors.WHITE : Colors.DARK_GRAY);

        int textLength = text.getText().getString().length();
        float letterFadeInTicks = (float) text.textIntroTicks / textLength;

        context.getMatrices().pushMatrix();
        context.getMatrices().scale(2f, 2f); // Scale for text

        // Render each character of the text individually, to allow for the bouncing in effect
        for (int i = 0; i < textLength; i++) {
            Text letter = Text.of(String.valueOf(text.getText().getString().charAt(i)));
            letter = letter.copy().formatted(Formatting.BOLD);
            drawLetter(context, tickCounter, text, letter, letterX, textY, letterFadeInTicks, textColor, textAlpha, i);

            letterX += this.textRenderer.getWidth(letter);
        }

        context.getMatrices().popMatrix();
        context.getMatrices().popMatrix();
    }

    public void drawLetter(DrawContext context, RenderTickCounter tickCounter, WipeoutText text, Text letter, int x, int y, float fadeInTicks, int color, float alpha, int entry) {
        float ticks = this.getTickAndDelta(text, tickCounter);
        float startTicks = fadeInTicks * entry;
        if(ticks < startTicks) return;

        float bounceDelta = Easings.easeInBack(Math.min((ticks - startTicks) / fadeInTicks, 1f));
        float alphaDelta = Math.min((ticks - startTicks) / (fadeInTicks * 2f), 1f);

        float yBounce = MathHelper.lerp(bounceDelta, text.letterBounceY, 0f);
        float letterAlpha = MathHelper.lerp(alphaDelta, 0f, 1f) * alpha;
        int letterColor = ColorHelper.withAlpha(letterAlpha, color);

        context.getMatrices().pushMatrix();
        context.getMatrices().translate(x, y - yBounce);
        context.drawTextWithShadow(this.textRenderer, letter, 0, 0, letterColor);
        context.getMatrices().popMatrix();
    }
}
