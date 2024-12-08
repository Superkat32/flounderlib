package net.superkat.flounderlib.nbt;

import net.minecraft.nbt.NbtCompound;
import net.superkat.flounderlib.api.nbt.FlounderNbtApi;
import net.superkat.flounderlib.api.nbt.NbtRead;
import net.superkat.flounderlib.api.nbt.NbtWrite;

import java.util.UUID;

public record NbtSerializer<T>(Class<T> nbtableClazz, NbtWrite<T> writer, NbtRead<T> reader) {

    public void writeNbt(NbtCompound nbtCompound, String key, Object value) {
        try {
            T castedValue = (T) value;
            writer.serialize(nbtCompound, key, castedValue);
        } catch (ClassCastException e) {
            System.out.println("uh oh! " + e);
        }
    }

    public T readNbt(NbtCompound nbtCompound, String key) {
        return reader.deserialize(nbtCompound, key);
    }

    public static void registerTest() {
        FlounderNbtApi.registerNbtSerializer(new NbtSerializer<>(String.class, NbtCompound::putString, NbtCompound::getString));
        FlounderNbtApi.registerNbtSerializer(new NbtSerializer<>(int.class, NbtCompound::putInt, NbtCompound::getInt));
        FlounderNbtApi.registerNbtSerializer(new NbtSerializer<>(UUID.class, NbtCompound::putUuid, NbtCompound::getUuid));
    }

}
