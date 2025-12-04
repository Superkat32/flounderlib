package net.superkat.flounderlib.api.minigame.v1.game;

import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.superkat.flounderlib.impl.minigame.network.packets.FlounderGameAddS2CPacket;
import net.superkat.flounderlib.impl.minigame.network.packets.FlounderGameRemoveS2CPacket;
import net.superkat.flounderlib.impl.minigame.network.packets.FlounderGameUpdateS2CPacket;
import net.superkat.flounderlib.impl.minigame.packed.PackedFlGameInfo;
import net.superkat.flounderlib.impl.minigame.sync.FlDataValue;
import net.superkat.flounderlib.impl.minigame.sync.FlounderSyncState;

import java.util.List;
import java.util.UUID;

public abstract class SyncedFlounderGame extends FlounderGame implements SyncableFlounderableGame {
    protected FlounderSyncState flounderSyncState = this.createFlounderSyncState();
    protected boolean isDirty;

    public SyncedFlounderGame(BlockPos centerPos) {
        super(centerPos);
    }

    public SyncedFlounderGame(int ticks, BlockPos centerPos) {
        super(ticks, centerPos);
    }

    public abstract void addDataValues(FlounderSyncState.Builder builder);

    public FlounderSyncState createFlounderSyncState() {
        FlounderSyncState.Builder builder = new FlounderSyncState.Builder();
        this.addDataValues(builder);

        return builder.build();
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

        ServerPlayerEntity player = this.getPlayer(playerUuid);
        this.syncAdd(player);

        List<FlDataValue.Packed<?>> dirtyValues = getPackedValues(true);
        CustomPayload valuesPacket = new FlounderGameUpdateS2CPacket(PackedFlGameInfo.fromGame(this), dirtyValues);
        this.sendPacketToPlayer(player, valuesPacket);
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

    public List<FlDataValue.Packed<?>> getPackedValues(boolean sendAll) {
        return this.flounderSyncState.getValuesAndPackEmUp(sendAll);
    }

    @Override
    public CustomPayload createAddPacket() {
        return new FlounderGameAddS2CPacket(PackedFlGameInfo.fromGame(this));
    }

    @Override
    public CustomPayload createUpdatePacket() {
        List<FlDataValue.Packed<?>> dirtyValues = getPackedValues(false);
        return new FlounderGameUpdateS2CPacket(PackedFlGameInfo.fromGame(this), dirtyValues);
    }

    @Override
    public CustomPayload createRemovePacket() {
        return new FlounderGameRemoveS2CPacket(PackedFlGameInfo.fromGame(this));
    }
}
