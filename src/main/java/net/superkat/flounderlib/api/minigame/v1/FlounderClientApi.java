package net.superkat.flounderlib.api.minigame.v1;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.superkat.flounderlib.api.minigame.v1.game.SyncableFlounderableGame;
import net.superkat.flounderlib.api.minigame.v1.registry.FlounderGameType;
import net.superkat.flounderlib.api.minigame.v1.sync.FlounderSyncData;
import net.superkat.flounderlib.impl.minigame.duck.FlounderClientWorld;
import net.superkat.flounderlib.impl.minigame.sync.FlounderClientGameManager;

import java.util.List;

public class FlounderClientApi {
    public static <D extends FlounderSyncData, T extends SyncableFlounderableGame<D>> boolean anyMinigames(FlounderGameType<T> flounderGameType) {
        return !getAllMinigameSyncData(flounderGameType).isEmpty();
    }

    // I can't believe I came up with this myself
    public static <D extends FlounderSyncData, T extends SyncableFlounderableGame<D>> D getMinigameSyncData(FlounderGameType<T> flounderGameType) {
        return getAllMinigameSyncData(flounderGameType).getFirst();
    }

    public static <D extends FlounderSyncData, T extends SyncableFlounderableGame<D>> List<D> getAllMinigameSyncData(FlounderGameType<T> flounderGameType) {
        return getClientGameManager().getDataFromType(flounderGameType);
    }

    public static FlounderClientGameManager getClientGameManager() {
        return getClientGameManager(MinecraftClient.getInstance().world);
    }

    public static FlounderClientGameManager getClientGameManager(ClientWorld world) {
        return ((FlounderClientWorld) world).flounderlib$getClientGameManager();
    }

}
