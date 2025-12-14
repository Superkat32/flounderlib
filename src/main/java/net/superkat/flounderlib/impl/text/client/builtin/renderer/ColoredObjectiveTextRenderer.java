package net.superkat.flounderlib.impl.text.client.builtin.renderer;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.ARGB;
import net.minecraft.util.CommonColors;
import net.minecraft.util.Mth;
import net.superkat.flounderlib.api.text.v1.builtin.ColoredObjectiveText;
import net.superkat.flounderlib.api.text.v1.client.FlounderTextRenderer;
import net.superkat.flounderlib.api.util.v1.ease.Easings;

public class ColoredObjectiveTextRenderer extends FlounderTextRenderer.Singleton<ColoredObjectiveText> {

    @Override
    public void renderText(GuiGraphics graphics, DeltaTracker deltaTracker, ColoredObjectiveText text, int entry) {
        float stretch = 1f;
        float alpha = 1f;

        // Stretch in/out and fade in/out the text
        if(text.ticks < text.fadeInTicks) { // Stretch and fade in
            float delta = this.getTickAndDelta(text, deltaTracker) / text.fadeInTicks;
            stretch = Mth.lerp(Easings.easeInOutCirc(delta), text.maxStretch, 1f);
            alpha = Mth.lerp(Easings.easeOutSine(delta), 0f, 1f);
        } else if (text.ticks > text.maxTicks - text.fadeOutTicks) { // Stretch and fade out
            float delta = (this.getTickAndDelta(text, deltaTracker) - (text.maxTicks - text.fadeOutTicks) - 1) / text.fadeOutTicks;
            stretch = Mth.lerp(Easings.easeInSine(delta), 1f, text.maxStretch);
            alpha = Mth.lerp(delta, 1f, 0f);
        }

        // Determine positions and color
        int centerX = graphics.guiWidth() / 2;
        int centerY = graphics.guiHeight() / 2;
        int width = this.font.width(text.getText());
        int x = -width / 2;
        int y = centerY / -3;
        int color = ARGB.color(alpha, CommonColors.WHITE);

        // Make shadow color slightly darker to be more readable in brighter environments (e.g. day time)
        int shadowColor = ARGB.scaleRGB(ARGB.color(alpha, text.color), 0.75f);

        // Translate to center of screen, then scale up, then render
        graphics.pose().pushMatrix();
        graphics.pose().translate(centerX, centerY);
        graphics.pose().scale(2f * stretch, 2f);

        // Draw the colored shadow text slightly offset
        float offsetX = 0.5f;
        float offsetY = 0.5f;
        graphics.pose().translate(offsetX, offsetY);
        graphics.drawString(this.font, text.getText(), x, y, shadowColor, false);
        graphics.pose().translate(-offsetX, -offsetY);

        // Draw the normal text
        graphics.drawString(this.font, text.getText(), x, y, color, false);

        graphics.pose().popMatrix();
    }
}
