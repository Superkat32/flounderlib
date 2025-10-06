package net.superkat.flounderlib.api.minigame;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.superkat.flounderlib.api.gametype.FlounderGameType;
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

    void invalidate();

    boolean isInvalidated();

    /**
     * @return List of participating players as {@link ServerPlayerEntity}s.
     */
    List<ServerPlayerEntity> getPlayers();

    /**
     * @return Set of participating players as {@link UUID}s.
     */
    Set<UUID> getPlayerUuids();

    BlockPos getCenterBlockPos();

    boolean containsBlockPos(BlockPos pos);

    /**
     * The minigame's given integer id during creation. This is used for saving the minigame(differentiating between saved minigames) and more importantly syncing minigames(if they are synced).
     *
     * @return The minigame's given int id, given from {@link  FlounderableGame#init(ServerWorld, int)}.
     */
    int getMinigameId();

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
