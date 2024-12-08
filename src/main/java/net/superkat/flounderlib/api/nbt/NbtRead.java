package net.superkat.flounderlib.api.nbt;

import net.minecraft.nbt.NbtCompound;

@FunctionalInterface
public interface NbtRead<T> {
    T deserialize(NbtCompound nbtCompound, String key);
}
