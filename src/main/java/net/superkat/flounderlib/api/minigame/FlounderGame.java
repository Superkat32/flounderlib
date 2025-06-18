package net.superkat.flounderlib.api.minigame;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import net.superkat.flounderlib.api.IFlounderGame;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public abstract class FlounderGame implements IFlounderGame {
    public World world = null;
    public int minigameId;
    public int ticks = 0;
    public boolean invalidated = false;
    public Set<UUID> playerUuids = new HashSet<>();

    @Override
    public void initialize(World world, int minigameId) {
        this.world = world;
        this.minigameId = minigameId;
    }

    @Override
    public void tick() {
        ticks++;
    }

    @Override
    public void invalidate() {
        this.invalidated = true;
        this.onInvalidate();
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

    public Set<ServerPlayerEntity> getPlayers() {
        Set<ServerPlayerEntity> players = new HashSet<>();
        if(this.world == null) return players;

        for (UUID playerUuid : playerUuids) {
            ServerPlayerEntity player = (ServerPlayerEntity) this.world.getPlayerByUuid(playerUuid);
            if(player == null) continue;

            players.add(player);
        }
        return players;
    }

    public int playerCount() {
        return this.playerUuids.size();
    }

    public Set<UUID> getPlayerUuids() {
        return this.playerUuids;
    }

    @Override
    public final int getMinigameId() {
        return this.minigameId;
    }
}
