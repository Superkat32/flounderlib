package net.superkat.flounderlib.api.sync;

import net.minecraft.nbt.NbtCompound;

@FunctionalInterface
public interface FlDataDecoder<G, T> {
    void decode(NbtCompound nbt, G game);
}
