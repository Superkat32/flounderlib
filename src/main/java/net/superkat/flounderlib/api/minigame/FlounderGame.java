package net.superkat.flounderlib.api.minigame;

import net.minecraft.world.World;
import net.superkat.flounderlib.api.IFlounderGame;

public abstract class FlounderGame implements IFlounderGame {
    public World world = null;
    public int minigameId;
    public int ticks = 0;
    public boolean invalidated = false;

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
    }

    @Override
    public boolean isInvalidated() {
        return this.invalidated;
    }

    @Override
    public final int getMinigameId() {
        return this.minigameId;
    }
}
