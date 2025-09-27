package net.superkat.flounderlib.api.sync;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.collection.Int2ObjectBiMap;
import net.superkat.flounderlib.api.minigame.SyncedFlounderGame;

public class FlounderDataSyncer<T extends SyncedFlounderGame<?>> {
    public static final String ENTRIES_LIST_ID = "list";
    public static final String ENTRY_ID = "id";
    public static final String ENTRY_DATA = "data";
    public final Int2ObjectBiMap<FlDataSyncer<T, ?>> dataSyncers = Int2ObjectBiMap.create(16);
    public boolean dirty = false;

    @SafeVarargs
    public FlounderDataSyncer(FlDataSyncer<T, ?>... dataSyncers) {
        for (FlDataSyncer<T, ?> dataSyncer : dataSyncers) {
            this.dataSyncers.add(dataSyncer);
        }
    }

    public boolean isDirty() {
        return dirty;
    }

    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }

    public void tick() {

    }

    public void write(NbtCompound nbt, T game) {
        NbtList list = new NbtList();
        for (FlDataSyncer<T, ?> dataSyncer : dataSyncers) {
            NbtCompound compound = new NbtCompound();
            int id = dataSyncers.getRawId(dataSyncer);
            compound.putInt(ENTRY_ID, id);
            dataSyncer.encode(compound, game);
            list.add(compound);
        }
        nbt.put(ENTRIES_LIST_ID, list);
    }

    public void read(NbtCompound nbt, T game) {
        NbtList list = nbt.getListOrEmpty(ENTRIES_LIST_ID);
        for (NbtCompound compound : list.streamCompounds().toList()) {
            int id = compound.getInt(ENTRY_ID).orElseThrow();
            FlDataSyncer<T, ?> dataSyncer = this.dataSyncers.getOrThrow(id);
            dataSyncer.decode(compound, game);
        }
    }
}
