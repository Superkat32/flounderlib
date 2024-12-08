package net.superkat.flounderlib.api;

/**
 * The absolute bare minimum methods required for a minigame. It is recommended that you extend {@link net.superkat.flounderlib.minigame.FlounderGame} instead of just implementing this.
 */
public interface IFlounderGame {
    /**
     * Called right when a minigame has been created.
     */
    void create();

    /**
     * Called to start a minigame's actual gameplay.
     */
    void start();

    /**
     * Called every tick. This is where most of a minigame's processing is going to happen.
     */
    void tick();

    /**
     * @return If the minigame should end(players won/lost, game over, etc.)
     */
    boolean shouldEnd();

    /**
     * Called when a minigame has ended. Winning or losing sequences should be started here. Does not necessary mean the minigame is ready to be removed right away.
     */
    void end();

    /**
     * Called when a minigame is to be removed.
     */
    void invalidate();
}
