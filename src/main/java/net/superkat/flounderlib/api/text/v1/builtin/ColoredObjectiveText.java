package net.superkat.flounderlib.api.text.v1.builtin;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.Identifier;
import net.minecraft.util.dynamic.Codecs;
import net.superkat.flounderlib.FlounderLib;
import net.superkat.flounderlib.api.text.v1.registry.FlounderTextType;
import net.superkat.flounderlib.api.text.v1.text.FlounderText;
import net.superkat.flounderlib.impl.text.BuiltinFlounderTexts;

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
    public FlounderTextType<?> getFlounderTextType() {
        return BuiltinFlounderTexts.COLORED_OBJECTIVE_TEXT_TYPE;
    }
}
