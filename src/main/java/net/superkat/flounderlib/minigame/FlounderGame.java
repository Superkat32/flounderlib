package net.superkat.flounderlib.minigame;

import net.superkat.flounderlib.api.IFlounderGame;

public abstract class FlounderGame implements IFlounderGame {
    public int ticks = 0;

    @Override
    public void create() {

    }

    public void start() {

    }

    @Override
    public void tick() {
        ticks++;
    }

    public boolean shouldEnd() {
        return false;
    }

    public void end() {

    }

    @Override
    public void invalidate() {

    }

    @Override
    public boolean shouldRemove() {
        return false;
    }
}
