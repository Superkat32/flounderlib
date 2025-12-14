package net.superkat.flounderlib.api.minigame.v1.game;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerPlayerConnection;

import java.util.List;

public interface SyncableFlounderableGame extends FlounderableGame {

    boolean isDirty();

    void setDirty(boolean dirty);

    default void markDirty() {
        this.setDirty(true);
    }

    default void syncAdd(ServerPlayer player) {
        this.syncAdd(List.of(player));
    }

    default void syncAdd(List<ServerPlayer> players) {
        CustomPacketPayload packet = this.createAddPacket();
        this.sendPacketToPlayers(players, packet);

        // TODO - custom bundle packet
        List<CustomPacketPayload> extraPackets = this.createExtraAddPackets();
        for (CustomPacketPayload extraPacket : extraPackets) {
            this.sendPacketToPlayers(players, extraPacket);
        }
    }

    default void syncUpdate(ServerPlayer player) {
        this.syncUpdate(List.of(player));
    }

    default void syncUpdate(List<ServerPlayer> players) {
        CustomPacketPayload packet = this.createUpdatePacket();
        this.sendPacketToPlayers(players, packet);

        // TODO - custom bundle packet
        List<CustomPacketPayload> extraPackets = this.createExtraUpdatePackets();
        for (CustomPacketPayload extraPacket : extraPackets) {
            this.sendPacketToPlayers(players, extraPacket);
        }

        this.setDirty(false);
    }

    default void syncRemove(ServerPlayer player) {
        this.syncRemove(List.of(player));
    }

    default void syncRemove(List<ServerPlayer> players) {
        CustomPacketPayload packet = this.createRemovePacket();
        this.sendPacketToPlayers(players, packet);

        // TODO - custom bundle packet
        List<CustomPacketPayload> extraPackets = this.createExtraRemovePackets();
        for (CustomPacketPayload extraPacket : extraPackets) {
            this.sendPacketToPlayers(players, extraPacket);
        }
    }

    default void sendPacketToPlayer(ServerPlayer player, CustomPacketPayload packet) {
        this.sendPacketToPlayers(List.of(player), packet);
    }

    default void sendPacketToPlayers(List<ServerPlayer> players, CustomPacketPayload packet) {
        for (ServerPlayer player : players) {
            if(player == null || player.connection == null) continue;
            sendPacketToPlayer(player.connection, packet);
        }
    }

    default void sendPacketToPlayer(ServerPlayerConnection networkHandler, CustomPacketPayload packet) {
        ServerPlayer player = networkHandler.getPlayer();
        ServerPlayNetworking.send(player, packet);
    }

    CustomPacketPayload createAddPacket();

    CustomPacketPayload createUpdatePacket();

    CustomPacketPayload createRemovePacket();

    default List<CustomPacketPayload> createExtraAddPackets() {
        return List.of();
    }

    default List<CustomPacketPayload> createExtraUpdatePackets() {
        return List.of();
    }

    default List<CustomPacketPayload> createExtraRemovePackets() {
        return List.of();
    }
}
