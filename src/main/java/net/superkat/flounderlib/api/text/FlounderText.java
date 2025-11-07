package net.superkat.flounderlib.api.text;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.text.Text;
import net.minecraft.text.TextCodecs;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public abstract class FlounderText {
    public final MinecraftClient client;
    public final TextRenderer textRenderer;
    public Text text;
    public int ticks = 0;
    public int maxTicks = 100;

    public FlounderText(Text text) {
        this.client = MinecraftClient.getInstance();
        this.textRenderer = client.textRenderer;
        this.text = text;
    }

    public abstract void render(DrawContext context, RenderTickCounter tickCounter);

    public void tick(boolean paused) {
        if(paused) return;

        this.ticks++;
    }

    public boolean shouldRemove() {
        return this.ticks >= this.maxTicks;
    }

    public Text getText() {
        return this.text;
    }

    public static <T extends FlounderText> Codec<T> createDefaultCodec(Function<Text, T> applyFunction) {
        return RecordCodecBuilder.create(instance ->
            instance.group(
                    getDefaultTextCodec()
            ).apply(instance, applyFunction)
        );
    }

    public static <T extends FlounderText> @NotNull RecordCodecBuilder<T, Text> getDefaultTextCodec() {
        return TextCodecs.CODEC.fieldOf("text").forGetter(FlounderText::getText);
    }
}
