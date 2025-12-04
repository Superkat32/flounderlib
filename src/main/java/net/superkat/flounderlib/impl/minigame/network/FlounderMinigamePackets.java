package net.superkat.flounderlib.impl.minigame.network;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.superkat.flounderlib.impl.minigame.network.packets.FlounderGameAddS2CPacket;
import net.superkat.flounderlib.impl.minigame.network.packets.FlounderGameRemoveS2CPacket;
import net.superkat.flounderlib.impl.minigame.network.packets.FlounderGameUpdateS2CPacket;

public class FlounderMinigamePackets {

    public static void init() {
        PayloadTypeRegistry.playS2C().register(FlounderGameAddS2CPacket.ID, FlounderGameAddS2CPacket.CODEC);
        PayloadTypeRegistry.playS2C().register(FlounderGameUpdateS2CPacket.ID, FlounderGameUpdateS2CPacket.CODEC);
        PayloadTypeRegistry.playS2C().register(FlounderGameRemoveS2CPacket.ID, FlounderGameRemoveS2CPacket.CODEC);
    }

}
