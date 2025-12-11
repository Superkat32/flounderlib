package net.superkat.flounderlib.api.text.v1.client;

import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElement;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.superkat.flounderlib.api.text.v1.text.FlounderText;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;

/**
 * Client-sided component used to <i>handle AND render</i> {@link FlounderText}s.<br><br>
 *
 * Available default renderers:
 * <ul>
 *     <li>{@link Abstract} <b>(Not recommend - other renderers should fit your needs!)</b><br> An abstract renderer intended to give helpful variables for rendering.</li>
 *     <li>{@link Simple} The most basic renderer, backed by a {@link List}. Will fit most situations.</li>
 *     <li>{@link Singleton} Only renders the most recently added FlounderText.</li>
 *     <li>{@link Queued} Renders each FlounderText one at a time, based on order added (FIFO)</li>
 *     <li>{@link WordQueued} Renders each word of a FlounderText one at a time (words split by spaces), based on order added (FIFO)</li>
 * </ul>
 */
public interface FlounderTextRenderer<T extends FlounderText> extends HudElement {

    void renderText(DrawContext context, RenderTickCounter tickCounter, T text, int entry);

    default void init() {}

    @Override
    default void render(DrawContext context, RenderTickCounter tickCounter) {
        int entry = 0;

        for (Iterator<T> iterator = this.getTexts().iterator(); iterator.hasNext(); ) {
            T text = iterator.next();

            this.renderText(context, tickCounter, text, entry);
            entry++;

            if(text.isFinishedRendering()) {
                iterator.remove();
            }
        }
    }

    default float getTickAndDelta(T text, RenderTickCounter tickCounter) {
        return text.getTicks() + tickCounter.getTickProgress(false);
    }

    default void tick(boolean paused) {
        this.getTexts().forEach(text -> text.tick(paused));
    }

    default void addText(T text) {
        if(!canAddText(text)) return;
        this.getTexts().add(text);
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    default boolean canAddText(T text) {
        return this.allowBlankTexts() || !text.isTextBlank();
    }

    default boolean allowBlankTexts() {
        return false;
    }

    default void clear() {
        this.getTexts().clear();
    }

    default int getTextsCount() {
        return this.getTexts().size();
    }

    Collection<T> getTexts();

    abstract class Abstract <T extends FlounderText> implements FlounderTextRenderer<T> {
        public final MinecraftClient client;
        public TextRenderer textRenderer = null;

        public Abstract() {
            this.client = MinecraftClient.getInstance();
        }

        @Override
        public void init() {
            this.textRenderer = this.client.textRenderer;
        }
    }

    abstract class Simple<T extends FlounderText> extends Abstract<T> {
        protected final List<T> texts = new ArrayList<>();

        @Override
        public Collection<T> getTexts() {
            return this.texts;
        }
    }

    abstract class Singleton<T extends FlounderText> extends Simple<T> {
        @Override
        public void addText(T text) {
            this.clear();
            super.addText(text);
        }
    }

    abstract class Queued<T extends FlounderText> extends Abstract<T> {
        public final Queue<T> texts = new ArrayDeque<>();
        public T currentText = null;

        @Override
        public void render(DrawContext context, RenderTickCounter tickCounter) {
            if(this.currentText != null) {
                this.renderText(context, tickCounter, this.currentText, 0);
                if(this.currentText.isFinishedRendering()) {
                    this.currentText = null;
                }
            } else {
                if(this.getTexts().isEmpty()) return;
                this.currentText = this.texts.poll();
            }
        }

        @Override
        public void tick(boolean paused) {
            if(this.currentText == null) return;
            this.currentText.tick(paused);
        }

        @Override
        public Collection<T> getTexts() {
            return this.texts;
        }
    }

    abstract class WordQueued<T extends FlounderText> extends Queued<T> {
        public abstract T createTextFromWord(T initText, String word);

        @Override
        public void addText(T text) {
            if(!this.canAddText(text)) return;
            String[] words = text.getText().getLiteralString().split(" ");

            for (String word : words) {
                T wordText = this.createTextFromWord(text, word);
                this.texts.add(wordText);
            }
        }
    }

}
