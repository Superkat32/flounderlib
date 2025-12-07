package net.superkat.flounderlib.impl.minigame.sync;

import net.minecraft.network.RegistryByteBuf;
import net.superkat.flounderlib.api.minigame.v1.game.FlounderableGame;
import net.superkat.flounderlib.api.minigame.v1.sync.FlounderStateSyncer;
import net.superkat.flounderlib.api.minigame.v1.sync.FlounderSyncState;
import net.superkat.flounderlib.api.minigame.v1.sync.function.SyncedDataSetter;

public class FlSyncValue<G extends FlounderableGame, S extends FlounderSyncState, V> {
    public final FlSyncKey<G, S, V> key;
    public V currentValue;
    public boolean dirty = true;

    public FlSyncValue(FlSyncKey<G, S, V> key) {
        this.key = key;
    }

    public void updateValue(G game) {
        V newValue = this.key.getter().get(game);
        if(this.currentValue == null || !this.currentValue.equals(newValue)) {
            this.setCurrentValue(newValue);
            this.setDirty(true);
        }
    }

    public boolean isDirty() {
        return dirty;
    }

    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }

    public V getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(V currentValue) {
        this.currentValue = currentValue;
    }

    public int getKeyId() {
        return this.key.id();
    }

    public FlSyncValue.Packed<V> pack() {
        return new FlSyncValue.Packed<>(this.getKeyId(), this.key, this.getCurrentValue());
    }

    public static record Packed<V>(int keyId, FlSyncKey<?, ?, V> key, V value) {
        public static <V> Packed<V> fromBuf(RegistryByteBuf buf, FlounderStateSyncer<?, ?> syncer, int keyId) {
            FlSyncKey<?, ?, V> key = syncer.getKey(keyId);
            V value = key.read(buf);
            return new Packed<>(keyId, key, value);
        }

        public void write(RegistryByteBuf buf) {
            buf.writeByte(this.keyId);
            this.key.write(buf, this.value);
        }

        @SuppressWarnings("unchecked")
        public <S extends FlounderSyncState> void cursedSet(S syncState) {
            SyncedDataSetter<S, V> setter = (SyncedDataSetter<S, V>) this.key.setter();
            setter.set(syncState, this.value);
        }
    }
}
