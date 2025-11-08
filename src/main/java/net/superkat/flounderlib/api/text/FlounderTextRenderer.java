package net.superkat.flounderlib.api.text;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElement;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;

import java.util.Iterator;
import java.util.List;

public class FlounderTextRenderer implements HudElement {
    public static final FlounderTextRenderer DEFAULT_INSTANCE = new FlounderTextRenderer();
    public final List<FlounderText> texts = new ObjectArrayList<>();

    public void addText(FlounderText flounderText) {
        if(flounderText.textBlank()) return;
        this.texts.add(flounderText);
    }

    public void tick(boolean paused) {
        for (Iterator<FlounderText> iterator = this.texts.iterator(); iterator.hasNext(); ) {
            FlounderText text = iterator.next();
            text.tick(paused);

            if(text.shouldRemove()) {
                iterator.remove();
            }
        }
    }

    @Override
    public void render(DrawContext context, RenderTickCounter tickCounter) {
        this.texts.forEach(text -> text.render(context, tickCounter));
    }

    public void clear() {
        this.texts.clear();
    }
}
