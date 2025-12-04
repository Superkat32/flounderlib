package net.superkat.flounderlib.impl.minigame.sync;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.superkat.flounderlib.api.minigame.v1.sync.FlDataKey;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class FlounderSyncState {
    public final Map<FlDataKey<?>, FlDataValue<?>> values;

    public FlounderSyncState(Map<FlDataKey<?>, FlDataValue<?>> values) {
        this.values = values;
    }

    public void update(List<FlDataValue.Packed<?>> packedValues) {
        for (FlDataValue.Packed<?> packedValue : packedValues) {
            FlDataKey<?> key = packedValue.key();
            this.values.putIfAbsent(key, packedValue.unpack());
            this.values.get(key).copyFromPacked(packedValue);
        }
    }

    public List<FlDataValue.Packed<?>> getValuesAndPackEmUp(boolean includeAll) {
        List<FlDataValue.Packed<?>> list = new ArrayList<>();
        for (FlDataValue<?> value : values.values()) {
            value.updateFromGetter();

            if(!includeAll && !value.isDirty()) continue;
            list.add(value.pack());
            value.setDirty(false);
        }

        return list;
    }

    @SuppressWarnings("unchecked")
    public <T> FlDataValue<T> getDataValue(FlDataKey<T> dataKey) {
        return (FlDataValue<T>) this.values.get(dataKey);
    }

    public <T> T getValue(FlDataKey<T> dataKey) {
        return getDataValue(dataKey).getValue();
    }

    public static class Builder {
        private final Map<FlDataKey<?>, FlDataValue<?>> map = new Object2ObjectOpenHashMap<>();

        public <T> void addKey(FlDataKey<T> key, Supplier<T> getter) {
            this.map.put(key, new FlDataValue<>(key, getter));
        }

        public FlounderSyncState build() {
            return new FlounderSyncState(map);
        }
    }


}
