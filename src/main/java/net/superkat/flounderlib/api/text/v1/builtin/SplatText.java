package net.superkat.flounderlib.api.text.v1.builtin;

import com.mojang.serialization.MapCodec;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.superkat.flounderlib.FlounderLib;
import net.superkat.flounderlib.api.text.v1.registry.FlounderTextType;
import net.superkat.flounderlib.api.text.v1.text.FlounderText;
import net.superkat.flounderlib.impl.text.BuiltinFlounderTexts;

public class SplatText extends FlounderText {
    public static final Identifier ID = Identifier.fromNamespaceAndPath(FlounderLib.MOD_ID, "splat_text");
    public static final MapCodec<SplatText> CODEC = createDefaultCodec(SplatText::new);

    public int textY;
    public int prevTextY;
    public int backgroundY;
    public int prevBackgroundY;

    public int startXOffset;
    public int slideInTicks;
    public int fadeInTicks;
    public int fadeOutTicks;

    public int backgroundId;

    public SplatText(Component text) {
        super(text);

        this.backgroundY = 9; // fallback
        this.prevBackgroundY = backgroundY;
        this.textY = backgroundY + (9 / 2);
        this.prevTextY = textY;

        this.startXOffset = -100;
        this.slideInTicks = 2;
        this.fadeInTicks = 3;
        this.fadeOutTicks = 5;

        this.backgroundId = 0; // fallback
    }

    public void setBackgroundId(int backgroundIdCount) {
        this.backgroundId = this.random.nextInt(backgroundIdCount);
    }

    //    @Override
//    public void draw(DrawContext context, RenderTickCounter tickCounter, int entry, int totalEntries) {
//        int windowWidth = context.getScaledWindowWidth();
//        int windowHeight = context.getScaledWindowHeight();
//        int centerX = windowWidth / 2;
//
//        // Slide in
//        int xOffset = 0;
//        if(this.ticks < this.slideInTicks) {
//            xOffset = MathHelper.lerp(this.getTickDelta(tickCounter) / this.slideInTicks, this.startXOffset, 0);
//        }
//
//        // Fading
//        float alpha = 1f;
//        if(this.ticks <= this.fadeInTicks) { // Fade in
//            alpha = MathHelper.lerp((float) this.ticks / this.fadeInTicks, 0f, 1f);
//        } else if (this.ticks >= this.maxTicks - this.fadeOutTicks) { // Fade out
//            alpha = MathHelper.lerp((float) (this.ticks - (this.maxTicks - this.fadeOutTicks)) / this.fadeOutTicks, 1f, 0f);
//        }
//
//        // Center the text background
//        int backgroundWidth = (int) (windowWidth / 3.5);
//        int backgroundHeight = 16;
//        int backgroundX = centerX - (backgroundWidth / 2) + xOffset;
//        // Slide up the text background
//        this.backgroundY = this.getBackgroundY(windowHeight, entry);
//        this.prevBackgroundY = MathHelper.lerp(tickCounter.getTickProgress(false) / 2f, this.prevBackgroundY, this.backgroundY);;
//
//        // Center the text
//        int textWidth = this.textRenderer.getWidth(this.getText());
//        int textHeight = 9;
//        int textX = centerX - (textWidth / 2) + xOffset;
//        // Slide the text up
//        this.textY = this.backgroundY + (textHeight / 2);
//        this.prevTextY = MathHelper.lerp(tickCounter.getTickProgress(false) / 2f, this.prevTextY, this.textY);;
//
//        // Apply fading effects (if any)
//        int textColor = ColorHelper.withAlpha(alpha, Colors.WHITE);
//
//        // Draw the background and text
//        context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, BACKGROUND_TEXTURES[this.backgroundId], backgroundX, this.prevBackgroundY, backgroundWidth, backgroundHeight, alpha);
//        context.drawTextWithShadow(this.textRenderer, this.getText(), textX, this.prevTextY, textColor);
//    }
//
//    public int getBackgroundY(int windowHeight, int entry) {
//        int backgroundHeight = 16;
//        return windowHeight - 38 - ((backgroundHeight + 1) * (entry + 1));
//    }

    @Override
    public FlounderTextType<?> getFlounderTextType() {
        return BuiltinFlounderTexts.SPLAT_TEXT_TYPE;
    }
}
