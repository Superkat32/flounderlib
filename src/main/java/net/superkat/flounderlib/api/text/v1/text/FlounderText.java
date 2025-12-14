package net.superkat.flounderlib.api.text.v1.text;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.util.RandomSource;
import net.superkat.flounderlib.api.text.v1.registry.FlounderTextType;

import java.util.function.Function;

/**
 * Common-sided component which contains the parameters of a FlounderText.
 */
public abstract class FlounderText {
    public final RandomSource random;

    public Component text;
    public int ticks = 0;
    public int maxTicks = 100;

    public boolean finishedRendering = false;

    public FlounderText(Component text) {
        this.text = text;
        this.random = RandomSource.create();
    }

    // TODO - give paused and frozen (from hud event probably)
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

    public Component getText() {
        return text;
    }

    public abstract FlounderTextType<?> getFlounderTextType();

    public static <T extends FlounderText> MapCodec<T> createDefaultCodec(Function<Component, T> applyFunc) {
        return RecordCodecBuilder.mapCodec(
                instance ->  instance.group(
                        createTextCodec()
                ).apply(instance, applyFunc)
        );
    }

    public static <T extends FlounderText> RecordCodecBuilder<T, Component> createTextCodec() {
        return ComponentSerialization.CODEC.fieldOf("text").forGetter(FlounderText::getText);
    }
}