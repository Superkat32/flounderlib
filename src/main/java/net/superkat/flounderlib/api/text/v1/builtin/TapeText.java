package net.superkat.flounderlib.api.text.v1.builtin;

import com.mojang.serialization.Codec;
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

import java.util.ArrayList;
import java.util.List;

public class TapeText extends FlounderText {
    public static final Identifier ID = Identifier.fromNamespaceAndPath(FlounderLib.MOD_ID, "tape_text");
    public static final MapCodec<TapeText> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    createTextCodec(),
                    ExtraCodecs.RGB_COLOR_CODEC.optionalFieldOf("color", CommonColors.HIGH_CONTRAST_DIAMOND).forGetter(text -> text.color),
                    ExtraCodecs.RGB_COLOR_CODEC.optionalFieldOf("text_color", CommonColors.DARK_GRAY).forGetter(text -> text.textColor),
                    Codec.BOOL.optionalFieldOf("text_shadow", true).forGetter(text -> text.textShadow)
            ).apply(instance, TapeText::new)
    );

    public final int color;
    public final int textColor;
    public final boolean textShadow;
    public final List<TapeLine> lines = new ArrayList<>();

    public TapeText(Component text, int color, int textColor, boolean textShadow) {
        super(text);

        this.color = color;
        this.textColor = textColor;
        this.textShadow = textShadow;
    }

    public void addTapeLine(TapeLine line) {
        this.lines.add(line);
    }

    @Override
    public void tick(boolean paused) {
        super.tick(paused);
    }

    @Override
    public FlounderTextType<?> getFlounderTextType() {
        return BuiltinFlounderTexts.TAPE_TEXT_TYPE;
    }

    public static class TapeLine {
        public float startX;
        public float targetX;
        public float startY;
        public float targetY;
        public boolean bounce;

        public float width;
        public float height;

        public float rotation; // IN RADIANS!!!

        public int moveTicks;
        public int fadeInTicks;
        public int fadeOutTicks;

        public TapeLine(
                float startX, float targetX,
                float startY, float targetY,
                float width, float height,
                float rotation, boolean bounce,
                int moveTicks,
                int fadeInTicks, int fadeOutTicks
        ) {
            this.startX = startX;
            this.startY = startY;
            this.targetX = targetX;
            this.targetY = targetY;
            this.width = width;
            this.height = height;
            this.rotation = rotation;
            this.bounce = bounce;
            this.moveTicks = moveTicks;
            this.fadeInTicks = fadeInTicks;
            this.fadeOutTicks = fadeOutTicks;
        }
    }
}
