package net.superkat.flounderlib.impl.minigame.client;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.superkat.flounderlib.impl.minigame.sync.FlDataValue;
import net.superkat.flounderlib.impl.minigame.sync.FlounderSyncState;

import java.util.List;
import java.util.Map;

public class FlClientGameList {

    public final Map<Integer, FlounderSyncState> syncStates = new Object2ObjectOpenHashMap<>();

    public void addSyncState(int minigameId, FlounderSyncState syncState) {
        this.syncStates.put(minigameId, syncState);
    }

    public void updateSyncState(int minigameId, List<FlDataValue.Packed<?>> values) {
        if(!this.contains(minigameId)) return;

        FlounderSyncState syncState = syncStates.get(minigameId);
        syncState.update(values);
    }


    public void removeSyncState(int minigameId) {
        this.syncStates.remove(minigameId);
    }

    public FlounderSyncState getSyncState(int minigameId) {
        return this.syncStates.get(minigameId);
    }

    public List<FlounderSyncState> getSyncStates() {
        return List.copyOf(this.syncStates.values());
    }

    public boolean contains(int minigameId) {
        return this.syncStates.containsKey(minigameId);
    }

}
