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

import java.util.ArrayList;
import java.util.List;

public class TapeText extends FlounderText {
    public static final Identifier ID = Identifier.of(FlounderLib.MOD_ID, "tape_text");
    public static final MapCodec<TapeText> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    createTextCodec(),
                    Codecs.RGB.optionalFieldOf("color", Colors.CYAN).forGetter(text -> text.color)
            ).apply(instance, TapeText::new)
    );

    public final int color;
    public final List<TapeLine> lines = new ArrayList<>();

    public TapeText(Text text, int color) {
        super(text);

        this.color = color;
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

        public int width;
        public int height;

        public float rotation; // IN RADIANS!!!

        public int moveTicks;
        public int fadeInTicks;

        public TapeLine(
                float startX, float targetX,
                float startY, float targetY,
                int width, int height,
                float rotation, boolean bounce,
                int moveTicks, int fadeInTicks
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
        }
    }
}
