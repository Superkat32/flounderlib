package net.superkat.flounderlib.api.text.v1.builtin;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.CommonColors;
import net.minecraft.util.ExtraCodecs;
import net.superkat.flounderlib.FlounderLib;
import net.superkat.flounderlib.api.text.v1.registry.FlounderTextType;
import net.superkat.flounderlib.api.text.v1.text.FlounderText;
import net.superkat.flounderlib.impl.text.BuiltinFlounderTexts;

public class ColoredObjectiveText extends FlounderText {
    public static final Identifier ID = Identifier.fromNamespaceAndPath(FlounderLib.MOD_ID, "colored_objective_text");
    public static final MapCodec<ColoredObjectiveText> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    createTextCodec(),
                    ExtraCodecs.ARGB_COLOR_CODEC.optionalFieldOf("color", CommonColors.SOFT_YELLOW).forGetter(text -> text.color)
            ).apply(instance, ColoredObjectiveText::new)
    );

    public int color;

    public int fadeInTicks;
    public int fadeOutTicks;
    public float maxStretch;

    public ColoredObjectiveText(Component text, int color) {
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
