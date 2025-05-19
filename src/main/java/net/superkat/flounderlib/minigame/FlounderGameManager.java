package net.superkat.flounderlib.minigame;

import net.superkat.flounderlib.api.IFlounderGame;

import java.util.Map;

public interface FlounderGameManager {

    void tick();

    Map<Integer, IFlounderGame> getGames();

}
