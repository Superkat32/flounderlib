package net.superkat.flounderlib.impl.text.client.builtin.renderer;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.superkat.flounderlib.api.text.v1.builtin.RepoText;
import net.superkat.flounderlib.api.text.v1.client.FlounderTextRenderer;
import net.superkat.flounderlib.api.util.v1.ease.Easings;

public class RepoTextRenderer extends FlounderTextRenderer.WordQueued<RepoText> {

    @Override
    public void renderText(DrawContext context, RenderTickCounter tickCounter, RepoText text, int entry) {
        float bounceY = 0;

        // Initial bounce effect
        if(text.ticks <= text.bounceInTicks) {
            float delta = this.getTickAndDelta(text, tickCounter) / text.bounceInTicks;
            bounceY = text.bounceAmountY * (1f - Easings.easeOutElastic(delta));
        }

        // Determine positions and color
        int centerX = context.getScaledWindowWidth() / 2;
        int centerY = context.getScaledWindowHeight() / 2;
        int width = this.textRenderer.getWidth(text.getText());
        int x = -width / 2;
        int y = centerY / 4;
        int color = text.ticks <= text.yellowTicks ? Colors.YELLOW : Colors.WHITE;

        // Translate to center of screen, then scale up, then render
        context.getMatrices().pushMatrix();
        context.getMatrices().translate(centerX, centerY);
        context.getMatrices().scale(2f, 2f);
        context.getMatrices().translate(0, bounceY); // Apply bounce
        context.drawText(this.textRenderer, text.text, x, y, color, true);
        context.getMatrices().popMatrix();
    }

    @Override
    public RepoText createTextFromWord(RepoText initText, String word) {
        return new RepoText(Text.literal(word));
    }
}
