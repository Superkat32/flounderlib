package net.superkat.flounderlib.api.text;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElement;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;

import java.util.Collection;
import java.util.Iterator;

public class FlounderTextRenderer<T extends FlounderText> implements HudElement {

    public final Collection<T> texts = this.createCollection();

    public void addText(T flounderText) {
        if(flounderText.getText().getLiteralString().isBlank()) return;
        texts.add(flounderText);
    }

    public void tick(boolean paused) {
        for (Iterator<T> iterator = this.texts.iterator(); iterator.hasNext(); ) {
            T text = iterator.next();
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

    public Collection<T> createCollection() {
        return new ObjectArrayList<>();
    }

}
