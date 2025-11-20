package net.superkat.flounderlib.api.text.builtin;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.Identifier;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.util.math.ColorHelper;
import net.superkat.flounderlib.FlounderLib;
import net.superkat.flounderlib.api.text.core.FlounderText;
import net.superkat.flounderlib.api.text.core.FlounderTextType;

public class ColoredObjectiveText extends FlounderText {
    public static final Identifier ID = Identifier.of(FlounderLib.MOD_ID, "colored_objective_text");
    public static final MapCodec<ColoredObjectiveText> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    createTextCodec(),
                    Codecs.ARGB.optionalFieldOf("color", Colors.LIGHT_YELLOW).forGetter(text -> text.color)
            ).apply(instance, ColoredObjectiveText::new)
    );

    public int color;
    public float stretch = 1;
    public float alpha = 0f;

    public ColoredObjectiveText(Text text, int color) {
        super(text);
        this.color = color;
        this.ticks = 60;
    }

    @Override
    public void draw(DrawContext context, RenderTickCounter tickCounter, int entry, int totalEntries) {
        // Stretch in/out and fade in/out the text
//        float endTime = this.maxTime - 100;
//        if(this.time <= 100) {
//            float timeDelta = this.time / 100f;
//            this.stretch = MathHelper.lerp(timeDelta, 3f, 1f);
//            this.alpha = MathHelper.lerp(timeDelta, 0f, 1f);
//        } else if(this.time >= endTime) {
//            float timeDelta = (this.time - endTime) / 100f;
//            this.stretch = MathHelper.lerp(timeDelta, 1f, 3f);
//            this.alpha = MathHelper.lerp(timeDelta, 1f, 0f);
//        } else {
//            this.stretch = MathHelper.lerp(tickCounter.getDynamicDeltaTicks(), this.stretch, 1f);
//            this.alpha = MathHelper.lerp(tickCounter.getDynamicDeltaTicks(), this.alpha, 1f);
//        }

        // Determine positions and color
        int centerX = context.getScaledWindowWidth() / 2;
        int centerY = context.getScaledWindowHeight() / 2;
        int width = this.textRenderer.getWidth(this.text);
        int x = -width / 2;
        int y = centerY / -3;
        int color = ColorHelper.withAlpha(this.alpha, Colors.WHITE);

        // Make shadow color slightly darker to be more readable in brighter environments (e.g. day time)
        int shadowColor = ColorHelper.scaleRgb(ColorHelper.withAlpha(this.alpha, this.color), 0.75f);

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
