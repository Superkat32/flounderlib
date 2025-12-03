package net.superkat.flounderlib.api.minigame.v1.game;

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
    public BlockPos centerPos;
    private int minigameId;

    public int ticks = 0;
    public boolean invalidated = false;
    public Set<UUID> playerUuids = new HashSet<>();

    /**
     * Main constructor for minigames - intended for use when initially creating a minigame.
     *
     * @param centerPos This minigame's center BlockPos
     */
    public FlounderGame(BlockPos centerPos) {
        this.centerPos = centerPos;
    }

    /**
     * Main constructor for minigames from their codecs - intended for use for a minigame's creation from their codec. <br><br>
     * Nothing else special happens besides giving a simple way of setting the current tick count.
     *
     * @param ticks This minigame's current tick count (intended to be deserialized via your game's codec)
     * @param centerPos This minigame's center BlocKPos (intended to be deserialized via your game's codec)
     */
    public FlounderGame(int ticks, BlockPos centerPos) {
        this.ticks = ticks;
        this.centerPos = centerPos;
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
        this.addPlayerUuid(player.getUuid());
    }

    public void addPlayerUuid(UUID playerUuid) {
        this.playerUuids.add(playerUuid);
    }

    public void removePlayer(ServerPlayerEntity player) {
        this.removePlayerUuid(player.getUuid());
    }

    public void removePlayerUuid(UUID playerUuid) {
        this.playerUuids.remove(playerUuid);
    }

    public boolean containsPlayer(ServerPlayerEntity player) {
        return this.containsPlayerUuid(player.getUuid());
    }

    public boolean containsPlayerUuid(UUID playerUuid) {
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
    public BlockPos getCenterPos() {
        return this.centerPos;
    }

    @Override
    public boolean containsBlockPos(BlockPos pos) {
        return pos.isWithinDistance(this.getCenterPos(), this.getGameType().distance());
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
