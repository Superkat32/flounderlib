package net.superkat.flounderlib.api.minigame.v1.game;

import com.google.common.collect.Sets;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Abstract FlounderGame which handles player management by default (along with other helpful default things, like incrementing the tick int and storing the Level)
 *
 * @see FlounderableGame
 * @see SyncedFlounderGame
 */
public abstract class FlounderGame implements FlounderableGame {
    public ServerLevel level;
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
    public void init(ServerLevel world, int minigameId) {
        this.level = world;
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

    public void addPlayer(ServerPlayer player) {
        if(player == null) return;
        this.addPlayerUuid(player.getUUID());
    }

    public void addPlayerUuid(UUID playerUuid) {
        this.playerUuids.add(playerUuid);
    }

    public void removePlayer(ServerPlayer player) {
        if(player == null) return;
        this.removePlayerUuid(player.getUUID());
    }

    public void removePlayerUuid(UUID playerUuid) {
        this.playerUuids.remove(playerUuid);
    }

    public boolean containsPlayer(ServerPlayer player) {
        if(player == null) return false;
        return this.containsPlayerUuid(player.getUUID());
    }

    public boolean containsPlayerUuid(UUID playerUuid) {
        return this.playerUuids.contains(playerUuid);
    }

    public void updatePlayers() {
        Set<ServerPlayer> currentPlayers = Sets.newHashSet(this.getPlayers());
        List<ServerPlayer> nearbyPlayers = this.level.getPlayers(this::playerWithinBounds);

        // Add new nearby players
        for (ServerPlayer nearbyPlayer : nearbyPlayers) {
            if(!currentPlayers.contains(nearbyPlayer)) {
                this.addPlayer(nearbyPlayer);
            }
        }

        // Remove old no longer nearby players
        for (ServerPlayer currentPlayer : currentPlayers) {
            if(!nearbyPlayers.contains(currentPlayer)) {
                this.removePlayer(currentPlayer);
            }
        }
    }

    public int getTicks() {
        return this.ticks;
    }

    @Override
    public BlockPos getCenterPos() {
        return this.centerPos;
    }

    @Override
    public boolean containsBlockPos(BlockPos pos) {
        return pos.closerThan(this.getCenterPos(), this.getGameType().distance());
    }

    public boolean playerWithinBounds(ServerPlayer player) {
        return this.containsBlockPos(player.blockPosition());
    }

    public int playerSearchDistance() {
        return this.getGameType().distance();
    }

    public int ticksPerPlayerUpdate() {
        return 1;
    }

    @Override
    public List<ServerPlayer> getPlayers() {
        return this.playerUuids.stream().map(this::getPlayer).toList();
    }

    @Override
    public Set<UUID> getPlayerUuids() {
        return this.playerUuids;
    }

    public int getPlayerCount() {
        return this.playerUuids.size();
    }

    public ServerPlayer getPlayer(UUID playerUuid) {
        return this.level.getServer().getPlayerList().getPlayer(playerUuid);
    }

    @Override
    public int getMinigameId() {
        return this.minigameId;
    }
}
