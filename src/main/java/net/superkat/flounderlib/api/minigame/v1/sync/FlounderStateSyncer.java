package net.superkat.flounderlib.api.minigame.v1.sync;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.text.Text;
import net.minecraft.text.TextCodecs;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.superkat.flounderlib.api.minigame.v1.game.FlounderableGame;
import net.superkat.flounderlib.api.minigame.v1.game.SyncableFlounderableGame;
import net.superkat.flounderlib.api.minigame.v1.registry.FlounderGameType;
import net.superkat.flounderlib.api.minigame.v1.sync.function.SyncedDataGetter;
import net.superkat.flounderlib.api.minigame.v1.sync.function.SyncedDataSetter;
import net.superkat.flounderlib.impl.minigame.sync.FlSyncKey;
import net.superkat.flounderlib.impl.minigame.sync.FlSyncValue;
import net.superkat.flounderlib.impl.minigame.sync.FlounderStateSyncerImpl;
import org.jetbrains.annotations.ApiStatus;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public interface FlounderStateSyncer<G extends FlounderableGame, S extends FlounderSyncState> {

    static <G extends SyncableFlounderableGame, S extends FlounderSyncState> FlounderStateSyncer<G, S> create(
            Class<G> gameClass, // Used to define the FlounderableGame generic type
            Supplier<S> clientSyncStateCreationFactory
    ) {
        return new FlounderStateSyncerImpl<>(clientSyncStateCreationFactory);
    }

    default FlounderStateSyncer<G, S> addByte(SyncedDataGetter<G, Byte> getter, SyncedDataSetter<S, Byte> setter) {
        return add(PacketCodecs.BYTE, getter, setter);
    }

    default FlounderStateSyncer<G, S> addBoolean(SyncedDataGetter<G, Boolean> getter, SyncedDataSetter<S, Boolean> setter) {
        return add(PacketCodecs.BOOLEAN, getter, setter);
    }

    default FlounderStateSyncer<G, S> addShort(SyncedDataGetter<G, Short> getter, SyncedDataSetter<S, Short> setter) {
        return add(PacketCodecs.SHORT, getter, setter);
    }

    default FlounderStateSyncer<G, S> addInteger(SyncedDataGetter<G, Integer> getter, SyncedDataSetter<S, Integer> setter) {
        return add(PacketCodecs.INTEGER, getter, setter);
    }

    default FlounderStateSyncer<G, S> addLong(SyncedDataGetter<G, Long> getter, SyncedDataSetter<S, Long> setter) {
        return add(PacketCodecs.LONG, getter, setter);
    }

    default FlounderStateSyncer<G, S> addFloat(SyncedDataGetter<G, Float> getter, SyncedDataSetter<S, Float> setter) {
        return add(PacketCodecs.FLOAT, getter, setter);
    }

    default FlounderStateSyncer<G, S> addDouble(SyncedDataGetter<G, Double> getter, SyncedDataSetter<S, Double> setter) {
        return add(PacketCodecs.DOUBLE, getter, setter);
    }

    default FlounderStateSyncer<G, S> addString(SyncedDataGetter<G, String> getter, SyncedDataSetter<S, String> setter) {
        return add(PacketCodecs.STRING, getter, setter);
    }

    default FlounderStateSyncer<G, S> addNbtElement(SyncedDataGetter<G, NbtElement> getter, SyncedDataSetter<S, NbtElement> setter) {
        return add(PacketCodecs.NBT_ELEMENT, getter, setter);
    }

    default FlounderStateSyncer<G, S> addNbtCompound(SyncedDataGetter<G, NbtCompound> getter, SyncedDataSetter<S, NbtCompound> setter) {
        return add(PacketCodecs.NBT_COMPOUND, getter, setter);
    }

    default FlounderStateSyncer<G, S> addBlockPos(SyncedDataGetter<G, BlockPos> getter, SyncedDataSetter<S, BlockPos> setter) {
        return add(BlockPos.PACKET_CODEC, getter, setter);
    }

    default FlounderStateSyncer<G, S> addVector3f(SyncedDataGetter<G, Vector3f> getter, SyncedDataSetter<S, Vector3f> setter) {
        return add(PacketCodecs.VECTOR_3F, getter, setter);
    }

    default FlounderStateSyncer<G, S> addVec3d(SyncedDataGetter<G, Vec3d> getter, SyncedDataSetter<S, Vec3d> setter) {
        return add(Vec3d.PACKET_CODEC, getter, setter);
    }

    default FlounderStateSyncer<G, S> addQuaternionf(SyncedDataGetter<G, Quaternionf> getter, SyncedDataSetter<S, Quaternionf> setter) {
        return add(PacketCodecs.QUATERNION_F, getter, setter);
    }

    default FlounderStateSyncer<G, S> addText(SyncedDataGetter<G, Text> getter, SyncedDataSetter<S, Text> setter) {
        return add(TextCodecs.PACKET_CODEC, getter, setter);
    }

    <V> FlounderStateSyncer<G, S> add(PacketCodec<ByteBuf, V> packetCodec, SyncedDataGetter<G, V> getter, SyncedDataSetter<S, V> setter);

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
