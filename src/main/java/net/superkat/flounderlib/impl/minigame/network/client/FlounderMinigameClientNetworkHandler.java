package net.superkat.flounderlib.impl.minigame.network.client;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.util.Identifier;
import net.superkat.flounderlib.api.minigame.v1.FlounderClientApi;
import net.superkat.flounderlib.api.minigame.v1.registry.FlounderGameType;
import net.superkat.flounderlib.api.minigame.v1.sync.FlounderSyncData;
import net.superkat.flounderlib.impl.minigame.game.FlounderRegistry;
import net.superkat.flounderlib.impl.minigame.network.packets.FlounderGameDataUpdateS2CPacket;
import net.superkat.flounderlib.impl.minigame.network.packets.FlounderGameRemoveS2CPacket;

public class FlounderMinigameClientNetworkHandler {

    public static void init() {
        ClientPlayNetworking.registerGlobalReceiver(FlounderGameDataUpdateS2CPacket.ID, FlounderMinigameClientNetworkHandler::onMinigameDataUpdate);
        ClientPlayNetworking.registerGlobalReceiver(FlounderGameRemoveS2CPacket.ID, FlounderMinigameClientNetworkHandler::onMinigameRemove);
    }

    public static <D extends FlounderSyncData> void onMinigameDataUpdate(FlounderGameDataUpdateS2CPacket<D> packet, ClientPlayNetworking.Context context) {
        Identifier gameTypeId = packet.gameTypeId();
        int minigameId = packet.minigameId();
        FlounderGameType<?> gameType = FlounderRegistry.getRegistry().get(gameTypeId);
        FlounderClientApi.getClientGameManager().addMinigameData(gameType, minigameId, packet.data());
    }

    public static void onMinigameRemove(FlounderGameRemoveS2CPacket packet, ClientPlayNetworking.Context context) {
        Identifier gameTypeId = packet.gameId();
        int minigameId = packet.minigameId();
        FlounderGameType<?> gameType = FlounderRegistry.getRegistry().get(gameTypeId);
        FlounderClientApi.getClientGameManager().removeMinigameData(gameType, minigameId);
    }

}
