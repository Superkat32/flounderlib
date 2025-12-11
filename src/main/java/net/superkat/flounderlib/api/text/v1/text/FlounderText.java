package net.superkat.flounderlib.api.text.v1.text;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.text.Text;
import net.minecraft.text.TextCodecs;
import net.minecraft.util.math.random.Random;
import net.superkat.flounderlib.api.text.v1.registry.FlounderTextType;

import java.util.function.Function;

/**
 * Common-sided component which contains the parameters of a FlounderText.
 */
public abstract class FlounderText {
    public final Random random;

    public Text text;
    public int ticks = 0;
    public int maxTicks = 100;

    public boolean finishedRendering = false;

    public FlounderText(Text text) {
        this.text = text;
        this.random = Random.create();
    }

    public void tick(boolean paused) {
        if(paused) return;

        this.ticks++;
        if(this.ticks >= this.maxTicks) {
            this.setFinishedRendering(true);
        }
    }

    public boolean isTextBlank() {
        String string = this.text.getString();
        return string == null || string.isBlank();
    }

    public boolean isFinishedRendering() {
        return finishedRendering;
    }

    public void setFinishedRendering(boolean finishedRendering) {
        this.finishedRendering = finishedRendering;
    }

    public int getTicks() {
        return ticks;
    }

    public Text getText() {
        return text;
    }

    public abstract FlounderTextType<?> getFlounderTextType();

    public static <T extends FlounderText> MapCodec<T> createDefaultCodec(Function<Text, T> applyFunc) {
        return RecordCodecBuilder.mapCodec(
                instance ->  instance.group(
                        createTextCodec()
                ).apply(instance, applyFunc)
        );
    }

    public static <T extends FlounderText> RecordCodecBuilder<T, Text> createTextCodec() {
        return TextCodecs.CODEC.fieldOf("text").forGetter(FlounderText::getText);
    }
}