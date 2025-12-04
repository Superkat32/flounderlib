package net.superkat.flounderlib.impl.minigame.sync;

import net.minecraft.network.RegistryByteBuf;
import net.superkat.flounderlib.api.minigame.v1.sync.FlDataKey;

import java.util.function.Supplier;

public class FlDataValue<T> {
    protected T value;
    protected boolean dirty;

    private final FlDataKey<T> key;
    private final Supplier<T> serverGetter;

    protected FlDataValue(FlDataKey<T> key, Supplier<T> serverGetter) {
        this.key = key;
        this.serverGetter = serverGetter;
    }

    public T getValue() {
        return this.value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public boolean isDirty() {
        return dirty;
    }

    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }

    public void copyFromPacked(Packed<?> packed) {
        this.setValue((T) packed.value);
    }

    protected void updateFromGetter() {
        if(this.serverGetter == null) return;

        T newValue = this.serverGetter.get();
        if(this.getValue() != newValue) {
            this.setValue(newValue);
            this.setDirty(true);
        }
    }

    public FlDataValue.Packed<T> pack() {
        return new Packed<T>(this.key.getId(), this.key, this.getValue());
    }

    public static record Packed<T>(int keyId, FlDataKey<T> key, T value) {
        public static <T> Packed<T> fromBuf(RegistryByteBuf buf, int keyId) {
            FlDataKey<T> dataKey = FlDataKey.getKey(keyId);
            T value = dataKey.read(buf);

            return new Packed<>(keyId, dataKey, value);
        }

        public void write(RegistryByteBuf buf) {
            buf.writeByte(this.keyId);
            this.key.write(buf, value);
        }

        public FlDataValue<T> unpack() {
            return new FlDataValue<>(this.key, () -> this.value); // cursed
        }
    }
}
