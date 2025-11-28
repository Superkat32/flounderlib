package net.superkat.flounderlib.api.text.builtin;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;
import net.superkat.flounderlib.FlounderLib;
import net.superkat.flounderlib.api.text.core.FlounderText;
import net.superkat.flounderlib.api.text.core.FlounderTextType;

public class WipeoutText extends FlounderText {
    public static final Identifier ID = Identifier.of(FlounderLib.MOD_ID, "wipeout_text");
    public static final MapCodec<WipeoutText> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    createTextCodec(),
                    Codecs.ARGB.optionalFieldOf("color", Colors.YELLOW).forGetter(text -> text.color),
                    Codec.BOOL.optionalFieldOf("positive", true).forGetter(text -> text.positive)
            ).apply(instance, WipeoutText::new)
    );

    public static final Identifier BACKGROUND_TEXTURE = Identifier.of(FlounderLib.MOD_ID, "text/wipeout/background");

    public int color;
    public boolean positive;

    public int textIntroTicks;
    public float letterBounceY;

    public float maxBackgroundAlpha;
    public float maxBackgroundHorizontal;
    public float maxBackgroundVertical;
    public int backgroundFadeIn;
    public int backgroundStretchInTicks;

    public int fadeOutTicks;

    public WipeoutText(Text text, int color, boolean positive) {
        super(text.copy().formatted(Formatting.BOLD));
        this.color = color;
        this.positive = positive;

        this.maxTicks = 60;

        this.textIntroTicks = 8;
        this.letterBounceY = 2f;

        this.maxBackgroundAlpha = 0.5f;
        this.maxBackgroundHorizontal = 12f;
        this.maxBackgroundVertical = 4f;
        this.backgroundFadeIn = 5;
        this.backgroundStretchInTicks = 7;

        this.fadeOutTicks = 10;
    }

    @Override
    public void onAdd() {
        SoundEvent sound = this.positive ? SoundEvents.ITEM_TRIDENT_THUNDER.value() : SoundEvents.ENTITY_ALLAY_DEATH;
        this.playSound(sound, 1f, 2f);
    }

    @Override
    public void draw(DrawContext context, RenderTickCounter tickCounter, int entry, int totalEntries) {
        float textAlpha = 1f;
        float backgroundAlpha = this.maxBackgroundAlpha;
        float backgroundHorizontalStretch = this.maxBackgroundHorizontal;
        float backgroundVerticalStretch = 2f;

        // Fade in the background
        if(this.ticks < this.backgroundFadeIn) {
            float delta = this.getTickDelta(tickCounter) / this.backgroundFadeIn;
            backgroundAlpha = MathHelper.lerp(delta, 0f, this.maxBackgroundAlpha);
        }

        // Stretch in the background
        if(this.ticks < this.backgroundStretchInTicks) { // Horizontal stretch in
            float delta = this.getTickDelta(tickCounter) / this.backgroundStretchInTicks;
            backgroundHorizontalStretch = MathHelper.lerp(delta, 0f, this.maxBackgroundHorizontal);
        } else if(this.ticks < this.maxTicks) {
            float delta = (this.getTickDelta(tickCounter) - (this.backgroundStretchInTicks)) / (this.maxTicks - this.backgroundStretchInTicks);
            backgroundVerticalStretch = MathHelper.lerp(delta, 2f, this.maxBackgroundVertical);
        }

        // Fade out the text and background
        if (this.ticks > this.maxTicks - this.fadeOutTicks) { // Stretch and fade out
            float delta = (this.getTickDelta(tickCounter) - (this.maxTicks - this.fadeOutTicks) - 1) / this.fadeOutTicks;
            backgroundAlpha = MathHelper.lerp(delta, this.maxBackgroundAlpha, 0f);
            textAlpha = MathHelper.lerp(delta, 1f, 0f);
        }

        // Determine positions and color
        int centerX = context.getScaledWindowWidth() / 2;
        int centerY = context.getScaledWindowHeight() / 2;
        int textWidth = this.textRenderer.getWidth(this.text);
        int textHeight = this.textRenderer.getWrappedLinesHeight(this.text, 216);

        int textX = -textWidth / 2;
        int textY = -centerY / 4;

        int backgroundHeight = 16;
        int backgroundX = -textWidth / 2;
        int backgroundY = -backgroundHeight / 2;
        int backgroundColor = ColorHelper.withAlpha(backgroundAlpha, this.color);

        // Center to middle of screen, used by both background and text
        context.getMatrices().pushMatrix();
        context.getMatrices().translate(centerX, centerY);

        // Draw background (I really can't tell you why this works)
        context.getMatrices().pushMatrix();
        context.getMatrices().translate(0, textY - backgroundHeight - textHeight + 4);
        context.getMatrices().scale(backgroundHorizontalStretch, backgroundVerticalStretch);
        context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, BACKGROUND_TEXTURE, backgroundX, backgroundY, textWidth, backgroundHeight, backgroundColor);
        context.getMatrices().popMatrix();

        // Draw the normal text
        int letterX = textX;
        int textColor = ColorHelper.withAlpha(backgroundAlpha, this.positive ? Colors.WHITE : Colors.DARK_GRAY);

        int textLength = this.text.getString().length();
        float letterFadeInTicks = (float) this.textIntroTicks / textLength;

        context.getMatrices().pushMatrix();
        context.getMatrices().scale(2f, 2f); // Scale for text

        // Render each character of the text individually, to allow for the bouncing in effect
        for (int i = 0; i < textLength; i++) {
            Text letter = Text.of(String.valueOf(this.text.getString().charAt(i)));
            letter = letter.copy().formatted(Formatting.BOLD);
            drawLetter(context, tickCounter, letter, letterX, textY, letterFadeInTicks, textColor, textAlpha, i);

            letterX += this.textRenderer.getWidth(letter);
        }

        context.getMatrices().popMatrix();
        context.getMatrices().popMatrix();
    }

    public void drawLetter(DrawContext context, RenderTickCounter tickCounter, Text letter, int x, int y, float fadeInTicks, int color, float alpha, int entry) {
        float ticks = this.getTickDelta(tickCounter);
        float startTicks = fadeInTicks * entry;
        if(ticks < startTicks) return;

        float bounceDelta = easeInBack(Math.min((ticks - startTicks) / fadeInTicks, 1f));
        float alphaDelta = Math.min((ticks - startTicks) / (fadeInTicks * 2f), 1f);

        float yBounce = MathHelper.lerp(bounceDelta, this.letterBounceY, 0f);
        float letterAlpha = MathHelper.lerp(alphaDelta, 0f, 1f) * alpha;
        int letterColor = ColorHelper.withAlpha(letterAlpha, color);

        context.getMatrices().pushMatrix();
        context.getMatrices().translate(x, y - yBounce);
        context.drawTextWithShadow(this.textRenderer, letter, 0, 0, letterColor);
        context.getMatrices().popMatrix();
    }

    // https://easings.net/#easeInBack
    public float easeInBack(float delta) {
        float c1 = 1.70158f;
        float c3 = c1 + 1f;

        return c3 * delta * delta * delta - c1 * delta * delta;
    }

    @Override
    public FlounderTextType<?> getType() {
        return BuiltinFlounderTextRenderers.WIPEOUT_TEXT_TYPE;
    }
}
