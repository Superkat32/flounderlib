package net.superkat.flounderlib.api;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.superkat.flounderlib.api.minigame.FlounderableGame;
import net.superkat.flounderlib.api.minigame.gametype.FlounderGameType;
import net.superkat.flounderlib.api.minigame.util.FlounderGameStartResult;
import net.superkat.flounderlib.duck.FlounderWorld;
import net.superkat.flounderlib.minigame.FlounderGameManager;
import net.superkat.flounderlib.minigame.FlounderRegistry;

import java.util.List;

public class FlounderApi {

    public static FlounderGameStartResult startMinigame(ServerWorld world, FlounderableGame game) {
        FlounderGameStartResult result = canMinigameStart(world, game.getGameType(), game.getCenterPos());
        if(result.isSuccessful()) {
            addMinigame(world, game);
        }
        return result;
    }

    public static void addMinigame(ServerWorld world, FlounderableGame game) {
        getGameManager(world).addGame(game);
    }

    public static void endMinigame(FlounderableGame game) {
        game.invalidate();
    }

    public static <T extends FlounderableGame> FlounderGameStartResult canMinigameStart(ServerWorld world, FlounderGameType<T> gameType, BlockPos centerPos) {
        if(gameType.singleton() && isMinigameAlreadyRunning(world, gameType)) return FlounderGameStartResult.FAILED_SINGLETON;
        if(!gameType.overlap() && gameTypeContainsPos(world, gameType, centerPos)) return FlounderGameStartResult.FAILED_OVERLAP;
        return FlounderGameStartResult.SUCCESS;
    }

    public static boolean minigameContainsPos(ServerWorld world, BlockPos centerPos) {
        return !findMinigamesAt(world, centerPos).isEmpty();
    }

    public static List<FlounderableGame> findMinigamesAt(ServerWorld world, BlockPos pos) {
        return getGameManager(world).findGamesAt(pos);
    }

    public static <T extends FlounderableGame> boolean gameTypeContainsPos(ServerWorld world, FlounderGameType<T> gameType, BlockPos centerPos) {
        return !findGameTypeAt(world, gameType, centerPos).isEmpty();
    }

    public static <T extends FlounderableGame> List<FlounderableGame> findGameTypeAt(ServerWorld world, FlounderGameType<T> gameType, BlockPos pos) {
        return getGameManager(world).findGameTypeAt(gameType, pos);
    }

    public static <T extends FlounderableGame> boolean isMinigameAlreadyRunning(ServerWorld world, FlounderGameType<T> gameType) {
        return getGameManager(world).doesGameExist(gameType);
    }

    public static <T extends FlounderableGame> FlounderGameType<T> register(FlounderGameType<T> type) {
        return FlounderRegistry.register(type);
    }

    public static <T extends FlounderableGame> FlounderGameType<T> register(FlounderGameType.Builder<T> builder) {
        return register(builder.build());
    }

    public static FlounderGameManager getGameManager(ServerWorld world) {
        return ((FlounderWorld) world).flounderlib$getFlounderGameManager();
    }



}
