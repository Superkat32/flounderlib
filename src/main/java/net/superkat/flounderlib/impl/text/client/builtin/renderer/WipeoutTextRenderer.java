package net.superkat.flounderlib.impl.text.client.builtin.renderer;

import net.minecraft.ChatFormatting;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.util.CommonColors;
import net.minecraft.util.Mth;
import net.superkat.flounderlib.FlounderLib;
import net.superkat.flounderlib.api.text.v1.builtin.WipeoutText;
import net.superkat.flounderlib.api.text.v1.client.FlounderTextRenderer;
import net.superkat.flounderlib.api.util.v1.ease.Easings;

public class WipeoutTextRenderer extends FlounderTextRenderer.Singleton<WipeoutText> {
    public static final Identifier BACKGROUND_TEXTURE = Identifier.fromNamespaceAndPath(FlounderLib.MOD_ID, "text/wipeout/background");

    @Override
    public void renderText(GuiGraphics graphics, DeltaTracker deltaTracker, WipeoutText text, int entry) {
        float textAlpha = 1f;
        float backgroundAlpha = text.maxBackgroundAlpha;
        float backgroundHorizontalStretch = text.maxBackgroundHorizontal;
        float backgroundVerticalStretch = 2f;

        // Fade in the background
        if(text.ticks < text.backgroundFadeIn) {
            float delta = this.getTickAndDelta(text, deltaTracker) / text.backgroundFadeIn;
            backgroundAlpha = Mth.lerp(delta, 0f, text.maxBackgroundAlpha);
        }

        // Stretch in the background
        if(text.ticks < text.backgroundStretchInTicks) { // Horizontal stretch in
            float delta = this.getTickAndDelta(text, deltaTracker) / text.backgroundStretchInTicks;
            backgroundHorizontalStretch = Mth.lerp(Easings.easeOutCubic(delta), 0f, text.maxBackgroundHorizontal);
        } else if(text.ticks < text.maxTicks) {
            float delta = (this.getTickAndDelta(text, deltaTracker) - (text.backgroundStretchInTicks)) / (text.maxTicks - text.backgroundStretchInTicks);
            backgroundVerticalStretch = Mth.lerp(delta, 2f, text.maxBackgroundVertical);
        }

        // Fade out the text and background
        if (text.ticks > text.maxTicks - text.fadeOutTicks) { // Stretch and fade out
            float delta = (this.getTickAndDelta(text, deltaTracker) - (text.maxTicks - text.fadeOutTicks) - 1) / text.fadeOutTicks;
            backgroundAlpha = Mth.lerp(delta, text.maxBackgroundAlpha, 0f);
            textAlpha = Mth.lerp(delta, 1f, 0f);
        }

        // Determine positions and color
        int centerX = graphics.guiWidth() / 2;
        int centerY = graphics.guiHeight() / 2;
        int textWidth = this.font.width(text.getText());
        int textHeight = this.font.wordWrapHeight(text.getText(), 216);

        int textX = -textWidth / 2;
        int textY = -centerY / 4;

        int backgroundHeight = 16;
        int backgroundX = -textWidth / 2;
        int backgroundY = -backgroundHeight / 2;
        int backgroundColor = ARGB.color(backgroundAlpha, text.color);

        // Center to middle of screen, used by both background and text
        graphics.pose().pushMatrix();
        graphics.pose().translate(centerX, centerY);

        // Draw background (I really can't tell you why this works)
        graphics.pose().pushMatrix();
        graphics.pose().translate(0, textY - backgroundHeight - textHeight + 4);
        graphics.pose().scale(backgroundHorizontalStretch, backgroundVerticalStretch);
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, BACKGROUND_TEXTURE, backgroundX, backgroundY, textWidth, backgroundHeight, backgroundColor);
        graphics.pose().popMatrix();

        // Draw the normal text
        int letterX = textX;
        int textColor = ARGB.color(backgroundAlpha, text.positive ? CommonColors.WHITE : CommonColors.DARK_GRAY);

        int textLength = text.getText().getString().length();
        float letterFadeInTicks = (float) text.textIntroTicks / textLength;

        graphics.pose().pushMatrix();
        graphics.pose().scale(2f, 2f); // Scale for text

        // Render each character of the text individually, to allow for the bouncing in effect
        for (int i = 0; i < textLength; i++) {
            Component letter = Component.nullToEmpty(String.valueOf(text.getText().getString().charAt(i)));
            letter = letter.copy().withStyle(ChatFormatting.BOLD);
            drawLetter(graphics, deltaTracker, text, letter, letterX, textY, letterFadeInTicks, textColor, textAlpha, i);

            letterX += this.font.width(letter);
        }

        graphics.pose().popMatrix();
        graphics.pose().popMatrix();
    }

    public void drawLetter(GuiGraphics context, DeltaTracker tickCounter, WipeoutText text, Component letter, int x, int y, float fadeInTicks, int color, float alpha, int entry) {
        float ticks = this.getTickAndDelta(text, tickCounter);
        float startTicks = fadeInTicks * entry;
        if(ticks < startTicks) return;

        float bounceDelta = Easings.easeInBack(Math.min((ticks - startTicks) / fadeInTicks, 1f));
        float alphaDelta = Math.min((ticks - startTicks) / (fadeInTicks * 2f), 1f);

        float yBounce = Mth.lerp(bounceDelta, text.letterBounceY, 0f);
        float letterAlpha = Mth.lerp(alphaDelta, 0f, 1f) * alpha;
        int letterColor = ARGB.color(letterAlpha, color);

        context.pose().pushMatrix();
        context.pose().translate(x, y - yBounce);
        context.drawString(this.font, letter, 0, 0, letterColor);
        context.pose().popMatrix();
    }
}
