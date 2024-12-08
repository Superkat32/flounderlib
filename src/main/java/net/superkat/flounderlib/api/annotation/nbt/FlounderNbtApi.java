package net.superkat.flounderlib.api.annotation.nbt;

import net.superkat.flounderlib.nbt.NbtSerializer;

import java.util.HashMap;
import java.util.Map;

public interface FlounderNbtApi {

    Map<Class<?>, NbtSerializer<?>> nbtSerializers = new HashMap<>();

    static <T> void registerNbtSerializer(NbtSerializer<T> serializer) {
        Class<?> nbtableClass = serializer.nbtableClazz();
        nbtSerializers.put(nbtableClass, serializer);
    }

}
