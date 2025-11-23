package net.superkat.flounderlib.api.text.core;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.text.TextCodecs;
import net.minecraft.util.math.random.Random;

import java.util.function.Function;

public abstract class FlounderText {
    public final MinecraftClient client;
    public final TextRenderer textRenderer;
    public final Random random;

    public Text text;
    protected boolean finishedRendering = false;

    public int ticks = 0;
    public int maxTicks = 100;

    public FlounderText(Text text) {
        this.text = text;
        this.client = MinecraftClient.getInstance();
        this.textRenderer = client.textRenderer;
        this.random = Random.create();
    }

    public void onAdd() {}

    public abstract void draw(DrawContext context, RenderTickCounter tickCounter, int entry, int totalEntries);

    public void tick(boolean paused) {
        if(paused) return;

        this.ticks++;
        if(this.ticks >= this.maxTicks) {
            this.setFinishedRendering(true);
        }
    }

    public void playSound(SoundEvent sound) {
        this.playSound(sound, 1f, 1f);
    }

    public void playSound(SoundEvent sound, float volume, float pitch) {
        this.playSound(sound, this.getSoundCategory(), volume, pitch);
    }

    public void playSound(SoundEvent sound, SoundCategory category, float volume, float pitch) {
        ClientPlayerEntity player = this.client.player;;
        if(player == null) return;

        player.playSoundToPlayer(sound, category, volume, pitch);
    }

    public SoundCategory getSoundCategory() {
        return SoundCategory.PLAYERS;
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

    public void onRemove() {}

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
