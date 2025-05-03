package net.superkat.flounderlib.api;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

/**
 * The absolute bare minimum methods required for a minigame. It is recommended that you extend {@link net.superkat.flounderlib.minigame.FlounderGame} instead of just implementing this.
 */
public interface IFlounderGame {

    /**
     * Called upon minigame creation & re-creation from world save(reading nbt).<br><br>
     * Most importantly, this gives you the server world! Make sure to save it, otherwise your "world" variable may be null!
     *
     * @param world The ServerWorld to set the minigame world to
     */
    void create(ServerWorld world);

    /**
     * Called every tick. This is your most important method, and where most of a minigame's processing is going to happen.
     */
    void tick();

    /**
     * Called when the game should be removed. This can happen forcefully via {@link FlounderApi#endGame(IFlounderGame)} & commands, or can happen naturally through your {@link IFlounderGame#tick()} method.<br><br>
     * Think of it as your last chance to clean up after yourself(e.g. remove entities, teleport players, etc.), because after this, the minigame will be removed and will no longer tick.
     */
    void invalidate();

    boolean isInvalidated();

    /**
     * Called every tick to see if a minigame is ready to be removed from the {@link net.superkat.flounderlib.minigame.FlounderGameManager}.<br>
     * Return true if it should be removed, otherwise return false.
     */
    boolean shouldRemove();


    /**
     * The minigame's unique Identifier. This is used for tracking & saving the minigame(if it should be saved).
     *
     * @return The minigame's unique Identifier.
     */
    @NotNull
    Identifier getIdentifier();
}
