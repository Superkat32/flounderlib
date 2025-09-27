package net.superkat.flounderlib.network;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.superkat.flounderlib.network.fun.packets.RepoTextS2CPacket;

public class FlounderPackets {

    public static void init() {
//        PayloadTypeRegistry.playS2C().register(FlounderGameCreationS2CPacket.ID, FlounderGameCreationS2CPacket.CODEC);
//        PayloadTypeRegistry.playS2C().register(FlounderDataTrackerUpdateS2CPacket.ID, FlounderDataTrackerUpdateS2CPacket.CODEC);
//        PayloadTypeRegistry.playS2C().register(FlounderGameDestroyS2CPacket.ID, FlounderGameDestroyS2CPacket.CODEC);
//
//        PayloadTypeRegistry.playS2C().register(FlounderGameUpdateS2CPacket.ID, FlounderGameUpdateS2CPacket.CODEC);

        PayloadTypeRegistry.playS2C().register(RepoTextS2CPacket.ID, RepoTextS2CPacket.CODEC);
    }

}
