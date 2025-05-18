package net.superkat.flounderlib.render.fun;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;

public class RepoText {
    public final Text text;
    public int maxTicks;

    public int ticks = 0;
    public int bounceY = 0;
    public int bounceYAmount = 1;
    public boolean yellow = true;

    public RepoText(Text text) {
        this.text = text;
        int textLength = text.getLiteralString().length();
        this.maxTicks = (int) (25 + (textLength + (textLength * 0.75)));
    }

    public void render(DrawContext context, RenderTickCounter counter) {
        MinecraftClient client = MinecraftClient.getInstance();
        TextRenderer textRenderer = client.inGameHud.getTextRenderer();

        if(this.ticks < 2) {
            this.bounceY += bounceYAmount;
        } else if (this.ticks < 3) {
            this.bounceY -= bounceYAmount * 2;
        } else if(this.bounceY < 0) {
            this.bounceY += bounceYAmount;
        }

//        if(this.ticks < 2) {
//            this.bounceY += bounceYAmount;
//        } else if(this.bounceY > 0) {
//            this.bounceY -= bounceYAmount;
//        }

        int centerX = context.getScaledWindowWidth() / 2;
        int centerY = context.getScaledWindowHeight() / 2;
        int y = -this.bounceY + centerY / 4;
        int width = textRenderer.getWidth(this.text);
        int x = - width / 2;

        int color = yellow ? Colors.YELLOW : Colors.WHITE;

        context.getMatrices().push();
        context.getMatrices().translate(centerX, centerY, 0);
        context.getMatrices().scale(2f, 2f, 1f);
        context.drawTextWithShadow(textRenderer, this.text, x, y, color);
        context.getMatrices().pop();
    }

    public void tick() {
        this.ticks++;
        if(this.ticks >= 5) {
            this.yellow = false;
        }
    }

    public boolean isFinished() {
        return this.ticks >= this.maxTicks;
    }
}
