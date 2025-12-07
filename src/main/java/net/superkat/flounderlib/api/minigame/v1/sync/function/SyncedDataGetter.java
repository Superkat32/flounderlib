package net.superkat.flounderlib.api.minigame.v1.sync.function;

import net.superkat.flounderlib.api.minigame.v1.game.FlounderableGame;

@FunctionalInterface
public interface SyncedDataGetter<G extends FlounderableGame, T> {
    T get(G game);
}
