package net.superkat.flounderlib.api;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.superkat.flounderlib.api.action.FlounderGameStartResult;
import net.superkat.flounderlib.api.gametype.FlounderGameType;
import net.superkat.flounderlib.api.minigame.FlounderableGame;
import net.superkat.flounderlib.duck.FlounderWorld;
import net.superkat.flounderlib.minigame.FlounderGameManager;
import net.superkat.flounderlib.minigame.FlounderRegistry;

import java.util.List;

public class FlounderApi {

    // region Minigame Management

    public static FlounderGameStartResult startMinigame(ServerWorld world, FlounderableGame game) {
        if(canStartMinigame(world, game.getGameType(), game.getCenterBlockPos())) {
            addMinigame(world, game);
            return FlounderGameStartResult.SUCCESS;
        } else {
            return FlounderGameStartResult.FAILED;
        }
    }

    public static void addMinigame(ServerWorld world, FlounderableGame game) {
        getGameManager(world).addGame(game);
    }

    public static void endMinigame(FlounderableGame game) {
        game.invalidate();
    }

    public static <T extends FlounderableGame> boolean canStartMinigame(ServerWorld world, FlounderGameType<T> gameType, BlockPos centerPos) {
        if(gameType.singleton() && isMinigameAlreadyRunning(world, gameType)) return false;

        if(gameType.overlap()) return true;
        return !minigameContainsPos(world, centerPos);
    }

    public static boolean minigameContainsPos(ServerWorld world, BlockPos centerPos) {
        return !findMinigamesAt(world, centerPos).isEmpty();
    }

    public static List<FlounderableGame> findMinigamesAt(ServerWorld world, BlockPos pos) {
        return getGameManager(world).findGamesAt(pos);
    }

    public static <T extends FlounderableGame> boolean isMinigameAlreadyRunning(ServerWorld world, FlounderGameType<T> gameType) {
        return getGameManager(world).doesGameExist(gameType);
    }

    // endregion

    // region Minigame Registration

    public static <T extends FlounderableGame> FlounderGameType<T> register(FlounderGameType<T> type) {
        return FlounderRegistry.register(type);
    }

    // endregion

    // region Minigame Handling

    public static FlounderGameManager getGameManager(ServerWorld world) {
        return ((FlounderWorld) world).flounderlib$getFlounderGameManager();
    }

    // endregion



}
