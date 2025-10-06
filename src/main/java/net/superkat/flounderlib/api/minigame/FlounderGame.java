package net.superkat.flounderlib.api.minigame;

import com.google.common.collect.Sets;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public abstract class FlounderGame implements FlounderableGame {
    public ServerWorld world;
    public BlockPos centerBlockPos;
    private int minigameId;

    public int ticks = 0;
    public boolean invalidated = false;
    public Set<UUID> playerUuids = new HashSet<>();

    public FlounderGame(BlockPos centerBlockPos) {
        this.centerBlockPos = centerBlockPos;
    }

    public FlounderGame(int ticks, BlockPos centerBlockPos) {
        this.ticks = ticks;
        this.centerBlockPos = centerBlockPos;
    }

    @Override
    public void init(ServerWorld world, int minigameId) {
        this.world = world;
        this.minigameId = minigameId;
        this.updatePlayers();
    }

    @Override
    public void tick() {
        this.ticks++;

        if(this.ticks % ticksPerPlayerUpdate() == 0) {
            this.updatePlayers();
        }
    }

    @Override
    public void invalidate() {
        this.invalidated = true;
    }

    @Override
    public boolean isInvalidated() {
        return this.invalidated;
    }

    public void addPlayer(ServerPlayerEntity player) {
        this.addPlayer(player.getUuid());
    }

    public void addPlayer(UUID playerUuid) {
        this.playerUuids.add(playerUuid);
    }

    public void removePlayer(ServerPlayerEntity player) {
        this.removePlayer(player.getUuid());
    }

    public void removePlayer(UUID playerUuid) {
        this.playerUuids.remove(playerUuid);
    }

    public boolean containsPlayer(ServerPlayerEntity player) {
        return this.containsPlayer(player.getUuid());
    }

    public boolean containsPlayer(UUID playerUuid) {
        return this.playerUuids.contains(playerUuid);
    }

    public void updatePlayers() {
        Set<ServerPlayerEntity> currentPlayers = Sets.newHashSet(this.getPlayers());
        List<ServerPlayerEntity> nearbyPlayers = this.world.getPlayers(this::playerWithinBounds);

        // Add new nearby players
        for (ServerPlayerEntity nearbyPlayer : nearbyPlayers) {
            if(!currentPlayers.contains(nearbyPlayer)) {
                this.addPlayer(nearbyPlayer);
            }
        }

        // Remove old no longer nearby players
        for (ServerPlayerEntity currentPlayer : currentPlayers) {
            if(!nearbyPlayers.contains(currentPlayer)) {
                this.removePlayer(currentPlayer);
            }
        }
    }

    @Override
    public BlockPos getCenterBlockPos() {
        return this.centerBlockPos;
    }

    @Override
    public boolean containsBlockPos(BlockPos pos) {
        return pos.isWithinDistance(this.getCenterBlockPos(), this.getGameType().distance());
    }

    public boolean playerWithinBounds(ServerPlayerEntity player) {
        return this.containsBlockPos(player.getBlockPos());
    }

    public int playerSearchDistance() {
        return this.getGameType().distance();
    }

    public int ticksPerPlayerUpdate() {
        return 1;
    }

    @Override
    public List<ServerPlayerEntity> getPlayers() {
        return this.playerUuids.stream().map(this::getPlayer).toList();
    }

    @Override
    public Set<UUID> getPlayerUuids() {
        return this.playerUuids;
    }

    public int getPlayerCount() {
        return this.playerUuids.size();
    }

    public ServerPlayerEntity getPlayer(UUID playerUuid) {
        return (ServerPlayerEntity) this.world.getPlayerByUuid(playerUuid);
    }

    @Override
    public int getMinigameId() {
        return this.minigameId;
    }
}
