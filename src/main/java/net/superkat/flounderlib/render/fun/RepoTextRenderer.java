package net.superkat.flounderlib.render.fun;

import com.google.common.collect.Queues;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.LayeredDrawer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.text.Text;

import java.util.Objects;
import java.util.Queue;

public class RepoTextRenderer implements LayeredDrawer.Layer {

    public static final RepoTextRenderer INSTANCE = new RepoTextRenderer();

    public Queue<RepoText> texts = Queues.newArrayDeque();
    public RepoText currentText = null;

    public void add(Text text) {
        if(text.getLiteralString().isEmpty() || text.getLiteralString().isBlank()) return;
        String[] words = Objects.requireNonNull(text.getLiteralString()).split(" ");

        for (String word : words) {
            texts.add(new RepoText(Text.of(word)));
        }
    }


    @Override
    public void render(DrawContext context, RenderTickCounter tickCounter) {
        if(currentText != null) {
            currentText.render(context, tickCounter);
            if(currentText.isFinished()) {
                currentText = null;
            }
        } else {
            if(texts.isEmpty()) return;
            currentText = texts.poll();
            // this was just a joke but its sorta funny
//            Narrator.getNarrator().say(currentText.text.getLiteralString(), false, 0.7f);
        }
    }

    public void tick() {
        if(this.currentText == null) return;
        this.currentText.tick();
        if(this.texts.size() >= 2) {
            this.currentText.tick(); //speed it up
        }
    }
}
