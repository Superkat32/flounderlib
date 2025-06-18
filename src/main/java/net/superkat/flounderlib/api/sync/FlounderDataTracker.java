package net.superkat.flounderlib.api.sync;

import com.google.common.collect.Sets;
import io.netty.handler.codec.DecoderException;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.PlayerAssociatedNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.collection.Class2IntMap;
import net.superkat.flounderlib.api.minigame.DataTrackedSyncedFlounderGame;
import net.superkat.flounderlib.network.sync.FlTrackedDataHandler;
import net.superkat.flounderlib.network.sync.FlTrackedDataHandlerRegistry;
import net.superkat.flounderlib.network.sync.packets.FlounderDataTrackerUpdateS2CPacket;
import net.superkat.flounderlib.network.sync.packets.FlounderGameCreationS2CPacket;
import net.superkat.flounderlib.network.sync.packets.FlounderGameDestroyS2CPacket;
import org.apache.commons.lang3.ObjectUtils;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.VisibleForTesting;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class FlounderDataTracker {
    private static final Class2IntMap CLASS_TO_LAST_ID = new Class2IntMap();

    private final DataTrackedSyncedFlounderGame trackedGame;
    private final FlounderDataTracker.Entry<?>[] entries;
    private final Set<PlayerAssociatedNetworkHandler> listeners = Sets.newIdentityHashSet();
    private boolean dirty = true;

    public FlounderDataTracker(DataTrackedSyncedFlounderGame trackedGame, FlounderDataTracker.Entry<?>[] entries) {
        this.trackedGame = trackedGame;
        this.entries = entries;
    }

    public static <T>FlTrackedData<T> registerData(Class<? extends DataTrackedSyncedFlounderGame> minigameClass, FlTrackedDataHandler<T> handler) {
        int dataId = CLASS_TO_LAST_ID.put(minigameClass);
        if(dataId > 254) {
            // idk why 254 is max but I'm guessing it's something to do with packet sizes?
            throw new IllegalArgumentException("Data value id is too big with " + dataId + "! (Max is 254)");
        } else {
            return new FlTrackedData<>(dataId, handler);
        }
    }

    public void addPlayerListener(ServerPlayerEntity player) {
        this.sendPacketsToNewPlayer(player);
        this.listeners.add(player.networkHandler);
    }

    public void removePlayerListener(ServerPlayerEntity player) {
        FlounderGameDestroyS2CPacket packet = new FlounderGameDestroyS2CPacket(this.trackedGame.getMinigameId());
        sendPacket(player.networkHandler, packet);
        this.listeners.remove(player.networkHandler);
    }

    public void removeAllListeners() {
        for (PlayerAssociatedNetworkHandler listener : this.listeners) {
            this.removePlayerListener(listener.getPlayer());
        }
    }

    public void tick() {
        if(!this.isDirty()) return;
        this.syncData();
    }

    public void sendPacketsToNewPlayer(ServerPlayerEntity player) {
        int minigameId = this.trackedGame.getMinigameId();
        FlounderGameCreationS2CPacket creationPacket = new FlounderGameCreationS2CPacket(minigameId, this.trackedGame);
        this.sendPacket(player.networkHandler, creationPacket);

        List<SerializedEntry<?>> changedEntries = getChangedEntries();
        if(changedEntries == null) return;

        CustomPayload packet = new FlounderDataTrackerUpdateS2CPacket(minigameId, changedEntries);
        this.sendPacket(player.networkHandler, packet);
    }

    public void syncData() {
        List<SerializedEntry<?>> dirtyEntries = getDirtyEntries();
        if(dirtyEntries == null) return;

        int minigameId = this.trackedGame.getMinigameId();
        CustomPayload packet = new FlounderDataTrackerUpdateS2CPacket(minigameId, dirtyEntries);
        for (PlayerAssociatedNetworkHandler listener : this.listeners) {
            this.sendPacket(listener, packet);
        }
    }

    @Nullable
    public List<SerializedEntry<?>> getDirtyEntries() {
        if(!this.isDirty()) return null;

        this.setDirty(false);
        List<SerializedEntry<?>> list = new ArrayList<>();

        for (FlounderDataTracker.Entry<?> entry : this.entries) {
            if(entry.isDirty()) {
                entry.setDirty(false);
                list.add(entry.toSerialized());
            }
        }

        if(list.isEmpty()) return null;
        return list;
    }

    @Nullable
    public List<SerializedEntry<?>> getChangedEntries() {
        List<SerializedEntry<?>> list = new ArrayList<>();

        for (Entry<?> entry : this.entries) {
            if(entry.isUnchanged()) continue;
            list.add(entry.toSerialized());
        }

        if(list.isEmpty()) return null;
        return list;
    }

    public void sendPacket(PlayerAssociatedNetworkHandler player, CustomPayload payload) {
        ServerPlayerEntity serverPlayer = player.getPlayer();
        ServerPlayNetworking.send(serverPlayer, payload);
    }

    public void writeUpdatedEntries(List<SerializedEntry<?>> entries) {
        for (FlounderDataTracker.SerializedEntry<?> serializedEntry : entries) {
            copyEntry(serializedEntry);
        }
    }

    @SuppressWarnings("unchecked")
    private <T> void copyEntry(SerializedEntry<T> serializedEntry) {
        // yeah sure okay that's very safe
        FlounderDataTracker.Entry<T> entry = (Entry<T>) this.entries[serializedEntry.entryId];
        entry.set(serializedEntry.value);
    }

    public <T> T get(FlTrackedData<T> data) {
        return this.getEntry(data).get();
    }

    public <T> void set(FlTrackedData<T> key, T value) {
        this.set(key, value, false);
    }

    public <T> void set(FlTrackedData<T> key, T value, boolean force) {
        FlounderDataTracker.Entry<T> entry = this.getEntry(key);
        if(force || ObjectUtils.notEqual(value, entry.get())) {
            entry.set(value);
            entry.setDirty(true);
            this.setDirty(true);
        }
    }

    private <T> FlounderDataTracker.Entry<T> getEntry(FlTrackedData<T> key) {
        return (FlounderDataTracker.Entry<T>) this.entries[key.dataId()];
    }

    public boolean isDirty() {
        return this.dirty;
    }

    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }

    @VisibleForTesting
    public Entry<?>[] getEntries() {
        return entries;
    }

    public static class Builder {
        private final DataTrackedSyncedFlounderGame trackedGame;
        private final FlounderDataTracker.Entry<?>[] entries;

        public Builder(DataTrackedSyncedFlounderGame trackedGame) {
            this.trackedGame = trackedGame;
            this.entries = new FlounderDataTracker.Entry[FlounderDataTracker.CLASS_TO_LAST_ID.getNext(trackedGame.getClass())];
        }

        public <T> FlounderDataTracker.Builder add(FlTrackedData<T> data, T value) {
            int dataId = data.dataId();
            if (dataId > this.entries.length) {
                throw new IllegalArgumentException("Data value id is too big with " + dataId + "! (Max is " + this.entries.length + ")");
            } else if (this.entries[dataId] != null) {
                throw new IllegalArgumentException("Duplicate id value for " + dataId + "!");
            } else {
                this.entries[data.dataId()] = new FlounderDataTracker.Entry<>(data, value);
                return this;
            }
        }

        public FlounderDataTracker build() {
            return new FlounderDataTracker(this.trackedGame, this.entries);
        }

    }

    public static class Entry<T> {
        private final FlTrackedData<T> data;
        private T value;
        private final T initValue;
        private boolean dirty;

        public Entry(FlTrackedData<T> data, T value) {
            this.data = data;
            this.value = value;
            this.initValue = value;
        }

        public T get() {
            return this.value;
        }

        public void set(T value) {
            this.value = value;
        }

        public void setDirty(boolean dirty) {
            this.dirty = dirty;
        }

        public boolean isDirty() {
            return this.dirty;
        }

        public boolean isUnchanged() {
            return this.initValue.equals(this.value);
        }

        public SerializedEntry<?> toSerialized() {
            return SerializedEntry.of(this.data, this.get());
        }
    }

    public record SerializedEntry<T>(int entryId, FlTrackedDataHandler<T> handler, T value) {

        public static <T> SerializedEntry<T> of(FlTrackedData<T> data, T value) {
            return new SerializedEntry<>(data.dataId(), data.handler(), value);
        }

        public void write(RegistryByteBuf buf) {
            int handlerId = FlTrackedDataHandlerRegistry.HANDLER_REGISTRY.getRawId(this.handler);
            buf.writeByte(this.entryId);
            buf.writeVarInt(handlerId);
            this.handler().codec().encode(buf, this.value);
        }

        public static SerializedEntry<?> fromBuf(RegistryByteBuf buf, int entryId) {
            int handlerId = buf.readVarInt();
            FlTrackedDataHandler<?> handler = FlTrackedDataHandlerRegistry.HANDLER_REGISTRY.get(handlerId);
            if(handler == null) {
                throw new DecoderException("Unknown FlTrackedDataHandler type " + handlerId);
            } else {
                return fromBuf(buf, entryId, handler);
            }
        }

        private static <T> SerializedEntry<T> fromBuf(RegistryByteBuf buf, int entryId, FlTrackedDataHandler<T> handler) {
            return new SerializedEntry<>(entryId, handler, handler.codec().decode(buf));
        }
    }
}