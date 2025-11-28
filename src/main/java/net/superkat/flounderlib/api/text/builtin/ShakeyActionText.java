package net.superkat.flounderlib.api.text.builtin;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;
import net.superkat.flounderlib.FlounderLib;
import net.superkat.flounderlib.api.text.core.FlounderText;
import net.superkat.flounderlib.api.text.core.FlounderTextType;

public class ShakeyActionText extends FlounderText {
    public static final Identifier ID = Identifier.of(FlounderLib.MOD_ID, "shakey_action_text");
    public static final MapCodec<ShakeyActionText> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    createTextCodec()
            ).apply(instance, ShakeyActionText::new)
    );

    public int bounceInTicks;
    public int fadeOutTicks;

    public int color;
    public int shadowColor;
    public float maxBounceScale;
    public float maxStretch;

    public ShakeyActionText(Text text) {
        super(text);

        this.maxTicks = 80;

        this.bounceInTicks = 16;
        this.fadeOutTicks = 4;

        this.color = ColorHelper.fromFloats(1f, 0.9f, 0.85f, 0.18f);
        this.shadowColor = ColorHelper.fromFloats(1f, 0.75f, 0.22f, 0f);
        this.maxBounceScale = 2f;
        this.maxStretch = 3f;
    }

    @Override
    public void onAdd() {
        this.playSound(SoundEvents.ITEM_TRIDENT_THUNDER.value(), 1f, 2f);
        this.playSound(SoundEvents.ENTITY_ENDER_DRAGON_HURT, 1f, 1.25f);
        this.playSound(SoundEvents.ENTITY_ENDER_DRAGON_HURT, 1f, 1.7f);
        this.playSound(SoundEvents.ENTITY_WARDEN_DEATH, 1f, 1.7f);
    }

    @Override
    public void draw(DrawContext context, RenderTickCounter tickCounter, int entry, int totalEntries) {
        float scale = 1f;
        float stretch = 1f;
        float alpha = 1f;

        // Stretch in/out and fade in/out the text
        if(this.ticks < this.bounceInTicks) { // Stretch and fade in
            float delta = this.getTickDelta(tickCounter) / this.bounceInTicks;
            scale = MathHelper.lerp(easeOutElastic(delta), 0f, 1f);
        } else if (this.ticks > this.maxTicks - this.fadeOutTicks) { // Stretch and fade out
            float delta = (this.getTickDelta(tickCounter) - (this.maxTicks - this.fadeOutTicks) - 1) / this.fadeOutTicks;
            stretch = MathHelper.lerp(delta, 1f, this.maxStretch);
            alpha = MathHelper.lerp(delta, 1f, 0f);
        }

        // Determine positions and color
        int centerX = context.getScaledWindowWidth() / 2;
        int centerY = context.getScaledWindowHeight() / 2;
        int width = this.textRenderer.getWidth(this.text);
        int height = this.textRenderer.getWrappedLinesHeight(this.text, 256);
        int x = -width / 2;
        int y = centerY / -3;
        int color = ColorHelper.withAlpha(alpha, this.color);
        int shadowColor = ColorHelper.withAlpha(alpha, this.shadowColor);

        // Translate to center of screen, then scale up, then render
        context.getMatrices().pushMatrix();
        context.getMatrices().translate(centerX, centerY);
        context.getMatrices().scale(2f * scale * stretch, 2f * scale);

        // Draw the normal text
        float tickDelta = this.getTickDelta(tickCounter);
        int letters = this.text.getString().length();
        int letterX = x;

        float shadowX = 0.5f;
        float shadowY = 0.5f;

        for (int i = 0; i < letters; i++) {
            Text letter = Text.literal(String.valueOf(this.text.getString().charAt(i))).getWithStyle(this.text.getStyle()).getFirst();
            float rotation = MathHelper.sin(tickDelta * 2f + i * 4f) / 32f;

            // I really can't begin to tell you the math that's going on here
            float scaleDelta = (tickDelta / 2f) - (i / 2f);
//            float extraScale = Math.max((MathHelper.sin(scaleDelta) - MathHelper.cos(scaleDelta * 2f)) / 24f + 1f, 1);
            float extraScale = Math.max((MathHelper.sin(scaleDelta) - MathHelper.cos(scaleDelta * 2f)) / 48f + 1f, 1);

            context.getMatrices().pushMatrix();
            context.getMatrices().scale(extraScale);
            context.getMatrices().rotateAbout(rotation, letterX, y);

            // Draw "shadow" (alternative background color)
            context.getMatrices().translate(shadowX, shadowY);
            context.drawText(this.textRenderer, letter, letterX, y, shadowColor, false);
            context.getMatrices().translate(-shadowX, -shadowY);

            // Draw normal letter
            context.drawText(this.textRenderer, letter, letterX, y, color, false);
            context.getMatrices().popMatrix();

            letterX += this.textRenderer.getWidth(letter);
        }

        context.getMatrices().popMatrix();
    }

    // https://easings.net/#easeOutElastic
    public float easeOutElastic(float delta) {
        float c4 = (2f * MathHelper.PI) / 3f;

        return delta == 0
                ? 0
                : (float) (delta == 1
                ? 1
                : Math.pow(2, -10 * delta) * Math.sin((delta * 10 - 0.75) * c4) + 1);
    }

    @Override
    public FlounderTextType<?> getType() {
        return BuiltinFlounderTextRenderers.SHAKEY_ACTION_TEXT_TYPE;
    }
}
