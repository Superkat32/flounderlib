package net.superkat.flounderlib.impl.minigame.sync;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.superkat.flounderlib.api.minigame.v1.game.FlounderableGame;
import net.superkat.flounderlib.api.minigame.v1.sync.FlounderSyncState;
import net.superkat.flounderlib.api.minigame.v1.sync.function.SyncedDataGetter;
import net.superkat.flounderlib.api.minigame.v1.sync.function.SyncedDataSetter;

public record FlSyncKey<G extends FlounderableGame, S extends FlounderSyncState, V>(
        int id, StreamCodec<ByteBuf, V> packetCodec, SyncedDataGetter<G, V> getter, SyncedDataSetter<S, V> setter
) {

    public FlSyncValue<G, S, V> createValue() {
        return new FlSyncValue<>(this);
    }

    public void write(RegistryFriendlyByteBuf buf, V value) {
        this.packetCodec.encode(buf, value);
    }

    public V read(RegistryFriendlyByteBuf buf) {
        return this.packetCodec.decode(buf);
    }

}
