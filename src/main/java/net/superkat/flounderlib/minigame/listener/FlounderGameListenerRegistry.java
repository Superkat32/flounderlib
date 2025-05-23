package net.superkat.flounderlib.minigame.listener;

import net.minecraft.util.Identifier;
import net.superkat.flounderlib.api.IFlounderGame;
import net.superkat.flounderlib.api.gametype.FlounderGameType;
import net.superkat.flounderlib.api.minigame.listener.FlounderGameListener;
import net.superkat.flounderlib.minigame.registry.FlounderGameTypeRegistry;

import java.util.HashMap;
import java.util.Map;

public class FlounderGameListenerRegistry {

    public static final Map<FlounderGameType<?>, FlounderGameListener<?>> LISTENERS = new HashMap<>();

    public static <T extends IFlounderGame> FlounderGameListener<T> registerListener(FlounderGameType<T> gameType) {
        FlounderGameListener<T> listener = new FlounderGameListener<>(gameType);
        LISTENERS.put(gameType, listener);
        return listener;
    }

    @SuppressWarnings("unchecked")
    public static <T extends IFlounderGame> void onMinigameAdd(T game) {
        Identifier id = game.getIdentifier();
        FlounderGameTypeRegistry.getRegistry().getOptionalValue(id).ifPresent(flounderGameType -> {
            FlounderGameListener<T> flounderGameListener = (FlounderGameListener<T>) LISTENERS.get(flounderGameType);
            if(flounderGameListener != null) {
                flounderGameListener.addGame(game);
            }
        });
    }

    @SuppressWarnings("unchecked")
    public static <T extends IFlounderGame> void onMinigameDestroy(T game) {
        Identifier id = game.getIdentifier();
        FlounderGameTypeRegistry.getRegistry().getOptionalValue(id).ifPresent(flounderGameType -> {
            FlounderGameListener<T> flounderGameListener = (FlounderGameListener<T>) LISTENERS.get(flounderGameType);
            if(flounderGameListener != null) {
                flounderGameListener.removeGame(game);
            }
        });
    }

}
