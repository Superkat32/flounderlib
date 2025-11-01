package net.superkat.flounderlib.api.minigame;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.PlayerAssociatedNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.superkat.flounderlib.api.sync.FlounderSyncData;

import java.util.List;

public interface SyncableFlounderableGame<T extends FlounderSyncData> extends FlounderableGame {

    T createData();

    boolean isDirty();

    void setDirty(boolean dirty);

    CustomPayload createUpdatePacket();

    default void markDirty() {
        this.setDirty(true);
    }

    default void sync(ServerPlayerEntity player) {
        this.sync(List.of(player));
    }

    default void sync(List<ServerPlayerEntity> players) {
        CustomPayload packet = this.createUpdatePacket();

        for (ServerPlayerEntity player : players) {
            if(player == null || player.networkHandler == null) continue;
            sendPacketToPlayer(player.networkHandler, packet);
        }
        this.setDirty(false);
    }

    default void sendPacketToPlayer(PlayerAssociatedNetworkHandler networkHandler, CustomPayload packet) {
        ServerPlayerEntity player = networkHandler.getPlayer();
        ServerPlayNetworking.send(player, packet);
    }
}
