package net.superkat.flounderlib.api.text.core;

import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElement;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;

public interface FlounderTextRenderer<T extends FlounderText> extends HudElement {

    static <T extends FlounderText> Simple<T> createSimple() {
        return new FlounderTextRenderer.Simple<T>();
    }

    static <T extends FlounderText> Singleton<T> createSingleton() {
        return new FlounderTextRenderer.Singleton<T>();
    }

    static <T extends FlounderText> Queued<T> createQueued() {
        return new FlounderTextRenderer.Queued<T>();
    }

    static <T extends FlounderText> Queued<T> createWordQueued(WordSplitter<T> wordSplitter) {
        return new FlounderTextRenderer.WordQueued<T>(wordSplitter);
    }

    default void add(T text) {
        if(text.isTextBlank()) return;
        this.getTexts().add(text);
    }

    @Override
    default void render(DrawContext context, RenderTickCounter tickCounter) {
        for (Iterator<T> iterator = this.getTexts().iterator(); iterator.hasNext(); ) {
            T text = iterator.next();
            renderText(text, context, tickCounter);
            if (text.isFinishedRendering()) {
                iterator.remove();
            }
        }
    }

    default void renderText(T text, DrawContext context, RenderTickCounter tickCounter) {
        text.draw(context, tickCounter);
        text.update();
    }

    default void clear() {
        this.getTexts().clear();
    }

    Collection<T> getTexts();

    static class Simple<T extends FlounderText> implements FlounderTextRenderer<T> {
        public final List<T> texts = new ArrayList<>();

        @Override
        public Collection<T> getTexts() {
            return this.texts;
        }
    }

    static class Singleton<T extends FlounderText> extends Simple<T> {
        @Override
        public void add(T text) {
            this.clear();
            this.texts.add(text);
        }
    }

    static class Queued<T extends FlounderText> implements FlounderTextRenderer<T> {
        public final Queue<T> texts = new ArrayDeque<>();
        public T currentText = null;

        @Override
        public void render(DrawContext context, RenderTickCounter tickCounter) {
            if(this.currentText != null) {
                this.renderText(this.currentText, context, tickCounter);
                if(this.currentText.isFinishedRendering()) {
                    this.currentText = null;
                }
            } else {
                if(this.texts.isEmpty()) return;
                this.currentText = this.texts.poll();
            }
        }

        @Override
        public Collection<T> getTexts() {
            return this.texts;
        }
    }

    static class WordQueued<T extends FlounderText> extends Queued<T> {
        public final WordSplitter<T> wordSplitter;

        public WordQueued(WordSplitter<T> wordSplitter) {
            this.wordSplitter = wordSplitter;
        }

        @Override
        public void add(T text) {
            if(text.isTextBlank()) return;
            String[] words = text.getText().getLiteralString().split(" ");

            for (String word : words) {
                T wordText = this.wordSplitter.createTextFromWord(text, word);
                this.texts.add(wordText);
            }
        }
    }

    @FunctionalInterface
    static interface WordSplitter<T extends FlounderText> {
        T createTextFromWord(T initText, String word);
    }
}
