package net.superkat.flounderlib.api.text.core;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.text.Text;
import net.minecraft.text.TextCodecs;
import net.minecraft.util.Util;

import java.util.function.Function;

public abstract class FlounderText {
    public final MinecraftClient client;
    public final TextRenderer textRenderer;

    public Text text;
    protected boolean finishedRendering = false;

    public long time = 0;
    public long startTime = -1;
    public long maxTime = 5000;

    public FlounderText(Text text) {
        this.text = text;
        this.client = MinecraftClient.getInstance();
        this.textRenderer = client.textRenderer;
    }

    public abstract void draw(DrawContext context, RenderTickCounter tickCounter);

    public void update() {
        long ms = Util.getMeasuringTimeMs();
        if(this.startTime == -1) {
            this.startTime = ms;
        }

        this.time = ms - this.startTime;
        if(this.time >= maxTime) {
            this.setFinishedRendering(true);
        }
    }

    public boolean isTextBlank() {
        String literalString = this.text.getLiteralString();
        return literalString == null || literalString.isBlank();
    }

    public boolean isFinishedRendering() {
        return this.finishedRendering;
    }

    public void setFinishedRendering(boolean finishedRendering) {
        this.finishedRendering = finishedRendering;
    }

    public Text getText() {
        return text;
    }

    public abstract FlounderTextType<?> getType();

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
