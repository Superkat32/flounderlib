package net.superkat.flounderlib.minigame;

import net.superkat.flounderlib.api.IFlounderGame;
import net.superkat.flounderlib.api.annotation.MinigameNbt;

public class FlounderGame implements IFlounderGame {
    @MinigameNbt
    public int ticks = 0;

    @Override
    public void create() {

    }

    @Override
    public void start() {

    }

    @Override
    public void tick() {
        ticks++;
    }

    @Override
    public boolean shouldEnd() {
        return false;
    }

    @Override
    public void end() {

    }

    @Override
    public void invalidate() {

    }
}
