package net.superkat.flounderlib.impl.text.client.builtin.renderer;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.util.Colors;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;
import net.superkat.flounderlib.api.text.v1.builtin.ColoredObjectiveText;
import net.superkat.flounderlib.api.text.v1.client.FlounderTextRenderer;
import net.superkat.flounderlib.api.util.v1.ease.Easings;

public class ColoredObjectiveTextRenderer extends FlounderTextRenderer.Singleton<ColoredObjectiveText> {

    @Override
    public void renderText(DrawContext context, RenderTickCounter tickCounter, ColoredObjectiveText text, int entry) {
        float stretch = 1f;
        float alpha = 1f;

        // Stretch in/out and fade in/out the text
        if(text.ticks < text.fadeInTicks) { // Stretch and fade in
            float delta = this.getTickAndDelta(text, tickCounter) / text.fadeInTicks;
            stretch = MathHelper.lerp(Easings.easeInOutCirc(delta), text.maxStretch, 1f);
            alpha = MathHelper.lerp(Easings.easeOutSine(delta), 0f, 1f);
        } else if (text.ticks > text.maxTicks - text.fadeOutTicks) { // Stretch and fade out
            float delta = (this.getTickAndDelta(text, tickCounter) - (text.maxTicks - text.fadeOutTicks) - 1) / text.fadeOutTicks;
            stretch = MathHelper.lerp(Easings.easeInSine(delta), 1f, text.maxStretch);
            alpha = MathHelper.lerp(delta, 1f, 0f);
        }

        // Determine positions and color
        int centerX = context.getScaledWindowWidth() / 2;
        int centerY = context.getScaledWindowHeight() / 2;
        int width = this.textRenderer.getWidth(text.getText());
        int x = -width / 2;
        int y = centerY / -3;
        int color = ColorHelper.withAlpha(alpha, Colors.WHITE);

        // Make shadow color slightly darker to be more readable in brighter environments (e.g. day time)
        int shadowColor = ColorHelper.scaleRgb(ColorHelper.withAlpha(alpha, text.color), 0.75f);

        // Translate to center of screen, then scale up, then render
        context.getMatrices().pushMatrix();
        context.getMatrices().translate(centerX, centerY);
        context.getMatrices().scale(2f * stretch, 2f);

        // Draw the colored shadow text slightly offset
        float offsetX = 0.5f;
        float offsetY = 0.5f;
        context.getMatrices().translate(offsetX, offsetY);
        context.drawText(this.textRenderer, text.getText(), x, y, shadowColor, false);
        context.getMatrices().translate(-offsetX, -offsetY);

        // Draw the normal text
        context.drawText(this.textRenderer, text.getText(), x, y, color, false);

        context.getMatrices().popMatrix();
    }
}
