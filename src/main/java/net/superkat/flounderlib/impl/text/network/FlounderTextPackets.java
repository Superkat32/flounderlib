package net.superkat.flounderlib.impl.text.network;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.superkat.flounderlib.impl.text.network.packets.FlounderTextS2CPacket;

public class FlounderTextPackets {

    public static void init() {
        PayloadTypeRegistry.playS2C().register(FlounderTextS2CPacket.ID, FlounderTextS2CPacket.CODEC);
    }

}
