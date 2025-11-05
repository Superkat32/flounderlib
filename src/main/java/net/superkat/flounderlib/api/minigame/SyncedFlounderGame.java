package net.superkat.flounderlib.api.minigame;

import net.minecraft.util.math.BlockPos;
import net.superkat.flounderlib.api.sync.FlounderSyncData;

import java.util.UUID;

public abstract class SyncedFlounderGame<T extends FlounderSyncData> extends FlounderGame implements SyncableFlounderableGame<T> {
    public boolean isDirty;

    public SyncedFlounderGame(BlockPos centerPos) {
        super(centerPos);
    }

    public SyncedFlounderGame(int ticks, BlockPos centerPos) {
        super(ticks, centerPos);
    }

    @Override
    public void tick() {
        super.tick();

        if(this.isDirty()) {
            this.syncUpdate(this.getPlayers());
        }
    }

    @Override
    public void addPlayerUuid(UUID playerUuid) {
        super.addPlayerUuid(playerUuid);

        this.syncAdd(this.getPlayer(playerUuid));
    }

    @Override
    public void removePlayerUuid(UUID playerUuid) {
        super.removePlayerUuid(playerUuid);

        this.syncRemove(this.getPlayer(playerUuid));
    }

    @Override
    public void invalidate() {
        super.invalidate();

        this.syncRemove(this.getPlayers());
    }

    @Override
    public boolean isDirty() {
        return this.isDirty;
    }

    @Override
    public void setDirty(boolean dirty) {
        this.isDirty = dirty;
    }
}
