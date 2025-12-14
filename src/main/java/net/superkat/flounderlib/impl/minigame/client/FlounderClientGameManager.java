package net.superkat.flounderlib.impl.minigame.client;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.client.multiplayer.ClientLevel;
import net.superkat.flounderlib.api.minigame.v1.registry.FlounderGameType;
import net.superkat.flounderlib.api.minigame.v1.sync.FlounderSyncState;
import net.superkat.flounderlib.impl.minigame.packed.PackedFlGameInfo;
import net.superkat.flounderlib.impl.minigame.sync.FlSyncValue;

import java.util.List;
import java.util.Map;

public class FlounderClientGameManager {
    public final ClientLevel world;

    public final Map<FlounderGameType<?>, FlClientGameList> games = new Object2ObjectOpenHashMap<>();

    public FlounderClientGameManager(ClientLevel world) {
        this.world = world;
    }

    public void addMinigame(PackedFlGameInfo gameInfo) {
        FlounderGameType<?> gameType = gameInfo.gameType();
        int gameId = gameInfo.gameId();

        FlounderSyncState syncState = gameType.stateSyncer().createSyncStateForClient();

        this.games.computeIfAbsent(gameType, (gameType1) -> new FlClientGameList());
        this.games.get(gameType).addSyncState(gameId, syncState);
    }

    public void updateMinigame(PackedFlGameInfo gameInfo, List<FlSyncValue.Packed<?>> values) {
        FlounderGameType<?> gameType = gameInfo.gameType();
        int gameId = gameInfo.gameId();

        if(!this.games.containsKey(gameType)) return;
        this.games.get(gameType).updateSyncState(gameId, values);
    }

    public void removeMinigame(PackedFlGameInfo gameInfo) {
        FlounderGameType<?> gameType = gameInfo.gameType();
        int gameId = gameInfo.gameId();

        if(!this.games.containsKey(gameType)) return;
        this.games.get(gameType).removeSyncState(gameId);
    }

    public FlClientGameList getGameList(FlounderGameType<?> gameType) {
        return this.games.computeIfAbsent(gameType, (gameType1) -> new FlClientGameList());
    }
}
