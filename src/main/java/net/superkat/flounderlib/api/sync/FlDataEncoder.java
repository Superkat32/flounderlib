package net.superkat.flounderlib.api.sync;

import net.minecraft.nbt.NbtCompound;

@FunctionalInterface
public interface FlDataEncoder<G, T> {
    void encode(NbtCompound buf, G game);
}
