package net.superkat.flounderlib.api.annotation.nbt;

import net.minecraft.nbt.NbtCompound;

import java.io.Serializable;

@FunctionalInterface
public interface NbtRead<T> {
    T deserialize(NbtCompound nbtCompound, String key);
}
