package net.superkat.flounderlib.minigame;

import net.minecraft.world.World;
import net.superkat.flounderlib.api.FlounderApi;
import net.superkat.flounderlib.api.IFlounderGame;
import net.superkat.flounderlib.api.event.MinigameEvents;
import net.superkat.flounderlib.api.gametype.FlounderGameType;

import java.util.Map;

public interface FlounderGameManager {

    void tick();

    Map<Integer, IFlounderGame> getGames();

    boolean isClient();

    default int getMinigameIntId(IFlounderGame game) {
        for (Map.Entry<Integer, IFlounderGame> entry : this.getGames().entrySet()) {
            IFlounderGame entryGame = entry.getValue();
            if(!entryGame.equals(game)) continue;
            return entry.getKey();
        }
        return -1;
    }

    default void onMinigameCreate(World world, IFlounderGame game) {
        FlounderGameType<?> type = FlounderApi.getRegistry().get(game.getIdentifier());
        if(type != null) {
            type.addActiveGame(game);
        }
        MinigameEvents.AFTER_MINIGAME_CREATE.invoker().onMinigameCreate(world, game);
    }

    default void onMinigameRemove(World world, IFlounderGame game) {
        FlounderGameType<?> type = FlounderApi.getRegistry().get(game.getIdentifier());
        if(type != null) {
            type.removeActiveGame(game);
        }
        MinigameEvents.AFTER_MINIGAME_REMOVAL.invoker().onMinigameRemoval(world, game);
    }

}
