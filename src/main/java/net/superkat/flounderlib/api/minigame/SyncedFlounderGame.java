package net.superkat.flounderlib.api.minigame;

import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.math.BlockPos;
import net.superkat.flounderlib.api.sync.FlounderSyncData;
import net.superkat.flounderlib.network.sync.packets.FlounderGameDataUpdateS2CPacket;

public abstract class SyncedFlounderGame<T extends FlounderSyncData> extends FlounderGame implements SyncableFlounderableGame<T> {
    public boolean isDirty;

    public SyncedFlounderGame(BlockPos centerPos) {
        super(centerPos);
    }

    @Override
    public void tick() {
        super.tick();

        if(this.isDirty()) {
            this.sync(this.getPlayers());
        }
    }

    public SyncedFlounderGame(int ticks, BlockPos centerPos) {
        super(ticks, centerPos);
    }

    @Override
    public boolean isDirty() {
        return this.isDirty;
    }

    @Override
    public void setDirty(boolean dirty) {
        this.isDirty = dirty;
    }

    @Override
    public CustomPayload createUpdatePacket() {
        return new FlounderGameDataUpdateS2CPacket<T>(this.getIdentifier(), this.getMinigameId(), this.createData());
    }
}
