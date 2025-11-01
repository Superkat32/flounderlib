package net.superkat.flounderlib.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.util.Identifier;
import net.superkat.flounderlib.api.FlounderClientApi;
import net.superkat.flounderlib.api.gametype.FlounderGameType;
import net.superkat.flounderlib.api.sync.FlounderSyncData;
import net.superkat.flounderlib.minigame.FlounderRegistry;
import net.superkat.flounderlib.network.sync.packets.FlounderGameDataUpdateS2CPacket;

public class FlounderClientNetworkHandler {

    public static void init() {

        ClientPlayNetworking.registerGlobalReceiver(FlounderGameDataUpdateS2CPacket.ID, FlounderClientNetworkHandler::onMinigameDataUpdate);

    }

    public static <D extends FlounderSyncData> void onMinigameDataUpdate(FlounderGameDataUpdateS2CPacket<D> packet, ClientPlayNetworking.Context context) {
        Identifier gameTypeId = packet.gameTypeId();
        int minigameId = packet.minigameId();
        FlounderGameType<?> gameType = FlounderRegistry.getRegistry().get(gameTypeId);
        FlounderClientApi.getClientGameManager().addMinigameData(gameType, minigameId, packet.data());
    }

}
