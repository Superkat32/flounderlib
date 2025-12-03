package net.superkat.flounderlib.api.text.v1.builtin;

import com.mojang.serialization.MapCodec;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.superkat.flounderlib.FlounderLib;
import net.superkat.flounderlib.api.text.v1.text.FlounderText;
import net.superkat.flounderlib.api.text.v1.text.FlounderTextType;

public class RepoText extends FlounderText {
    public static final Identifier ID = Identifier.of(FlounderLib.MOD_ID, "repo_text");
    public static final MapCodec<RepoText> CODEC = createDefaultCodec(RepoText::new);

    public int yellowTicks;
    public int bounceInTicks;
    public float bounceAmountY;

    public RepoText(Text text) {
        super(text);
        int textLength = text.getLiteralString().length();
        this.maxTicks = Math.min(8 + (textLength), 30);

        this.yellowTicks = 4;
        this.bounceInTicks = 8;
        this.bounceAmountY = MathHelper.nextBetween(this.random, 3f, 7f);
    }

    @Override
    public void draw(DrawContext context, RenderTickCounter tickCounter, int entry, int totalEntries) {
        float bounceY = 0;

        // Initial bounce effect
        if(this.ticks <= this.bounceInTicks) {
            float delta = this.getTickDelta(tickCounter) / this.bounceInTicks;
            bounceY = (float) (this.bounceAmountY * (1f - easeOutElastic(delta)));
        }

        // Determine positions and color
        int centerX = context.getScaledWindowWidth() / 2;
        int centerY = context.getScaledWindowHeight() / 2;
        int width = this.textRenderer.getWidth(this.text);
        int x = -width / 2;
        int y = centerY / 4;
        int color = this.ticks <= this.yellowTicks ? Colors.YELLOW : Colors.WHITE;

        // Translate to center of screen, then scale up, then render
        context.getMatrices().pushMatrix();
        context.getMatrices().translate(centerX, centerY);
        context.getMatrices().scale(2f, 2f);
        context.getMatrices().translate(0, bounceY); // Apply bounce
        context.drawText(this.textRenderer, this.text, x, y, color, true);
        context.getMatrices().popMatrix();
    }

    // https://easings.net/#easeOutElastic (amazing website)
    public double easeOutElastic(float delta) {
        float c4 = (2f * MathHelper.PI) / 3f;

        return delta == 0
                ? 0
                : delta == 1
                ? 1
                : Math.pow(2, -10 * delta) * MathHelper.sin((delta * 10f - 0.75f) * c4) + 1;
    }

    @Override
    public FlounderTextType<?> getType() {
        return BuiltinFlounderTextRenderers.REPO_TEXT_TYPE;
    }
}
