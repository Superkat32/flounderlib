package net.superkat.flounderlib.api.text.builtin;

import com.mojang.serialization.MapCodec;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.superkat.flounderlib.FlounderLib;
import net.superkat.flounderlib.api.text.core.FlounderText;
import net.superkat.flounderlib.api.text.core.FlounderTextType;

public class RepoText extends FlounderText {
    public static final Identifier ID = Identifier.of(FlounderLib.MOD_ID, "repo_text");
    public static final MapCodec<RepoText> CODEC = createDefaultCodec(RepoText::new);

    public int bounceY = 0;
    public boolean yellow = true;

    public RepoText(Text text) {
        super(text);
        int textLength = text.getLiteralString().length();
        this.maxTime = (long) (1050 + (textLength + (textLength * 0.75f)));
    }

    @Override
    public void draw(DrawContext context, RenderTickCounter tickCounter) {
        // Bounce up & down
        if(this.time <= 50) {
            this.bounceY = MathHelper.lerp( this.time / 50f, 0, 3);
        } else if(this.time <= 100f) {
            this.bounceY = MathHelper.lerp((this.time - 50f) / 50f, 2, -5);
        } else if(this.bounceY != 0) {
            this.bounceY = MathHelper.lerp(0.5f * tickCounter.getDynamicDeltaTicks(), this.bounceY, 0);
        }

        // Change color to yellow during the beginning
        if(this.time >= 250) {
            this.yellow = false;
        }

        // Determine positions and color
        int centerX = context.getScaledWindowWidth() / 2;
        int centerY = context.getScaledWindowHeight() / 2;
        int width = this.textRenderer.getWidth(this.text);
        int x = -width / 2;
        int y = -this.bounceY + (centerY / 4);
        int color = yellow ? Colors.YELLOW : Colors.WHITE;

        // Translate to center of screen, then scale up, then render
        context.getMatrices().pushMatrix();
        context.getMatrices().translate(centerX, centerY);
        context.getMatrices().scale(2f, 2f);
        context.drawText(this.textRenderer, this.text, x, y, color, true);
        context.getMatrices().popMatrix();
    }

    @Override
    public FlounderTextType<?> getType() {
        return BuiltinFlounderTextRenderers.REPO_TEXT_TYPE;
    }
}
