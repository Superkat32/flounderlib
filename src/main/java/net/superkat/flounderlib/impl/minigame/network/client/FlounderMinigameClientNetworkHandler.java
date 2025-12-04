package net.superkat.flounderlib.impl.minigame.network.client;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.text.Text;
import net.superkat.flounderlib.api.minigame.v1.FlounderClientApi;
import net.superkat.flounderlib.impl.minigame.network.packets.FlounderGameAddS2CPacket;
import net.superkat.flounderlib.impl.minigame.network.packets.FlounderGameRemoveS2CPacket;
import net.superkat.flounderlib.impl.minigame.network.packets.FlounderGameUpdateS2CPacket;
import net.superkat.flounderlib.impl.minigame.packed.PackedFlGameInfo;
import net.superkat.flounderlib.impl.minigame.sync.FlDataValue;

import java.util.List;

public class FlounderMinigameClientNetworkHandler {

    public static void init() {
        ClientPlayNetworking.registerGlobalReceiver(FlounderGameAddS2CPacket.ID, FlounderMinigameClientNetworkHandler::onMinigameAdd);
        ClientPlayNetworking.registerGlobalReceiver(FlounderGameUpdateS2CPacket.ID, FlounderMinigameClientNetworkHandler::onMinigameUpdate);
        ClientPlayNetworking.registerGlobalReceiver(FlounderGameRemoveS2CPacket.ID, FlounderMinigameClientNetworkHandler::onMinigameRemove);
    }

    public static void onMinigameAdd(FlounderGameAddS2CPacket packet, ClientPlayNetworking.Context context) {
        PackedFlGameInfo gameInfo = packet.gameInfo();
        FlounderClientApi.getClientGameManager().addMinigame(gameInfo);
    }

    public static void onMinigameUpdate(FlounderGameUpdateS2CPacket packet, ClientPlayNetworking.Context context) {
        PackedFlGameInfo gameInfo = packet.gameInfo();
        List<FlDataValue.Packed<?>> values = packet.values();
        FlounderClientApi.getClientGameManager().updateMinigame(gameInfo, values);

        // Debug
        context.player().sendMessage(Text.of(String.valueOf(values.size())), true);
    }

    public static void onMinigameRemove(FlounderGameRemoveS2CPacket packet, ClientPlayNetworking.Context context) {
        PackedFlGameInfo gameInfo = packet.gameInfo();
        FlounderClientApi.getClientGameManager().removeMinigame(gameInfo);
    }

}
