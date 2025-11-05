package net.superkat.flounderlib.network;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.superkat.flounderlib.network.sync.packets.FlounderGameDataUpdateS2CPacket;
import net.superkat.flounderlib.network.sync.packets.FlounderGameRemoveS2CPacket;

public class FlounderPackets {

    public static void init() {

        PayloadTypeRegistry.playS2C().register(FlounderGameDataUpdateS2CPacket.ID, FlounderGameDataUpdateS2CPacket.CODEC);
        PayloadTypeRegistry.playS2C().register(FlounderGameRemoveS2CPacket.ID, FlounderGameRemoveS2CPacket.CODEC);

    }

}
