package net.superkat.flounderlib.impl.text.client.builtin.renderer;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.text.Text;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;
import net.superkat.flounderlib.api.text.v1.builtin.ShakeyActionText;
import net.superkat.flounderlib.api.text.v1.client.FlounderTextRenderer;
import net.superkat.flounderlib.api.util.v1.ease.Easings;

public class ShakeyActionTextRenderer extends FlounderTextRenderer.Singleton<ShakeyActionText> {
    @Override
    public void renderText(DrawContext context, RenderTickCounter tickCounter, ShakeyActionText text, int entry) {
        float scale = 1f;
        float stretch = 1f;
        float alpha = 1f;
        float letterScaleDelta = 1f; // reduce travelling letter scaling effect during fade out

        // Stretch in/out and fade in/out the text
        if(text.ticks < text.bounceInTicks) { // Stretch and fade in
            float delta = this.getTickAndDelta(text, tickCounter) / text.bounceInTicks;
            scale = MathHelper.lerp(Easings.easeOutElastic(delta), 0f, 1f);
        } else if (text.ticks > text.maxTicks - text.fadeOutTicks) { // Stretch and fade out
            float delta = (this.getTickAndDelta(text, tickCounter) - (text.maxTicks - text.fadeOutTicks) - 1) / text.fadeOutTicks;
            stretch = MathHelper.lerp(delta, 1f, text.maxStretch);
            alpha = MathHelper.lerp(delta, 1f, 0f);
            letterScaleDelta = MathHelper.lerp(Easings.easeOutExpo(delta), 0.75f, 0f);
        }

        // Determine positions and color
        int centerX = context.getScaledWindowWidth() / 2;
        int centerY = context.getScaledWindowHeight() / 2;
        int width = this.textRenderer.getWidth(text.getText());
        int height = this.textRenderer.getWrappedLinesHeight(text.getText(), 256);
        int x = -width / 2;
        int y = centerY / -3;
        int color = ColorHelper.withAlpha(alpha, text.color);
        int shadowColor = ColorHelper.withAlpha(alpha, text.shadowColor);

        // Translate to center of screen, then scale up, then render
        context.getMatrices().pushMatrix();
        context.getMatrices().translate(centerX, centerY);
        context.getMatrices().scale(2f * scale * stretch, 2f * scale);

        // Draw the normal text
        float tickDelta = this.getTickAndDelta(text, tickCounter);
        int letters = text.getText().getString().length();
        int letterX = x;

        float shadowX = 0.5f;
        float shadowY = 0.5f;

        for (int i = 0; i < letters; i++) {
            Text letter = Text.literal(String.valueOf(text.getText().getString().charAt(i))).getWithStyle(text.getText().getStyle()).getFirst();
            float rotation = MathHelper.sin(tickDelta * 2f + i * 4f) / 32f;

            // I really can't begin to tell you the math that's going on here
            float scaleDelta = ((tickDelta + text.bounceInTicks) / 2f) - (i / 2f);
//            float extraScale = Math.max((MathHelper.sin(scaleDelta) - MathHelper.cos(scaleDelta * 2f)) / 24f + 1f, 1);
            float extraScale = Math.max(((MathHelper.sin(scaleDelta) - MathHelper.cos(scaleDelta * 2f)) / 48f) * letterScaleDelta + 1f, 1);

            context.getMatrices().pushMatrix();
            context.getMatrices().scale(extraScale);
            context.getMatrices().rotateAbout(rotation, letterX, y);

            // Draw "shadow" (alternative background color)
            context.getMatrices().translate(shadowX, shadowY);
            context.drawText(this.textRenderer, letter, letterX, y, shadowColor, false);
            context.getMatrices().translate(-shadowX, -shadowY);

            // Draw normal letter
            context.drawText(this.textRenderer, letter, letterX, y, color, false);
            context.getMatrices().popMatrix();

            letterX += this.textRenderer.getWidth(letter);
        }

        context.getMatrices().popMatrix();
    }
}
