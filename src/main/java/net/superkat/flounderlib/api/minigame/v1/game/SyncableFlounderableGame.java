package net.superkat.flounderlib.api.minigame.v1.game;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.PlayerAssociatedNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.superkat.flounderlib.api.minigame.v1.sync.FlounderSyncData;
import net.superkat.flounderlib.impl.minigame.network.packets.FlounderGameDataUpdateS2CPacket;
import net.superkat.flounderlib.impl.minigame.network.packets.FlounderGameRemoveS2CPacket;

import java.util.List;

// TODO - Tracked syncable minigame which syncs each individual value only when updated, instead of each value at once
public interface SyncableFlounderableGame<T extends FlounderSyncData> extends FlounderableGame {

    T createData();

    boolean isDirty();

    void setDirty(boolean dirty);

    default void markDirty() {
        this.setDirty(true);
    }

    default void syncAdd(ServerPlayerEntity player) {
        this.syncAdd(List.of(player));
    }

    default void syncAdd(List<ServerPlayerEntity> players) {
        // NO-OP for now
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

    default CustomPayload createAddPacket() {
        return null;
    }

    default CustomPayload createUpdatePacket() {
        return new FlounderGameDataUpdateS2CPacket<>(this.getIdentifier(), this.getMinigameId(), this.createData());
    }

    default CustomPayload createRemovePacket() {
        return new FlounderGameRemoveS2CPacket(this.getIdentifier(), this.getMinigameId());
    }
}
