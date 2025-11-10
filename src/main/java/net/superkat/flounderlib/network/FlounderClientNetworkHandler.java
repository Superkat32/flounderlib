package net.superkat.flounderlib.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.util.Identifier;
import net.superkat.flounderlib.api.FlounderClientApi;
import net.superkat.flounderlib.api.minigame.gametype.FlounderGameType;
import net.superkat.flounderlib.api.minigame.sync.FlounderSyncData;
import net.superkat.flounderlib.api.text.core.FlounderText;
import net.superkat.flounderlib.minigame.FlounderRegistry;
import net.superkat.flounderlib.network.sync.packets.FlounderGameDataUpdateS2CPacket;
import net.superkat.flounderlib.network.sync.packets.FlounderGameRemoveS2CPacket;
import net.superkat.flounderlib.network.text.FlounderTextS2CPacket;
import net.superkat.flounderlib.text.FlounderTextRegistry;

public class FlounderClientNetworkHandler {

    public static void init() {

        ClientPlayNetworking.registerGlobalReceiver(FlounderGameDataUpdateS2CPacket.ID, FlounderClientNetworkHandler::onMinigameDataUpdate);
        ClientPlayNetworking.registerGlobalReceiver(FlounderGameRemoveS2CPacket.ID, FlounderClientNetworkHandler::onMinigameRemove);

        ClientPlayNetworking.registerGlobalReceiver(FlounderTextS2CPacket.ID, FlounderClientNetworkHandler::onText);
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

    public static void onText(FlounderTextS2CPacket packet, ClientPlayNetworking.Context context) {
        Identifier id = packet.id();
        FlounderText text = packet.text();
        FlounderTextRegistry.getRenderer(id).add(text);
    }

}
