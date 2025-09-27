package net.superkat.flounderlib.api.sync;

import com.mojang.serialization.Codec;
import net.minecraft.nbt.NbtCompound;

import java.util.function.BiConsumer;
import java.util.function.Function;

public interface FlDataSyncer<G, T> extends FlDataEncoder<G, T>, FlDataDecoder<G, T> {
    static <G, T> FlDataSyncer<G, T> tuple(Codec<T> codec, Function<G, T> getter, BiConsumer<G, T> setter) {
        return new FlDataSyncer<>() {
            @Override
            public void decode(NbtCompound nbt, G game) {
                setter.accept(game, nbt.get(FlounderDataSyncer.ENTRY_DATA, codec).orElseThrow());
            }

            @Override
            public void encode(NbtCompound nbt, G game) {
                nbt.put(FlounderDataSyncer.ENTRY_DATA, codec, getter.apply(game));
            }

            @Override
            public String toString() {
                return codec.toString();
            }
        };
    }

//    static <G, T> FlDataSyncer<G, T> tuple(PacketCodec<ByteBuf, T> codec, Function<G, T> getter, BiConsumer<G, T> setter) {
//        return new FlDataSyncer<>() {
//            @Override
//            public void decode(RegistryByteBuf buf, G game) {
//                setter.accept(game, codec.decode(buf));
//            }
//
//            @Override
//            public void encode(RegistryByteBuf buf, G game) {
//                codec.encode(buf, getter.apply(game));
//            }
//        };
//    }
}
