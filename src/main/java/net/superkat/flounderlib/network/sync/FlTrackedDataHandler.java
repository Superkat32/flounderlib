package net.superkat.flounderlib.network.sync;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.superkat.flounderlib.api.sync.FlTrackedData;

public interface FlTrackedDataHandler<T> {
    PacketCodec<? super RegistryByteBuf, T> codec();

    default FlTrackedData<T> create(int dataId) {
        return new FlTrackedData<>(dataId, this);
    }

    static <T> FlTrackedDataHandler<T> create(PacketCodec<? super RegistryByteBuf, T> codec) {
        return () -> codec;
    }
}
