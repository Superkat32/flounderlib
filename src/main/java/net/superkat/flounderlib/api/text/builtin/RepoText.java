package net.superkat.flounderlib.api.text.builtin;

import com.mojang.serialization.Codec;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.Identifier;
import net.superkat.flounderlib.FlounderLib;
import net.superkat.flounderlib.api.text.FlounderText;
import net.superkat.flounderlib.api.text.FlounderTextApi;
import net.superkat.flounderlib.api.text.FlounderTextRenderer;
import net.superkat.flounderlib.text.FlounderTextType;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Objects;

public class RepoText extends FlounderText {
    public static final Identifier ID = Identifier.of(FlounderLib.MOD_ID, "repo_text");
    public static final Codec<RepoText> CODEC = createDefaultCodec(RepoText::new);
    public static final FlounderTextRenderer<RepoText> RENDERER = new RepoTextRenderer();
    public static final FlounderTextType<RepoText> TYPE = FlounderTextApi.register(ID, CODEC, RENDERER, RepoText::new);

    public int bounceY = 0;
    public int bounceYAmount = 1;
    public boolean yellow = true;

    public RepoText(Text text) {
        super(text);
        int textLength = text.getLiteralString().length();
        this.maxTicks = (int) (25 + (textLength + (textLength * 0.75)));
    }

    @Override
    public void render(DrawContext context, RenderTickCounter tickCounter) {
        if(this.ticks < 2) {
            this.bounceY += this.bounceYAmount;
        } else if (this.ticks < 3) {
            this.bounceY -= this.bounceYAmount * 2;
        } else if(this.bounceY < 0) {
            this.bounceY += this.bounceYAmount;
        }

        int centerX = context.getScaledWindowWidth() / 2;
        int centerY = context.getScaledWindowHeight() / 2;
        int y = -this.bounceY + centerY / 4;
        int width = this.textRenderer.getWidth(this.text);
        int x = - width / 2;

        int color = this.yellow ? Colors.YELLOW : Colors.WHITE;

        context.getMatrices().pushMatrix();
        context.getMatrices().translate(centerX, centerY);
        context.getMatrices().scale(2f, 2f);
        context.drawTextWithShadow(this.textRenderer, this.text, x, y, color);
        context.getMatrices().popMatrix();
    }

    @Override
    public void tick(boolean paused) {
        super.tick(paused);

        if(this.ticks >= 5) {
            this.yellow = false;
        }
    }

    public static class RepoTextRenderer extends FlounderTextRenderer<RepoText> {
        public RepoText currentText = null;

        @Override
        public void addText(RepoText repoText) {
            Text text = repoText.getText();
            if(text.getLiteralString().isEmpty() || text.getLiteralString().isBlank()) return;
            String[] words = Objects.requireNonNull(text.getLiteralString()).split(" ");

            for (String word : words) {
                this.texts.add(new RepoText(Text.of(word)));
            }
        }

        @Override
        public void tick(boolean paused) {
            if(this.currentText == null) return;
            this.currentText.tick(paused);
            if(this.texts.size() >= 2) {
                this.currentText.tick(paused); // speed it up
            }
        }

        @Override
        public void render(DrawContext context, RenderTickCounter tickCounter) {
            if(currentText != null) {
                currentText.render(context, tickCounter);
                if(currentText.shouldRemove()) {
                    currentText = null;
                }
            } else {
                if(texts.isEmpty()) return;
                currentText = ((ArrayDeque<RepoText>) texts).poll();
                // this was just a joke but its sorta funny
//                Narrator.getNarrator().say(currentText.text.getLiteralString(), false, 0.7f);
            }
        }

        @Override
        public Collection<RepoText> createCollection() {
            return new ArrayDeque<>();
        }
    }
}
