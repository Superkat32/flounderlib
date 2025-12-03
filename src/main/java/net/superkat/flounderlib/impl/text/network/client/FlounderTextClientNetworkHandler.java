package net.superkat.flounderlib.impl.text.network.client;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.util.Identifier;
import net.superkat.flounderlib.api.text.v1.text.FlounderText;
import net.superkat.flounderlib.impl.text.network.packets.FlounderTextS2CPacket;
import net.superkat.flounderlib.impl.text.registry.FlounderTextRegistry;

public class FlounderTextClientNetworkHandler {

    public static void init() {
        ClientPlayNetworking.registerGlobalReceiver(FlounderTextS2CPacket.ID, FlounderTextClientNetworkHandler::onText);
    }
    public static void onText(FlounderTextS2CPacket packet, ClientPlayNetworking.Context context) {
        Identifier id = packet.id();
        FlounderText text = packet.text();
        FlounderTextRegistry.getRenderer(id).add(text);
    }

}
