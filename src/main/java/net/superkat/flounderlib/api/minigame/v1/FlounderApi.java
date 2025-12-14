package net.superkat.flounderlib.api.minigame.v1;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.superkat.flounderlib.api.minigame.v1.game.FlounderableGame;
import net.superkat.flounderlib.api.minigame.v1.registry.FlounderGameType;
import net.superkat.flounderlib.api.minigame.v1.util.FlounderGameStartResult;
import net.superkat.flounderlib.impl.minigame.duck.FlounderLevel;
import net.superkat.flounderlib.impl.minigame.game.FlounderGameManager;
import net.superkat.flounderlib.impl.minigame.game.FlounderRegistry;

import java.util.List;

public class FlounderApi {

    public static FlounderGameStartResult startMinigame(ServerLevel world, FlounderableGame game) {
        FlounderGameStartResult result = canMinigameStart(world, game.getGameType(), game.getCenterPos());
        if(result.isSuccessful()) {
            addMinigame(world, game);
        }
        return result;
    }

    public static void addMinigame(ServerLevel world, FlounderableGame game) {
        getGameManager(world).addGame(game);
    }

    public static void endMinigame(FlounderableGame game) {
        game.invalidate();
    }

    public static <T extends FlounderableGame> FlounderGameStartResult canMinigameStart(ServerLevel world, FlounderGameType<T> gameType, BlockPos centerPos) {
        if(gameType.singleton() && isMinigameAlreadyRunning(world, gameType)) return FlounderGameStartResult.FAILED_SINGLETON;
        if(!gameType.overlap() && gameTypeContainsPos(world, gameType, centerPos)) return FlounderGameStartResult.FAILED_OVERLAP;
        return FlounderGameStartResult.SUCCESS;
    }

    public static boolean minigameContainsPos(ServerLevel world, BlockPos centerPos) {
        return !findMinigamesAt(world, centerPos).isEmpty();
    }

    public static List<FlounderableGame> findMinigamesAt(ServerLevel world, BlockPos pos) {
        return getGameManager(world).findGamesAt(pos);
    }

    public static <T extends FlounderableGame> boolean gameTypeContainsPos(ServerLevel world, FlounderGameType<T> gameType, BlockPos centerPos) {
        return !findGameTypeAt(world, gameType, centerPos).isEmpty();
    }

    public static <T extends FlounderableGame> List<FlounderableGame> findGameTypeAt(ServerLevel world, FlounderGameType<T> gameType, BlockPos pos) {
        return getGameManager(world).findGameTypeAt(gameType, pos);
    }

    public static <T extends FlounderableGame> boolean isMinigameAlreadyRunning(ServerLevel world, FlounderGameType<T> gameType) {
        return getGameManager(world).doesGameExist(gameType);
    }

    public static <T extends FlounderableGame> FlounderGameType<T> register(FlounderGameType<T> type) {
        return FlounderRegistry.register(type);
    }

    public static <T extends FlounderableGame> FlounderGameType<T> register(FlounderGameType.Builder<T> builder) {
        return register(builder.build());
    }

    public static FlounderGameManager getGameManager(ServerLevel world) {
        return ((FlounderLevel) world).flounderlib$getFlounderGameManager();
    }



}
