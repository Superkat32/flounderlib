package net.superkat.flounderlib.impl.text.client.builtin.renderer;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.superkat.flounderlib.api.text.v1.builtin.ShakeyActionText;
import net.superkat.flounderlib.api.text.v1.client.FlounderTextRenderer;
import net.superkat.flounderlib.api.util.v1.ease.Easings;

public class ShakeyActionTextRenderer extends FlounderTextRenderer.Singleton<ShakeyActionText> {
    @Override
    public void renderText(GuiGraphics graphics, DeltaTracker deltaTracker, ShakeyActionText text, int entry) {
        float scale = 1f;
        float stretch = 1f;
        float alpha = 1f;
        float letterScaleDelta = 1f; // reduce travelling letter scaling effect during fade out

        // Stretch in/out and fade in/out the text
        if(text.ticks < text.bounceInTicks) { // Stretch and fade in
            float delta = this.getTickAndDelta(text, deltaTracker) / text.bounceInTicks;
            scale = Mth.lerp(Easings.easeOutElastic(delta), 0f, 1f);
        } else if (text.ticks > text.maxTicks - text.fadeOutTicks) { // Stretch and fade out
            float delta = (this.getTickAndDelta(text, deltaTracker) - (text.maxTicks - text.fadeOutTicks) - 1) / text.fadeOutTicks;
            stretch = Mth.lerp(delta, 1f, text.maxStretch);
            alpha = Mth.lerp(delta, 1f, 0f);
            letterScaleDelta = Mth.lerp(Easings.easeOutExpo(delta), 0.75f, 0f);
        }

        // Determine positions and color
        int centerX = graphics.guiWidth() / 2;
        int centerY = graphics.guiHeight() / 2;
        int width = this.font.width(text.getText());
        int height = this.font.wordWrapHeight(text.getText(), 256);
        int x = -width / 2;
        int y = centerY / -3;
        int color = ARGB.color(alpha, text.color);
        int shadowColor = ARGB.color(alpha, text.shadowColor);

        // Translate to center of screen, then scale up, then render
        graphics.pose().pushMatrix();
        graphics.pose().translate(centerX, centerY);
        graphics.pose().scale(2f * scale * stretch, 2f * scale);

        // Draw the normal text
        float tickDelta = this.getTickAndDelta(text, deltaTracker);
        int letters = text.getText().getString().length();
        int letterX = x;

        float shadowX = 0.5f;
        float shadowY = 0.5f;

        for (int i = 0; i < letters; i++) {
            Component letter = Component.literal(String.valueOf(text.getText().getString().charAt(i))).toFlatList(text.getText().getStyle()).getFirst();
            float rotation = Mth.sin(tickDelta * 2f + i * 4f) / 32f;

            // I really can't begin to tell you the math that's going on here
            float scaleDelta = ((tickDelta + text.bounceInTicks) / 2f) - (i / 2f);
//            float extraScale = Math.max((MathHelper.sin(scaleDelta) - MathHelper.cos(scaleDelta * 2f)) / 24f + 1f, 1);
            float extraScale = Math.max(((Mth.sin(scaleDelta) - Mth.cos(scaleDelta * 2f)) / 48f) * letterScaleDelta + 1f, 1);

            graphics.pose().pushMatrix();
            graphics.pose().scale(extraScale);
            graphics.pose().rotateAbout(rotation, letterX, y);

            // Draw "shadow" (alternative background color)
            graphics.pose().translate(shadowX, shadowY);
            graphics.drawString(this.font, letter, letterX, y, shadowColor, false);
            graphics.pose().translate(-shadowX, -shadowY);

            // Draw normal letter
            graphics.drawString(this.font, letter, letterX, y, color, false);
            graphics.pose().popMatrix();

            letterX += this.font.width(letter);
        }

        graphics.pose().popMatrix();
    }
}
