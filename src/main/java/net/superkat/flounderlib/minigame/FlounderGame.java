package net.superkat.flounderlib.minigame;

import net.minecraft.server.world.ServerWorld;
import net.superkat.flounderlib.api.IFlounderGame;

public abstract class FlounderGame implements IFlounderGame {
    public ServerWorld world = null;
    public int ticks = 0;

    @Override
    public void create() {

    }

    @Override
    public void tick() {
        ticks++;
    }

    @Override
    public void invalidate() {

    }

    @Override
    public boolean shouldRemove() {
        return false;
    }

    @Override
    public void setWorld(ServerWorld world) {
        this.world = world;
    }
}
