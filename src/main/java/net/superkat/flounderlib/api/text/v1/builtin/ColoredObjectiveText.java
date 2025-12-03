package net.superkat.flounderlib.api.text.v1.builtin;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.Identifier;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;
import net.superkat.flounderlib.FlounderLib;
import net.superkat.flounderlib.api.text.v1.text.FlounderText;
import net.superkat.flounderlib.api.text.v1.text.FlounderTextType;

public class ColoredObjectiveText extends FlounderText {
    public static final Identifier ID = Identifier.of(FlounderLib.MOD_ID, "colored_objective_text");
    public static final MapCodec<ColoredObjectiveText> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    createTextCodec(),
                    Codecs.ARGB.optionalFieldOf("color", Colors.LIGHT_YELLOW).forGetter(text -> text.color)
            ).apply(instance, ColoredObjectiveText::new)
    );

    public int color;

    public int fadeInTicks;
    public int fadeOutTicks;
    public float maxStretch;

    public ColoredObjectiveText(Text text, int color) {
        super(text);
        this.color = color;
        this.maxTicks = 60;

        this.fadeInTicks = 2;
        this.fadeOutTicks = 2;
        this.maxStretch = 3f;
    }

    @Override
    public void draw(DrawContext context, RenderTickCounter tickCounter, int entry, int totalEntries) {
        float stretch = 1f;
        float alpha = 1f;

        // Stretch in/out and fade in/out the text
        if(this.ticks < this.fadeInTicks) { // Stretch and fade in
            float delta = this.getTickDelta(tickCounter) / this.fadeInTicks;
            stretch = MathHelper.lerp(delta, this.maxStretch, 1f);
            alpha = MathHelper.lerp(delta, 0f, 1f);
        } else if (this.ticks > this.maxTicks - this.fadeOutTicks) { // Stretch and fade out
            float delta = (this.getTickDelta(tickCounter) - (this.maxTicks - this.fadeOutTicks) - 1) / this.fadeOutTicks;
            stretch = MathHelper.lerp(delta, 1f, this.maxStretch);
            alpha = MathHelper.lerp(delta, 1f, 0f);
        }

        // Determine positions and color
        int centerX = context.getScaledWindowWidth() / 2;
        int centerY = context.getScaledWindowHeight() / 2;
        int width = this.textRenderer.getWidth(this.text);
        int x = -width / 2;
        int y = centerY / -3;
        int color = ColorHelper.withAlpha(alpha, Colors.WHITE);

        // Make shadow color slightly darker to be more readable in brighter environments (e.g. day time)
        int shadowColor = ColorHelper.scaleRgb(ColorHelper.withAlpha(alpha, this.color), 0.75f);

        // Translate to center of screen, then scale up, then render
        context.getMatrices().pushMatrix();
        context.getMatrices().translate(centerX, centerY);
        context.getMatrices().scale(2f * stretch, 2f);

        // Draw the colored shadow text slightly offset
        float offsetX = 0.5f;
        float offsetY = 0.5f;
        context.getMatrices().translate(offsetX, offsetY);
        context.drawText(this.textRenderer, this.text, x, y, shadowColor, false);
        context.getMatrices().translate(-offsetX, -offsetY);

        // Draw the normal text
        context.drawText(this.textRenderer, this.text, x, y, color, false);

        context.getMatrices().popMatrix();
    }

    @Override
    public FlounderTextType<?> getType() {
        return BuiltinFlounderTextRenderers.COLORED_OBJECTIVE_TEXT_TYPE;
    }
}
