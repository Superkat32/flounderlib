package net.superkat.flounderlib.impl.minigame.sync;

import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import net.minecraft.network.codec.PacketCodec;
import net.superkat.flounderlib.api.minigame.v1.game.FlounderableGame;
import net.superkat.flounderlib.api.minigame.v1.registry.FlounderGameType;
import net.superkat.flounderlib.api.minigame.v1.sync.FlounderStateSyncer;
import net.superkat.flounderlib.api.minigame.v1.sync.FlounderSyncState;
import net.superkat.flounderlib.api.minigame.v1.sync.function.SyncedDataGetter;
import net.superkat.flounderlib.api.minigame.v1.sync.function.SyncedDataSetter;
import org.jetbrains.annotations.ApiStatus;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

public class FlounderStateSyncerImpl<G extends FlounderableGame, S extends FlounderSyncState> implements FlounderStateSyncer<G, S> {
    @ApiStatus.Internal
    private static final Map<Integer, FlounderStateSyncer<?, ?>> SYNCERS = new Int2ObjectArrayMap<>();
    @ApiStatus.Internal
    private static final AtomicInteger ID_INDEX = new AtomicInteger(1);

    public final Supplier<S> clientSyncStateCreationFactory;
    public final Map<Integer, FlSyncKey<G, S, ?>> keys = new Int2ObjectArrayMap<>();
    public final int id;

    public FlounderGameType<G> gameType;

    @ApiStatus.Internal
    public FlounderStateSyncerImpl(
            Supplier<S> clientSyncStateCreationFactory
    ) {
        this.clientSyncStateCreationFactory = clientSyncStateCreationFactory;
        this.id = ID_INDEX.getAndIncrement();

        SYNCERS.put(this.id, this);
    }

    @Override
    public <V> FlounderStateSyncer<G, S> add(PacketCodec<ByteBuf, V> packetCodec, SyncedDataGetter<G, V> getter, SyncedDataSetter<S, V> setter) {
        int id = this.keys.size() + 1;
        FlSyncKey<G, S, V> value = new FlSyncKey<>(id, packetCodec, getter, setter);
        this.keys.put(id, value);
        return this;
    }

    @Override
    public S createSyncStateForClient() {
        return this.clientSyncStateCreationFactory.get();
    }

    @Override
    public FlounderGameType<G> getFlounderGameType() {
        return this.gameType;
    }

    @Override
    public void setFlounderGameType(FlounderGameType<G> gameType) {
        this.gameType = gameType;
    }

    @Override
    public Map<Integer, FlSyncKey<G, S, ?>> keys() {
        return this.keys;
    }

    @ApiStatus.Internal
    @SuppressWarnings("unchecked")
    public static <G extends FlounderableGame, S extends FlounderSyncState> FlounderStateSyncer<G, S> getSyncer(int id) {
        return (FlounderStateSyncer<G, S>) SYNCERS.get(id);
    }

    @Override
    public int getId() {
        return this.id;
    }
}
