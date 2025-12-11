package net.superkat.flounderlib.api.text.v1;

import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.util.Identifier;
import net.superkat.flounderlib.api.text.v1.client.FlounderTextRenderer;
import net.superkat.flounderlib.api.text.v1.registry.FlounderTextType;
import net.superkat.flounderlib.api.text.v1.text.FlounderText;
import net.superkat.flounderlib.impl.text.client.FlounderTextRendererHandler;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;

public class FlounderTextClientApi {

    @SuppressWarnings("UnusedReturnValue")
    public static <T extends FlounderText> FlounderTextRenderer<T> registerDefaultTextRenderer(
            FlounderTextType<T> textType,
            FlounderTextRenderer<T> rendererInstance
    ) {
        return registerTextRenderer(textType, rendererInstance, (identifier, renderer) ->
                HudElementRegistry.attachElementAfter(VanillaHudElements.BOSS_BAR, identifier, renderer)
        );
    }

    public static <T extends FlounderText> FlounderTextRenderer<T> registerTextRenderer(
            FlounderTextType<T> textType,
            FlounderTextRenderer<T> rendererInstance,
            BiConsumer<Identifier, FlounderTextRenderer<T>> rendererIdAndRendererForHudRegistration
    ) {
        return registerTextRenderer(textType, textType.id(), rendererInstance, rendererIdAndRendererForHudRegistration);
    }

    public static <T extends FlounderText> FlounderTextRenderer<T> registerTextRenderer(
            FlounderTextType<T> textType,
            Identifier rendererId,
            FlounderTextRenderer<T> rendererInstance,
            BiConsumer<Identifier, FlounderTextRenderer<T>> rendererIdAndRendererForHudRegistration
    ) {
        rendererIdAndRendererForHudRegistration.accept(rendererId, rendererInstance);
        FlounderTextRendererHandler.register(textType, rendererInstance);
        return rendererInstance;
    }

    @Nullable
    public static <T extends FlounderText> FlounderTextRenderer<T> getTextRenderer(FlounderTextType<?> textType) {
        return FlounderTextRendererHandler.getRenderer(textType);
    }

}
