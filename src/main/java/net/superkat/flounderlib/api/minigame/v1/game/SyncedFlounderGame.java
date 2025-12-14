package net.superkat.flounderlib.api.minigame.v1.game;

import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.superkat.flounderlib.api.minigame.v1.sync.FlounderStateSyncer;
import net.superkat.flounderlib.impl.minigame.network.packets.FlounderGameAddS2CPacket;
import net.superkat.flounderlib.impl.minigame.network.packets.FlounderGameRemoveS2CPacket;
import net.superkat.flounderlib.impl.minigame.network.packets.FlounderGameUpdateS2CPacket;
import net.superkat.flounderlib.impl.minigame.packed.PackedFlGameInfo;
import net.superkat.flounderlib.impl.minigame.sync.FlSyncValue;
import net.superkat.flounderlib.impl.minigame.sync.FlounderSyncTracker;

import java.util.List;
import java.util.UUID;

public abstract class SyncedFlounderGame extends FlounderGame implements SyncableFlounderableGame {
    protected FlounderSyncTracker<? extends SyncableFlounderableGame> flounderSyncTracker = this.createFlounderSyncState();
    protected boolean isDirty;

    public SyncedFlounderGame(BlockPos centerPos) {
        super(centerPos);
    }

    public SyncedFlounderGame(int ticks, BlockPos centerPos) {
        super(ticks, centerPos);
    }

    @SuppressWarnings("unchecked")
    public <G extends SyncableFlounderableGame> FlounderSyncTracker<G> createFlounderSyncState() {
        FlounderStateSyncer<G, ?> stateSyncer = (FlounderStateSyncer<G, ?>) this.getGameType().stateSyncer();
        G game = (G) this;
        return new FlounderSyncTracker<>(game, stateSyncer.createValues());
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

        ServerPlayer player = this.getPlayer(playerUuid);
        this.syncAdd(player);
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

    public List<FlSyncValue.Packed<?>> getPackedValues(boolean includeAll) {
        return this.flounderSyncTracker.updateAndGetPackedValues(includeAll);
    }

    @Override
    public CustomPacketPayload createAddPacket() {
        return new FlounderGameAddS2CPacket(this.packGameInfo());
    }

    @Override
    public CustomPacketPayload createUpdatePacket() {
        List<FlSyncValue.Packed<?>> dirtyValues = this.getPackedValues(false);
        return new FlounderGameUpdateS2CPacket(this.packGameInfo(), this.getStateSyncer(), dirtyValues);
    }


    @Override
    public CustomPacketPayload createRemovePacket() {
        return new FlounderGameRemoveS2CPacket(this.packGameInfo());
    }

    @Override
    public List<CustomPacketPayload> createExtraAddPackets() {
        List<FlSyncValue.Packed<?>> dirtyValues = getPackedValues(true);
        CustomPacketPayload valuesPacket = new FlounderGameUpdateS2CPacket(PackedFlGameInfo.fromGame(this), this.getStateSyncer(), dirtyValues);
        return List.of(valuesPacket);
    }

    protected PackedFlGameInfo packGameInfo() {
        return PackedFlGameInfo.fromGame(this);
    }

    public FlounderStateSyncer<?, ?> getStateSyncer() {
        return this.getGameType().stateSyncer();
    }
}
