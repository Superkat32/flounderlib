package net.superkat.flounderlib.api.render;

import net.superkat.flounderlib.api.IFlounderGame;
import net.superkat.flounderlib.api.gametype.FlounderGameType;

public class FlounderRenderApi {

    public static <T extends IFlounderGame> void registerMinigameHud(
            FlounderGameType<T> minigameType,
            FlounderGameListener<T> hudRendererInstance
    ) {

    }


//    private static final Map<FlounderGameType<?>, FlounderRenderer.Factory<?>> RENDERER_FACTORIES = new Object2ObjectOpenHashMap<>();

//    public static <T extends IFlounderGame> void registerMinigameRenderer(
//            FlounderGameType<T> minigameType,
//            FlounderRenderer.Factory<T> factory,
//            Consumer<FlounderRenderer<T>> rendererInstanceConsumer
//    ) {
//        RENDERER_FACTORIES.put(minigameType, factory);
//        FlounderRenderer<T> renderer = factory.create(minigameType);
//        rendererInstanceConsumer.accept(renderer);
//    }
//
//    @Nullable
//    public static <T extends IFlounderGame> FlounderRenderer<T> getMinigameRendererInstance(FlounderGameType<T> minigameType) {
//        return null;
//    }

}
