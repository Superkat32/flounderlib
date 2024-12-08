package net.superkat.flounderlib.api.annotation.nbt;

import net.minecraft.nbt.NbtCompound;

@FunctionalInterface
public interface NbtWrite<T> {
    void serialize(NbtCompound nbtCompound, String key, T value);
}
