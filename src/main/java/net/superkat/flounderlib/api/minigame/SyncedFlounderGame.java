package net.superkat.flounderlib.api.minigame;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.PlayerAssociatedNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.superkat.flounderlib.api.IFlounderGame;
import net.superkat.flounderlib.network.sync.packets.FlounderGameUpdateS2CPacket;

import java.util.Set;

public interface SyncedFlounderGame extends IFlounderGame {

    boolean isDirty();

    void setDirty(boolean dirty);

    default void markDirty() {
        this.setDirty(true);
    }

    default void sync(Set<ServerPlayerEntity> players) {
        FlounderGameUpdateS2CPacket packet = createUpdatePacket();

        for (ServerPlayerEntity player : players) {
            if(player == null) continue;
            if(player.networkHandler == null) continue;
            this.sendPacket(player.networkHandler, packet);
        }
        this.setDirty(false);
    }

    default void sendPacket(PlayerAssociatedNetworkHandler player, CustomPayload payload) {
        ServerPlayerEntity serverPlayer = player.getPlayer();
        ServerPlayNetworking.send(serverPlayer, payload);
    }

    void writeSyncNbt(NbtCompound nbt);

    void readSyncNbt(NbtCompound nbt);

    default FlounderGameUpdateS2CPacket createUpdatePacket() {
        NbtCompound nbt = new NbtCompound();
        this.writeSyncNbt(nbt);
        return new FlounderGameUpdateS2CPacket(this.getMinigameId(), nbt);
    }

}
