package net.superkat.flounderlib.api.minigame.v1.sync.function;

import net.superkat.flounderlib.api.minigame.v1.sync.FlounderSyncState;

@FunctionalInterface
public interface SyncedDataSetter<S extends FlounderSyncState, T> {
    void set(S syncState, T value);
}
