package net.superkat.flounderlib.impl.minigame.client;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.superkat.flounderlib.api.minigame.v1.sync.FlounderSyncState;
import net.superkat.flounderlib.impl.minigame.sync.FlSyncValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FlClientGameList {
    // TODO - proper ordering on the client based on something (start time?)
    public final Map<Integer, FlounderSyncState> syncStates = new Object2ObjectOpenHashMap<>();
    public final List<FlounderSyncState> orderedSyncStates = new ArrayList<>();

    public void addSyncState(int minigameId, FlounderSyncState syncState) {
        this.syncStates.put(minigameId, syncState);
        this.orderedSyncStates.add(syncState);
    }

    public void updateSyncState(int minigameId, List<FlSyncValue.Packed<?>> values) {
        if(!this.contains(minigameId)) return;

        FlounderSyncState syncState = this.syncStates.get(minigameId);
        for (FlSyncValue.Packed<?> value : values) {
            value.cursedSet(syncState);
        }
    }

    public void removeSyncState(int minigameId) {
        FlounderSyncState syncState = this.syncStates.remove(minigameId);
        this.orderedSyncStates.remove(syncState);
    }

    public List<FlounderSyncState> getSyncStates() {
        return List.copyOf(this.orderedSyncStates);
    }

    public boolean contains(int minigameId) {
        return this.syncStates.containsKey(minigameId);
    }

}
