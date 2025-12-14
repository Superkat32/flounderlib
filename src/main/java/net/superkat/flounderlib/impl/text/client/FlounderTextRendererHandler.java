package net.superkat.flounderlib.impl.text.client;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.superkat.flounderlib.api.gui.v1.event.client.GuiEvents;
import net.superkat.flounderlib.api.text.v1.client.FlounderTextRenderer;
import net.superkat.flounderlib.api.text.v1.registry.FlounderTextType;
import net.superkat.flounderlib.api.text.v1.text.FlounderText;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.function.Consumer;

public class FlounderTextRendererHandler {
    public static final Map<FlounderTextType<?>, FlounderTextRenderer<?>> RENDERERS = new Object2ObjectOpenHashMap<>();

    public static void init() {
        GuiEvents.END_TICK.register((client, gui, paused) -> {
            forRenderers(renderer -> renderer.tick(paused));
        });

        GuiEvents.GUI_CLEAR.register((client, gui) -> {
            forRenderers(FlounderTextRenderer::clear);
        });

        ClientLifecycleEvents.CLIENT_STARTED.register(client -> {
            forRenderers(FlounderTextRenderer::init);
        });
    }

    public static void register(FlounderTextType<?> textType, FlounderTextRenderer<?> renderer) {
        RENDERERS.put(textType, renderer);
    }

    public static void forRenderers(Consumer<FlounderTextRenderer<?>> rendererConsumer) {
        RENDERERS.values().forEach(rendererConsumer);
    }

    @Nullable
    @SuppressWarnings("unchecked")
    public static <T extends FlounderText> FlounderTextRenderer<T> getRenderer(FlounderTextType<?> textType) {
        return (FlounderTextRenderer<T>) RENDERERS.getOrDefault(textType, null);
    }

}

