package net.superkat.flounderlib.api.minigame.v1.game;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.PlayerAssociatedNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.List;

public interface SyncableFlounderableGame extends FlounderableGame {

    boolean isDirty();

    void setDirty(boolean dirty);

    default void markDirty() {
        this.setDirty(true);
    }

    default void syncAdd(ServerPlayerEntity player) {
        this.syncAdd(List.of(player));
    }

    default void syncAdd(List<ServerPlayerEntity> players) {
        CustomPayload packet = this.createAddPacket();
        this.sendPacketToPlayers(players, packet);
    }

    default void syncUpdate(ServerPlayerEntity player) {
        this.syncUpdate(List.of(player));
    }

    default void syncUpdate(List<ServerPlayerEntity> players) {
        CustomPayload packet = this.createUpdatePacket();
        this.sendPacketToPlayers(players, packet);
        this.setDirty(false);
    }

    default void syncRemove(ServerPlayerEntity player) {
        this.syncRemove(List.of(player));
    }

    default void syncRemove(List<ServerPlayerEntity> players) {
        CustomPayload packet = this.createRemovePacket();
        this.sendPacketToPlayers(players, packet);
    }

    default void sendPacketToPlayer(ServerPlayerEntity player, CustomPayload packet) {
        this.sendPacketToPlayers(List.of(player), packet);
    }

    default void sendPacketToPlayers(List<ServerPlayerEntity> players, CustomPayload packet) {
        for (ServerPlayerEntity player : players) {
            if(player == null || player.networkHandler == null) continue;
            sendPacketToPlayer(player.networkHandler, packet);
        }
    }

    default void sendPacketToPlayer(PlayerAssociatedNetworkHandler networkHandler, CustomPayload packet) {
        ServerPlayerEntity player = networkHandler.getPlayer();
        ServerPlayNetworking.send(player, packet);
    }

    CustomPayload createAddPacket();

    CustomPayload createUpdatePacket();

    CustomPayload createRemovePacket();
}
