package net.superkat.flounderlib.api.text;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.text.Text;
import net.superkat.flounderlib.api.text.type.FlounderTextParams;

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

    public FlounderText(FlounderTextParams.Default params) {
        this(params.getText());
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

    public boolean textBlank() {
        return this.text == null || this.text.getLiteralString() == null || this.text.getLiteralString().isBlank();
    }

}
