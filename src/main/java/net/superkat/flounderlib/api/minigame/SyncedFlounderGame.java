package net.superkat.flounderlib.api.minigame;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.PlayerAssociatedNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.superkat.flounderlib.api.IFlounderGame;
import net.superkat.flounderlib.api.sync.FlounderDataSyncer;
import net.superkat.flounderlib.network.sync.packets.FlounderGameUpdateS2CPacket;

import java.util.Set;

public interface SyncedFlounderGame<T extends SyncedFlounderGame<?>> extends IFlounderGame {

    FlounderDataSyncer<T> getFlounderDataSyncer();

    default boolean isDirty() {
        return this.getFlounderDataSyncer().isDirty();
    }

    default void setDirty(boolean dirty) {
        this.getFlounderDataSyncer().setDirty(dirty);
    }

    default void tickDataSyncer() {
        this.getFlounderDataSyncer().tick();
    }

    default void sync(Set<ServerPlayerEntity> players) {
        CustomPayload packet = this.createUpdatePacket();

        for (ServerPlayerEntity player : players) {
            if(player == null || player.networkHandler == null) continue;
            this.sendPacketToPlayer(player.networkHandler, packet);
        }
        this.setDirty(false);
    }

    default CustomPayload createUpdatePacket() {
        NbtCompound nbt = new NbtCompound();
        this.writeUpdate(nbt);
        return new FlounderGameUpdateS2CPacket(this.getMinigameId(), nbt);
    }

    default void writeUpdate(NbtCompound nbt) {
        // oh my (❁´◡`❁) - is IntelliJ really getting mad that I'm missing a comma after "oh" what is this nonsense
        T self = (T) this;
        this.getFlounderDataSyncer().write(nbt, self);

    }

    default void readUpdate(NbtCompound nbt) {
        T self = (T) this;
        this.getFlounderDataSyncer().read(nbt, self);
    }

    default void sendPacketToPlayer(PlayerAssociatedNetworkHandler networkHandler, CustomPayload payload) {
        ServerPlayerEntity player = networkHandler.getPlayer();
        ServerPlayNetworking.send(player, payload);
    }

    /**
     * Check if the client should tick its instance of this minigame. The client will continue to update any variables as synced, which is helpful for rendering.
     *
     * @return True if the client should tick its instance, or false if it shouldn't.
     */
    default boolean shouldTickClient() {
        return true;
    }

    /**
     * Check if the client should wait for a special minigame-tick packet before ticking its minigame. Setting to true helps the client account for TPS lag and should remain mostly synced between the server & clients, but also increases the overall packets sent(though the tick packet is small with only 1 variable - the minigame's int id)
     *
     * @return True if the client should only tick its instance of this minigame after receiving a special tick packet, or false if the client should tick this minigame itself.
     */
    default boolean syncedClientTick() {
        return false;
    }
}
