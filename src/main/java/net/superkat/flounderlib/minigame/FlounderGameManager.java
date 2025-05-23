package net.superkat.flounderlib.minigame;

import net.superkat.flounderlib.api.IFlounderGame;

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

}
