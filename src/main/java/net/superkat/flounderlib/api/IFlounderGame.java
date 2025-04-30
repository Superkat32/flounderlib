package net.superkat.flounderlib.api;

import net.minecraft.util.Identifier;

/**
 * The absolute bare minimum methods required for a minigame. It is recommended that you extend {@link net.superkat.flounderlib.minigame.FlounderGame} instead of just implementing this.
 */
public interface IFlounderGame {
    /**
     * Called when a minigame has been created.
     */
    void create();

    /**
     * Called every tick. This is your most important method, and where most of a minigame's processing is going to happen.
     */
    void tick();

    /**
     * Called when the game should be removed. This can happen forcefully via {@link FlounderApi#endGame(IFlounderGame)} & commands, or can happen naturally through your {@link IFlounderGame#tick()} method.<br><br>
     * Think of it as your last chance to clean up after yourself(e.g. remove entities, award players, etc.), because after this, the minigame will be removed and will no longer tick.
     */
    void invalidate();

    /**
     * Called every tick to see if a minigame is ready to be removed from the {@link net.superkat.flounderlib.minigame.FlounderGameManager}.<br>
     * Return true if it should be removed, otherwise return false.
     */
    boolean shouldRemove();

    Identifier getIdentifier();
}
