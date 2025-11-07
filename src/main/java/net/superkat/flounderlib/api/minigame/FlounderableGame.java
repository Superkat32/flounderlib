package net.superkat.flounderlib.api.minigame;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.superkat.flounderlib.api.FlounderApi;
import net.superkat.flounderlib.api.minigame.gametype.FlounderGameType;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * The absolute bare minimum methods required for a minigame.<br><br>
 *
 * It is recommended that you extend {@link FlounderGame} instead of just implementing this. However, this is visible in case you wish to do it all yourself.
 */
public interface FlounderableGame {

    /**
     * Called upon minigame creation & re-creation from world save(reading nbt).<br><br>
     * More importantly, this gives you the world! Make sure to save it, otherwise your "world" variable may be null! Alternatively, pass the world into your minigame's constructor<br><br>
     *
     * @param world The World to set the minigame world to.
     * @param minigameId The minigame's integer id for saving the minigame & syncing the minigame (if either are needed)
     */
    void init(ServerWorld world, int minigameId);

    /**
     * Called every tick. This is your most important method, and where most of a minigame's processing is going to happen.<br><br>
     * It <i>should</i> be safe to assume {@link FlounderableGame#init(ServerWorld, int)} has already been called.
     */
    void tick();

    /**
     * Called when the game should be removed. This can happen forcefully via {@link FlounderApi#endMinigame(FlounderableGame)} & commands, or can happen naturally through your game logic(e.g.{@link FlounderableGame#tick()} method).<br><br>
     * Think of it as your last chance to clean up after yourself(e.g. remove entities, teleport players, etc.), because after this, the minigame will be removed and will no longer tick.
     */
    void invalidate();

    /**
     * Called every tick to see if this minigame is ready to be removed from the {@link net.superkat.flounderlib.minigame.FlounderGameManager}.<br>
     * Once removed, your game will stop ticking and will be assumed as finished.<br>
     * @return True if your game is ready to be removed, otherwise return false.
     */
    boolean isInvalidated();

    /**
     * @return List of participating players as {@link ServerPlayerEntity}s.
     */
    List<ServerPlayerEntity> getPlayers();

    /**
     * @return Set of participating players as {@link UUID}s.
     */
    Set<UUID> getPlayerUuids();

    /**
     * The center BlockPos of this minigame, used for tracking and determining spacing between other minigames wanting to start.<br><br>
     *
     * If using {@link FlounderGame}, this, along with {@link FlounderGame#playerSearchDistance()}, will be used to determine when players are considered within bounds.
     *
     * @return The center BlockPos of this minigame.
     */
    BlockPos getCenterPos();

    /**
     * @param pos The checked BlockPos.
     * @return True if this minigame's bounds contains the BlockPos.
     */
    boolean containsBlockPos(BlockPos pos);

    /**
     * The minigame's given integer id during creation. This is used for saving the minigame(differentiating between saved minigames) and more importantly syncing minigames(if they are synced).
     *
     * @return The minigame's given int id, given from {@link  FlounderableGame#init(ServerWorld, int)}.
     */
    int getMinigameId();

    /**
     * The minigame's unique {@link FlounderGameType}. This is used for various default data, and tracking & saving the minigame(if it should be saved).
     *
     * @return The minigame's unique FlounderGameType
     */
    @NotNull
    FlounderGameType<?> getGameType();

    /**
     * The minigame's unique Identifier. This is used for tracking & saving the minigame(if it should be saved).
     *
     * @return The minigame's unique Identifier.
     */
    default Identifier getIdentifier() {
        return this.getGameType().id();
    }

}
