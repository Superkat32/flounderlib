package net.superkat.flounderlib.minigame;

import net.minecraft.server.world.ServerWorld;
import net.superkat.flounderlib.api.IFlounderGame;

public abstract class FlounderGame implements IFlounderGame {
    public ServerWorld world = null;
    public int ticks = 0;
    public boolean invalidated = false;

    @Override
    public void create(ServerWorld world) {
        this.world = world;
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

}
