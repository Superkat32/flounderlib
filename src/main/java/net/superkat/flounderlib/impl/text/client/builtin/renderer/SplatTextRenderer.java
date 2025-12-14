package net.superkat.flounderlib.impl.text.client.builtin.renderer;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.util.CommonColors;
import net.minecraft.util.Mth;
import net.superkat.flounderlib.FlounderLib;
import net.superkat.flounderlib.api.text.v1.builtin.SplatText;
import net.superkat.flounderlib.api.text.v1.client.FlounderTextRenderer;

public class SplatTextRenderer extends FlounderTextRenderer.Simple<SplatText> {
    public static final Identifier[] BACKGROUND_TEXTURES = getBackgroundTextures();

    private static final int BACKGROUND_TEXTURE_COUNT = 7;

    @Override
    public void addText(SplatText text) {
        text.backgroundY = this.getBackgroundY(this.client.getWindow().getGuiScaledHeight(), 0);
        text.prevBackgroundY = text.backgroundY;
        text.textY = text.backgroundY + (9 / 2);
        text.prevTextY = text.textY;
        text.setBackgroundId(BACKGROUND_TEXTURE_COUNT);
        super.addText(text);
    }

    @Override
    public void renderText(GuiGraphics graphics, DeltaTracker deltaTracker, SplatText text, int entry) {
        int windowWidth = graphics.guiWidth();
        int windowHeight = graphics.guiHeight();
        int centerX = windowWidth / 2;

        // Slide in
        int xOffset = 0;
        if(text.ticks < text.slideInTicks) {
            xOffset = Mth.lerpInt(this.getTickAndDelta(text, deltaTracker) / text.slideInTicks, text.startXOffset, 0);
        }

        // Fading
        float alpha = 1f;
        if(text.ticks <= text.fadeInTicks) { // Fade in
            alpha = Mth.lerp((float) text.ticks / text.fadeInTicks, 0f, 1f);
        } else if (text.ticks >= text.maxTicks - text.fadeOutTicks) { // Fade out
            alpha = Mth.lerp((float) (text.ticks - (text.maxTicks - text.fadeOutTicks)) / text.fadeOutTicks, 1f, 0f);
        }

        // Center the text background
        int backgroundWidth = (int) (windowWidth / 3.5);
        int backgroundHeight = 16;
        int backgroundX = centerX - (backgroundWidth / 2) + xOffset;
        // Slide up the text background
        text.backgroundY = this.getBackgroundY(windowHeight, entry);
        text.prevBackgroundY = Mth.lerpInt(deltaTracker.getGameTimeDeltaPartialTick(false) / 2f, text.prevBackgroundY, text.backgroundY);;

        // Center the text
        int textWidth = this.font.width(text.getText());
        int textHeight = 9;
        int textX = centerX - (textWidth / 2) + xOffset;
        // Slide the text up
        text.textY = text.backgroundY + (textHeight / 2);
        text.prevTextY = Mth.lerpInt(deltaTracker.getGameTimeDeltaPartialTick(false) / 2f, text.prevTextY, text.textY);;

        // Apply fading effects (if any)
        int textColor = ARGB.color(alpha, CommonColors.WHITE);

        // Draw the background and text
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, BACKGROUND_TEXTURES[text.backgroundId], backgroundX, text.prevBackgroundY, backgroundWidth, backgroundHeight, alpha);
        graphics.drawString(this.font, text.getText(), textX, text.prevTextY, textColor);
    }

    public int getBackgroundY(int windowHeight, int entry) {
        int backgroundHeight = 16;
        return windowHeight - 38 - ((backgroundHeight + 1) * (entry + 1));
    }

    private static Identifier[] getBackgroundTextures() {
        Identifier[] textures = new Identifier[BACKGROUND_TEXTURE_COUNT];
        for (int i = 0; i < BACKGROUND_TEXTURE_COUNT; i++) {
            textures[i] = bg(i + 1);
        }
        return textures;
    }

    private static Identifier bg(int id) {
        return Identifier.fromNamespaceAndPath(FlounderLib.MOD_ID, "text/splat/splatted" + id);
    }
}
