package net.superkat.flounderlib.api.minigame.v1.sync;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.phys.Vec3;
import net.superkat.flounderlib.api.minigame.v1.game.FlounderableGame;
import net.superkat.flounderlib.api.minigame.v1.game.SyncableFlounderableGame;
import net.superkat.flounderlib.api.minigame.v1.registry.FlounderGameType;
import net.superkat.flounderlib.api.minigame.v1.sync.function.SyncedDataGetter;
import net.superkat.flounderlib.api.minigame.v1.sync.function.SyncedDataSetter;
import net.superkat.flounderlib.impl.minigame.sync.FlSyncKey;
import net.superkat.flounderlib.impl.minigame.sync.FlSyncValue;
import net.superkat.flounderlib.impl.minigame.sync.FlounderStateSyncerImpl;
import org.jetbrains.annotations.ApiStatus;
import org.joml.Quaternionfc;
import org.joml.Vector3fc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * This acts similar to a Codec, but allows you to define getters and setters for each value.<br><br>
 *
 * {@link SyncedDataGetter} - Allows you to get a value from your FlounderGame<br>
 * {@link SyncedDataSetter} - Allows you to set the gathered value on your FlounderSyncState
 *
 * @see SyncableFlounderableGame
 * @see FlounderSyncState
 *
 * @param <G> Your FlounderGame
 * @param <S> Your FlounderSyncState
 */
public interface FlounderStateSyncer<G extends FlounderableGame, S extends FlounderSyncState> {

    static <G extends SyncableFlounderableGame, S extends FlounderSyncState> FlounderStateSyncer<G, S> create(
            Class<G> gameClass, // Used to define the FlounderableGame generic type
            Supplier<S> clientSyncStateCreationFactory
    ) {
        return new FlounderStateSyncerImpl<>(clientSyncStateCreationFactory);
    }

    default FlounderStateSyncer<G, S> addByte(SyncedDataGetter<G, Byte> getter, SyncedDataSetter<S, Byte> setter) {
        return add(ByteBufCodecs.BYTE, getter, setter);
    }

    default FlounderStateSyncer<G, S> addBoolean(SyncedDataGetter<G, Boolean> getter, SyncedDataSetter<S, Boolean> setter) {
        return add(ByteBufCodecs.BOOL, getter, setter);
    }

    default FlounderStateSyncer<G, S> addShort(SyncedDataGetter<G, Short> getter, SyncedDataSetter<S, Short> setter) {
        return add(ByteBufCodecs.SHORT, getter, setter);
    }

    default FlounderStateSyncer<G, S> addInteger(SyncedDataGetter<G, Integer> getter, SyncedDataSetter<S, Integer> setter) {
        return add(ByteBufCodecs.INT, getter, setter);
    }

    default FlounderStateSyncer<G, S> addLong(SyncedDataGetter<G, Long> getter, SyncedDataSetter<S, Long> setter) {
        return add(ByteBufCodecs.LONG, getter, setter);
    }

    default FlounderStateSyncer<G, S> addFloat(SyncedDataGetter<G, Float> getter, SyncedDataSetter<S, Float> setter) {
        return add(ByteBufCodecs.FLOAT, getter, setter);
    }

    default FlounderStateSyncer<G, S> addDouble(SyncedDataGetter<G, Double> getter, SyncedDataSetter<S, Double> setter) {
        return add(ByteBufCodecs.DOUBLE, getter, setter);
    }

    default FlounderStateSyncer<G, S> addString(SyncedDataGetter<G, String> getter, SyncedDataSetter<S, String> setter) {
        return add(ByteBufCodecs.STRING_UTF8, getter, setter);
    }

    default FlounderStateSyncer<G, S> addNbtElement(SyncedDataGetter<G, Tag> getter, SyncedDataSetter<S, Tag> setter) {
        return add(ByteBufCodecs.TAG, getter, setter);
    }

    default FlounderStateSyncer<G, S> addNbtCompound(SyncedDataGetter<G, CompoundTag> getter, SyncedDataSetter<S, CompoundTag> setter) {
        return add(ByteBufCodecs.COMPOUND_TAG, getter, setter);
    }

    default FlounderStateSyncer<G, S> addBlockPos(SyncedDataGetter<G, BlockPos> getter, SyncedDataSetter<S, BlockPos> setter) {
        return add(BlockPos.STREAM_CODEC, getter, setter);
    }

    default FlounderStateSyncer<G, S> addVector3f(SyncedDataGetter<G, Vector3fc> getter, SyncedDataSetter<S, Vector3fc> setter) {
        return add(ByteBufCodecs.VECTOR3F, getter, setter);
    }

    default FlounderStateSyncer<G, S> addVec3d(SyncedDataGetter<G, Vec3> getter, SyncedDataSetter<S, Vec3> setter) {
        return add(Vec3.STREAM_CODEC, getter, setter);
    }

    default FlounderStateSyncer<G, S> addQuaternionf(SyncedDataGetter<G, Quaternionfc> getter, SyncedDataSetter<S, Quaternionfc> setter) {
        return add(ByteBufCodecs.QUATERNIONF, getter, setter);
    }

    default FlounderStateSyncer<G, S> addText(SyncedDataGetter<G, Component> getter, SyncedDataSetter<S, Component> setter) {
        return add(ComponentSerialization.TRUSTED_CONTEXT_FREE_STREAM_CODEC, getter, setter);
    }

    <V> FlounderStateSyncer<G, S> add(StreamCodec<ByteBuf, V> packetCodec, SyncedDataGetter<G, V> getter, SyncedDataSetter<S, V> setter);

    @ApiStatus.Internal
    S createSyncStateForClient();

    @ApiStatus.Internal
    FlounderGameType<G> getFlounderGameType();

    @ApiStatus.Internal
    void setFlounderGameType(FlounderGameType<G> gameType);

    @ApiStatus.Internal
    Map<Integer, FlSyncKey<G, S, ?>> keys();

    @ApiStatus.Internal
    @SuppressWarnings("unchecked")
    default <V> FlSyncKey<G, S, V> getKey(int id) {
        return (FlSyncKey<G, S, V>) this.keys().get(id);
    }

    @ApiStatus.Internal
    default List<FlSyncValue<G, ?, ?>> createValues() {
        List<FlSyncValue<G, ?, ?>> values = new ArrayList<>();
        for (FlSyncKey<G, S, ?> key : this.keys().values()) {
            values.add(key.createValue());
        }

        return values;
    }

    @ApiStatus.Internal
    int getId();
}
