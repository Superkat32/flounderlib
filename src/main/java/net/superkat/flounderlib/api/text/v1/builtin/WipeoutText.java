package net.superkat.flounderlib.api.text.v1.builtin;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.dynamic.Codecs;
import net.superkat.flounderlib.FlounderLib;
import net.superkat.flounderlib.api.text.v1.registry.FlounderTextType;
import net.superkat.flounderlib.api.text.v1.text.FlounderText;
import net.superkat.flounderlib.impl.text.BuiltinFlounderTexts;

public class WipeoutText extends FlounderText {
    public static final Identifier ID = Identifier.of(FlounderLib.MOD_ID, "wipeout_text");
    public static final MapCodec<WipeoutText> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    createTextCodec(),
                    Codecs.ARGB.optionalFieldOf("color", Colors.YELLOW).forGetter(text -> text.color),
                    Codec.BOOL.optionalFieldOf("positive", true).forGetter(text -> text.positive)
            ).apply(instance, WipeoutText::new)
    );

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

        this.textIntroTicks = 7;
        this.letterBounceY = 2f;

        this.maxBackgroundAlpha = 0.5f;
        this.maxBackgroundHorizontal = 12f;
        this.maxBackgroundVertical = 4f;
        this.backgroundFadeIn = 5;
        this.backgroundStretchInTicks = 8;

        this.fadeOutTicks = 10;
    }

    @Override
    public FlounderTextType<?> getFlounderTextType() {
        return BuiltinFlounderTexts.WIPEOUT_TEXT_TYPE;
    }
}
