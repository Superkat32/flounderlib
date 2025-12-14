package net.superkat.flounderlib.impl.text.client.builtin.renderer;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.CommonColors;
import net.superkat.flounderlib.api.text.v1.builtin.RepoText;
import net.superkat.flounderlib.api.text.v1.client.FlounderTextRenderer;
import net.superkat.flounderlib.api.util.v1.ease.Easings;

public class RepoTextRenderer extends FlounderTextRenderer.WordQueued<RepoText> {

    @Override
    public void renderText(GuiGraphics graphics, DeltaTracker deltaTracker, RepoText text, int entry) {
        float bounceY = 0;

        // Initial bounce effect
        if(text.ticks <= text.bounceInTicks) {
            float delta = this.getTickAndDelta(text, deltaTracker) / text.bounceInTicks;
            bounceY = text.bounceAmountY * (1f - Easings.easeOutElastic(delta));
        }

        // Determine positions and color
        int centerX = graphics.guiWidth() / 2;
        int centerY = graphics.guiHeight() / 2;
        int width = this.font.width(text.getText());
        int x = -width / 2;
        int y = centerY / 4;
        int color = text.ticks <= text.yellowTicks ? CommonColors.YELLOW : CommonColors.WHITE;

        // Translate to center of screen, then scale up, then render
        graphics.pose().pushMatrix();
        graphics.pose().translate(centerX, centerY);
        graphics.pose().scale(2f, 2f);
        graphics.pose().translate(0, bounceY); // Apply bounce
        graphics.drawString(this.font, text.text, x, y, color, true);
        graphics.pose().popMatrix();
    }

    @Override
    public RepoText createTextFromWord(RepoText initText, String word) {
        return new RepoText(Component.literal(word));
    }
}
