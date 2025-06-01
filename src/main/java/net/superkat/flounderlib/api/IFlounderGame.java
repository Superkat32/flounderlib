package net.superkat.flounderlib.api;

import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.superkat.flounderlib.api.minigame.FlounderGame;
import net.superkat.flounderlib.minigame.FlounderServerGameManager;
import org.jetbrains.annotations.NotNull;

/**
 * The absolute bare minimum methods required for a minigame. It is recommended that you extend {@link FlounderGame} instead of just implementing this.
 */
public interface IFlounderGame {

    /**
     * Called upon minigame creation & re-creation from world save(reading nbt).<br><br>
     * More importantly, this gives you the world! Make sure to save it, otherwise your "world" variable may be null!<br><br>
     *
     * World is {@link net.minecraft.server.world.ServerWorld} when server-side, and {@link net.minecraft.client.world.ClientWorld} when (and if) on client-side.
     *
     * @param world The World to set the minigame world to.
     * @param id The minigame's integer id for saving the minigame & syncing the minigame (if either are needed)
     */
    void initialize(World world, int id);

    /**
     * Called every tick. This is your most important method, and where most of a minigame's processing is going to happen.
     */
    void tick();

    /**
     * Called when the game should be removed. This can happen forcefully via {@link FlounderApi#endGame(IFlounderGame)} & commands, or can happen naturally through your game logic(e.g.{@link IFlounderGame#tick()} method).<br><br>
     * Think of it as your last chance to clean up after yourself(e.g. remove entities, teleport players, etc.), because after this, the minigame will be removed and will no longer tick.
     */
    void invalidate();

    /**
     * Internal method for specific minigames (e.g. {@link net.superkat.flounderlib.api.minigame.SyncedFlounderGame}) to call required code.
     */
    default void onInvalidate() {}

    /**
     * Called every tick to see if this minigame is ready to be removed from the {@link FlounderServerGameManager}.<br>
     * Once removed, your game will stop ticking and will be assumed as finished.<br>
     * Return true if it should be removed, otherwise return false.
     */
    boolean isInvalidated();

    /**
     * The minigame's unique Identifier. This is used for tracking & saving the minigame(if it should be saved).
     *
     * @return The minigame's unique Identifier.
     */
    @NotNull
    Identifier getIdentifier();

    /**
     * The minigame's given integer id during creation. This is used for saving the minigame(differentiating between saved minigames) and more importantly syncing minigames(if they are synced).
     *
     * @return The minigame's given int id.
     */
    int getMinigameId();
}
