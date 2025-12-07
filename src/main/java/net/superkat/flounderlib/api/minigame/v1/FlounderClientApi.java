package net.superkat.flounderlib.api.minigame.v1;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.superkat.flounderlib.api.minigame.v1.game.SyncableFlounderableGame;
import net.superkat.flounderlib.api.minigame.v1.registry.FlounderGameType;
import net.superkat.flounderlib.api.minigame.v1.sync.FlounderStateSyncer;
import net.superkat.flounderlib.api.minigame.v1.sync.FlounderSyncState;
import net.superkat.flounderlib.impl.minigame.client.FlounderClientGameManager;
import net.superkat.flounderlib.impl.minigame.duck.FlounderClientWorld;

import java.util.List;

public class FlounderClientApi {

//    public static <T extends SyncableFlounderableGame, V> V getSyncValue(FlounderGameType<T> flounderGameType, FlDataKey<V> dataKey) {
//        return getFirstSyncState(flounderGameType).getValue(dataKey);
//    }
//
//    public static <T extends SyncableFlounderableGame> FlounderSyncTracker getFirstSyncState(FlounderGameType<T> flounderGameType) {
//        return getSyncStates(flounderGameType).getFirst();
//    }
//
//    public static <T extends SyncableFlounderableGame> List<FlounderSyncTracker> getSyncStates(FlounderGameType<T> flounderGameType) {
//        return getClientGameManager().getGameList(flounderGameType).getSyncStates();
//    }
//
//    public static <T extends SyncableFlounderableGame> boolean anyMinigames(FlounderGameType<T> flounderGameType) {
//        return !getSyncStates(flounderGameType).isEmpty();
//    }

    public static <G extends SyncableFlounderableGame, S extends FlounderSyncState> S getFirstSyncState(FlounderStateSyncer<G, S> stateSyncer) {
        return getSyncStates(stateSyncer).getFirst();
    }

    @SuppressWarnings("unchecked")
    public static <G extends SyncableFlounderableGame, S extends FlounderSyncState> List<S> getSyncStates(FlounderStateSyncer<G, S> stateSyncer) {
        return (List<S>) getClientGameManager().getGameList(stateSyncer.getFlounderGameType()).getSyncStates();
    }

    public static <T extends SyncableFlounderableGame> boolean anyMinigames(FlounderGameType<T> flounderGameType) {
        return !getClientGameManager().getGameList(flounderGameType).getSyncStates().isEmpty();
    }

    public static FlounderClientGameManager getClientGameManager() {
        return getClientGameManager(MinecraftClient.getInstance().world);
    }

    public static FlounderClientGameManager getClientGameManager(ClientWorld world) {
        return ((FlounderClientWorld) world).flounderlib$getClientGameManager();
    }

}
