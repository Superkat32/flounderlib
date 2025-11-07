package net.superkat.flounderlib.minigame.sync;

import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.client.world.ClientWorld;
import net.superkat.flounderlib.api.minigame.gametype.FlounderGameType;
import net.superkat.flounderlib.api.minigame.sync.FlounderSyncData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FlounderClientGameManager {
    public final ClientWorld world;
    public final Map<Integer, FlounderSyncData> games = new Int2ObjectArrayMap<>();
    public final Map<FlounderGameType<?>, List<FlounderSyncData>> gameTypes = new Object2ObjectOpenHashMap<>();

    public FlounderClientGameManager(ClientWorld world) {
        this.world = world;
    }

    public void addMinigameData(FlounderGameType<?> gameType, int minigameId, FlounderSyncData data) {
        FlounderSyncData previous = this.games.put(minigameId, data);
        List<FlounderSyncData> list = this.gameTypes.computeIfAbsent(gameType, type -> new ArrayList<>());
        if(previous != null) {
            list.remove(previous);
        }
        list.add(data);
    }

    public void removeMinigameData(FlounderGameType<?> gameType, int minigameId) {
        FlounderSyncData previous = this.games.remove(minigameId);
        this.gameTypes.get(gameType).remove(previous);
    }

    @SuppressWarnings("unchecked")
    public <T extends FlounderSyncData> List<T> getDataFromType(FlounderGameType<?> gameType) {
        if(!this.gameTypes.containsKey(gameType)) return List.of();
        return (List<T>) this.gameTypes.get(gameType);
    }
}
