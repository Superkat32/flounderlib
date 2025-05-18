package net.superkat.flounderlib.api.minigame;

import net.minecraft.server.world.ServerWorld;
import net.superkat.flounderlib.api.IFlounderGame;

public abstract class FlounderGame implements IFlounderGame {
    public ServerWorld world = null;
    public int minigameId;
    public int ticks = 0;
    public boolean invalidated = false;

    @Override
    public void initialize(ServerWorld world, int minigameId) {
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
    }

    @Override
    public boolean isInvalidated() {
        return this.invalidated;
    }

    @Override
    public boolean shouldRemove() {
        return this.isInvalidated();
    }

    @Override
    public final int getMinigameId() {
        return this.minigameId;
    }
}
